package com.example.studyguide.ui

import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.data.repository.StudyGuideRepository
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val repository: StudyGuideRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { repository.getAllArticles() } returns flowOf(
            listOf(
                GuideArticleEntity(
                    id = "art_1",
                    countryCode = "US",
                    categoryId = "Visa",
                    title = "Visa Guide",
                    summary = "Visa Summary",
                    contentMarkdown = "Content"
                ),
                GuideArticleEntity(
                    id = "art_2",
                    countryCode = "US",
                    categoryId = "Housing",
                    title = "Housing Guide",
                    summary = "Housing Summary",
                    contentMarkdown = "Content"
                )
            )
        )
        coEvery { repository.getAllChecklistItems() } returns flowOf(emptyList())
        coEvery { repository.getAllDeadlines() } returns flowOf(emptyList())
        coEvery { repository.getBookmarkedArticles() } returns flowOf(emptyList())

        viewModel = MainViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectTab updates ui state`() = runTest {
        viewModel.selectTab(2)
        val state = viewModel.uiState.first()
        assertEquals(2, state.selectedTab)
    }

    @Test
    fun `selectCategory filters articles`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.selectCategory("Visa")
        testDispatcher.scheduler.advanceUntilIdle()

        val articles = viewModel.articles.first()
        assertEquals(1, articles.size)
        assertEquals("Visa Guide", articles[0].title)
    }

    @Test
    fun `updateSearchQuery filters articles by search term`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.updateSearchQuery("Housing")
        testDispatcher.scheduler.advanceUntilIdle()

        val articles = viewModel.articles.first()
        assertEquals(1, articles.size)
        assertEquals("Housing Guide", articles[0].title)
    }

    @Test
    fun `toggleBookmark calls repository`() = runTest {
        val article = GuideArticleEntity("1", "US", "Visa", "Title", "Summary", "Content", isBookmarked = false)
        viewModel.toggleBookmark(article)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.toggleBookmark("1", false) }
    }

    @Test
    fun `addDeadline invokes repository addDeadline`() = runTest {
        viewModel.addDeadline("Submit Form", "Visa", 1000L, "Notes")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.addDeadline("Submit Form", "Visa", 1000L, "Notes") }
    }
}
