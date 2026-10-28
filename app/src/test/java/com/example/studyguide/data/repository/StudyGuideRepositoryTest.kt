package com.example.studyguide.data.repository

import com.example.studyguide.data.local.db.dao.ChecklistItemDao
import com.example.studyguide.data.local.db.dao.DeadlineDao
import com.example.studyguide.data.local.db.dao.GuideArticleDao
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StudyGuideRepositoryTest {

    private val articleDao: GuideArticleDao = mockk(relaxed = true)
    private val checklistDao: ChecklistItemDao = mockk(relaxed = true)
    private val deadlineDao: DeadlineDao = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: StudyGuideRepository

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = StudyGuideRepository(
            articleDao = articleDao,
            checklistDao = checklistDao,
            deadlineDao = deadlineDao,
            ioDispatcher = testDispatcher
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllArticles returns articles from dao`() = runTest {
        val sampleArticles = listOf(
            GuideArticleEntity(
                id = "1",
                countryCode = "US",
                categoryId = "Visa",
                title = "Visa Guide",
                summary = "Summary",
                contentMarkdown = "Content"
            )
        )
        coEvery { articleDao.getAllArticles() } returns flowOf(sampleArticles)

        val result = repository.getAllArticles().first()
        assertEquals(1, result.size)
        assertEquals("Visa Guide", result[0].title)
    }

    @Test
    fun `seedInitialDataIfEmpty populates database when empty`() = runTest {
        coEvery { articleDao.getArticleCount() } returns 0
        coEvery { checklistDao.getChecklistItemCount() } returns 0
        coEvery { deadlineDao.getDeadlineCount() } returns 0

        repository.seedInitialDataIfEmpty()

        coVerify { articleDao.insertArticles(any()) }
        coVerify { checklistDao.insertChecklistItems(any()) }
        coVerify { deadlineDao.insertDeadlines(any()) }
    }

    @Test
    fun `toggleBookmark updates article bookmark status`() = runTest {
        repository.toggleBookmark("art_1", false)
        coVerify { articleDao.updateBookmarkStatus("art_1", true) }
    }

    @Test
    fun `toggleChecklistItem updates checklist item status`() = runTest {
        repository.toggleChecklistItem("chk_1", true)
        coVerify { checklistDao.updateCompletionStatus("chk_1", false) }
    }

    @Test
    fun `addDeadline inserts new deadline`() = runTest {
        repository.addDeadline(
            title = "Submit Form",
            category = "Visa",
            dueDateEpochMs = 123456789L,
            notes = "Test Note"
        )
        coVerify { deadlineDao.insertDeadline(any()) }
    }

    @Test
    fun `deleteDeadline deletes deadline by id`() = runTest {
        repository.deleteDeadline("dl_1")
        coVerify { deadlineDao.deleteDeadlineById("dl_1") }
    }
}
