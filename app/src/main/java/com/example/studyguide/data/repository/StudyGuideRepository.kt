package com.example.studyguide.data.repository

import com.example.studyguide.data.local.db.dao.ChecklistItemDao
import com.example.studyguide.data.local.db.dao.DeadlineDao
import com.example.studyguide.data.local.db.dao.GuideArticleDao
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyGuideRepository @Inject constructor(
    private val articleDao: GuideArticleDao,
    private val checklistDao: ChecklistItemDao,
    private val deadlineDao: DeadlineDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // --- Guide Articles CRUD ---
    fun getAllArticles(): Flow<List<GuideArticleEntity>> = articleDao.getAllArticles()

    fun getArticlesByCategory(categoryId: String): Flow<List<GuideArticleEntity>> {
        return if (categoryId == "All") {
            articleDao.getAllArticles()
        } else {
            articleDao.getArticlesByCategory(categoryId)
        }
    }

    fun getBookmarkedArticles(): Flow<List<GuideArticleEntity>> = articleDao.getBookmarkedArticles()

    fun searchArticles(query: String): Flow<List<GuideArticleEntity>> = articleDao.searchArticles(query)

    suspend fun addArticle(
        title: String,
        categoryId: String,
        summary: String,
        contentMarkdown: String,
        imageUrl: String
    ) = withContext(ioDispatcher) {
        val article = GuideArticleEntity(
            id = "art_custom_" + UUID.randomUUID().toString().take(8),
            countryCode = "GLOBAL",
            categoryId = categoryId,
            title = title,
            summary = summary,
            contentMarkdown = contentMarkdown,
            imageUrl = imageUrl,
            readTimeMinutes = 3,
            lastUpdated = System.currentTimeMillis()
        )
        articleDao.insertArticle(article)
    }

    suspend fun updateArticle(article: GuideArticleEntity) = withContext(ioDispatcher) {
        articleDao.updateArticle(article)
    }

    suspend fun toggleBookmark(articleId: String, currentBookmarked: Boolean) = withContext(ioDispatcher) {
        articleDao.updateBookmarkStatus(articleId, !currentBookmarked)
    }

    suspend fun deleteArticle(articleId: String) = withContext(ioDispatcher) {
        articleDao.deleteArticleById(articleId)
    }

    // --- Checklist Items CRUD ---
    fun getAllChecklistItems(): Flow<List<ChecklistItemEntity>> = checklistDao.getAllChecklistItems()

    fun getChecklistItemsByStage(stage: String): Flow<List<ChecklistItemEntity>> =
        checklistDao.getChecklistItemsByStage(stage)

    suspend fun addChecklistItem(
        title: String,
        description: String,
        stage: String,
        isMandatory: Boolean
    ) = withContext(ioDispatcher) {
        val item = ChecklistItemEntity(
            id = "chk_custom_" + UUID.randomUUID().toString().take(8),
            countryCode = "GLOBAL",
            stage = stage,
            title = title,
            description = description,
            isCompleted = false,
            isMandatory = isMandatory,
            orderIndex = System.currentTimeMillis().toInt()
        )
        checklistDao.insertChecklistItem(item)
    }

    suspend fun updateChecklistItem(item: ChecklistItemEntity) = withContext(ioDispatcher) {
        checklistDao.updateChecklistItem(item)
    }

    suspend fun toggleChecklistItem(itemId: String, currentCompleted: Boolean) = withContext(ioDispatcher) {
        checklistDao.updateCompletionStatus(itemId, !currentCompleted)
    }

    suspend fun deleteChecklistItem(itemId: String) = withContext(ioDispatcher) {
        checklistDao.deleteChecklistItemById(itemId)
    }

    // --- Deadlines CRUD ---
    fun getAllDeadlines(): Flow<List<DeadlineEntity>> = deadlineDao.getAllDeadlines()

    suspend fun addDeadline(title: String, category: String, dueDateEpochMs: Long, notes: String) =
        withContext(ioDispatcher) {
            val entity = DeadlineEntity(
                id = "dl_custom_" + UUID.randomUUID().toString().take(8),
                title = title,
                category = category,
                dueDateEpochMs = dueDateEpochMs,
                isCompleted = false,
                reminderScheduled = true,
                notes = notes
            )
            deadlineDao.insertDeadline(entity)
        }

    suspend fun updateDeadline(deadline: DeadlineEntity) = withContext(ioDispatcher) {
        deadlineDao.updateDeadline(deadline)
    }

    suspend fun toggleDeadline(deadlineId: String, currentCompleted: Boolean) = withContext(ioDispatcher) {
        deadlineDao.updateCompletionStatus(deadlineId, !currentCompleted)
    }

    suspend fun deleteDeadline(deadlineId: String) = withContext(ioDispatcher) {
        deadlineDao.deleteDeadlineById(deadlineId)
    }

    // --- Initial Seed ---
    suspend fun seedInitialDataIfEmpty() = withContext(ioDispatcher) {
        if (articleDao.getArticleCount() == 0) {
            articleDao.insertArticles(getSampleArticles())
        }
        if (checklistDao.getChecklistItemCount() == 0) {
            checklistDao.insertChecklistItems(getSampleChecklistItems())
        }
        if (deadlineDao.getDeadlineCount() == 0) {
            deadlineDao.insertDeadlines(getSampleDeadlines())
        }
    }

    private fun getSampleArticles(): List<GuideArticleEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            GuideArticleEntity(
                id = "art_visa_1",
                countryCode = "US",
                categoryId = "Visa",
                title = "F-1 Student Visa Application Process",
                summary = "Step-by-step guide to applying for your F-1 visa, paying the SEVIS fee, and scheduling your interview.",
                contentMarkdown = """
                    # F-1 Student Visa Guide
                    
                    Preparing for your study abroad journey requires securing your F-1 visa. Follow these critical steps:
                    
                    1. **Receive Form I-20**: Obtain your I-20 form from your university DSO.
                    2. **Pay SEVIS I-901 Fee**: Visit fmjfee.com and pay the required fee before your interview.
                    3. **Complete DS-160 Form**: Fill out the online nonimmigrant visa application.
                    4. **Schedule Visa Interview**: Book an appointment at your local US embassy or consulate.
                    5. **Prepare Documentation**: Bring your passport, I-20, SEVIS receipt, financial statements, and academic transcripts.
                """.trimIndent(),
                imageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?q=80&w=800&auto=format&fit=crop",
                readTimeMinutes = 5,
                lastUpdated = now
            ),
            GuideArticleEntity(
                id = "art_housing_1",
                countryCode = "GLOBAL",
                categoryId = "Housing",
                title = "Finding & Securing Student Accommodation",
                summary = "Compare on-campus dorms, off-campus apartments, lease contracts, and safety tips.",
                contentMarkdown = """
                    # Student Housing Guide
                    
                    Choosing where to live is one of your biggest decisions when moving abroad.
                    
                    ### On-Campus vs Off-Campus
                    * **On-Campus Dorms**: Convenient, includes utilities, easy way to meet people.
                    * **Off-Campus Apartments**: Greater independence, lower cost when shared with roommates.
                    
                    ### Red Flags to Avoid
                    * Never wire money before seeing a lease agreement or virtual tour.
                    * Always read lease termination terms carefully.
                """.trimIndent(),
                imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=800&auto=format&fit=crop",
                readTimeMinutes = 4,
                lastUpdated = now
            ),
            GuideArticleEntity(
                id = "art_banking_1",
                countryCode = "GLOBAL",
                categoryId = "Banking",
                title = "Opening a Local Student Bank Account",
                summary = "Avoid international transaction fees by setting up a local checking account.",
                contentMarkdown = """
                    # Student Banking & Money Management
                    
                    Setting up a bank account early simplifies paying rent, receiving stipend payments, and handling daily expenses.
                    
                    ### Documents Required
                    1. Valid Passport & Student Visa
                    2. University Acceptance Letter / Student ID
                    3. Proof of Local Address (Lease or Dorm assignment)
                    
                    ### Recommended Features
                    Look for student accounts with zero monthly maintenance fees and fee-free ATM networks.
                """.trimIndent(),
                imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?q=80&w=800&auto=format&fit=crop",
                readTimeMinutes = 3,
                lastUpdated = now
            ),
            GuideArticleEntity(
                id = "art_health_1",
                countryCode = "GLOBAL",
                categoryId = "Health",
                title = "Health Insurance & Medical Services Abroad",
                summary = "Understanding health coverage, university clinics, copays, and mandatory immunizations.",
                contentMarkdown = """
                    # Health & Insurance Requirements
                    
                    Healthcare systems vary greatly around the world. Ensure you are covered from day one.
                    
                    * **University Health Plan**: Most institutions enroll international students automatically.
                    * **Immunization Records**: Submit required vaccine history prior to course registration.
                    * **Urgent Care vs Emergency Room**: Know where to go for non-life-threatening illness to save costs.
                """.trimIndent(),
                imageUrl = "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?q=80&w=800&auto=format&fit=crop",
                readTimeMinutes = 4,
                lastUpdated = now
            ),
            GuideArticleEntity(
                id = "art_academic_1",
                countryCode = "GLOBAL",
                categoryId = "Academic",
                title = "Academic Expectations & Orientation Week",
                summary = "Syllabus system, office hours, grading scales, and academic integrity guidelines.",
                contentMarkdown = """
                    # Succeeding in International Universities
                    
                    Get the most out of your academic experience:
                    
                    * **Attend International Student Orientation**: Crucial for legal compliance and making friends.
                    * **Office Hours**: Take advantage of professor office hours for coursework guidance.
                    * **Academic Integrity**: Understand plagiarism rules and citation standards.
                """.trimIndent(),
                imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=800&auto=format&fit=crop",
                readTimeMinutes = 3,
                lastUpdated = now
            )
        )
    }

    private fun getSampleChecklistItems(): List<ChecklistItemEntity> {
        return listOf(
            ChecklistItemEntity("chk_1", "GLOBAL", "pre_arrival", "Pay SEVIS I-901 Fee", "Complete payment online and print confirmation", false, true, 1),
            ChecklistItemEntity("chk_2", "GLOBAL", "pre_arrival", "Complete DS-160 & Schedule Visa Interview", "Fill form accurately and select embassy location", false, true, 2),
            ChecklistItemEntity("chk_3", "GLOBAL", "pre_arrival", "Book Flight & Arrange Airport Pickup", "Coordinate arrival with university welcoming committee", false, false, 3),
            ChecklistItemEntity("chk_4", "GLOBAL", "pre_arrival", "Secure Accommodation", "Finalize lease agreement or dorm booking", false, true, 4),
            ChecklistItemEntity("chk_5", "GLOBAL", "pre_arrival", "Pack Essential Documents", "Keep Passport, I-20, and transcripts in carry-on bag", false, true, 5),

            ChecklistItemEntity("chk_6", "GLOBAL", "first_week", "Attend International Student Orientation", "Mandatory check-in with university DSO", false, true, 1),
            ChecklistItemEntity("chk_7", "GLOBAL", "first_week", "Open Student Bank Account", "Visit campus branch with passport and I-20", false, true, 2),
            ChecklistItemEntity("chk_8", "GLOBAL", "first_week", "Get Student ID Card & Transit Pass", "Visit student services center", false, false, 3),
            ChecklistItemEntity("chk_9", "GLOBAL", "first_week", "Get Local SIM Card or Mobile Plan", "Compare prepaid vs contract options", false, false, 4),

            ChecklistItemEntity("chk_10", "GLOBAL", "first_month", "Meet Academic Advisor", "Review course selection and degree requirements", false, true, 1),
            ChecklistItemEntity("chk_11", "GLOBAL", "first_month", "Apply for SSN (If Working On-Campus)", "Request employment letter from campus department", false, false, 2),
            ChecklistItemEntity("chk_12", "GLOBAL", "first_month", "Join Student Clubs & Organizations", "Explore campus involvement opportunities", false, false, 3)
        )
    }

    private fun getSampleDeadlines(): List<DeadlineEntity> {
        val now = System.currentTimeMillis()
        val dayMs = 24 * 60 * 60 * 1000L
        return listOf(
            DeadlineEntity("dl_1", "Submit Health & Immunization Forms", "Health", now + 3 * dayMs, false, true, "Upload via student health portal"),
            DeadlineEntity("dl_2", "Tuition Payment Due Date", "Finance", now + 10 * dayMs, false, true, "Pay through student bursar portal"),
            DeadlineEntity("dl_3", "Course Add/Drop Deadline", "Academic", now + 18 * dayMs, false, true, "Final day to adjust class schedule"),
            DeadlineEntity("dl_4", "Housing Deposit Balance", "Housing", now + 25 * dayMs, false, false, "Second payment installment")
        )
    }
}
