package com.example.studyguide.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.data.repository.StudyGuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val selectedTab: Int = 0,
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val selectedChecklistStage: String = "pre_arrival",
    val selectedArticle: GuideArticleEntity? = null,
    val showAddDeadlineDialog: Boolean = false,
    val showAddChecklistDialog: Boolean = false,
    val showAddGuideDialog: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StudyGuideRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    val articles: StateFlow<List<GuideArticleEntity>> = combine(
        _uiState,
        repository.getAllArticles()
    ) { state, allArticles ->
        var list = allArticles
        if (state.selectedCategory != "All") {
            list = list.filter { it.categoryId.equals(state.selectedCategory, ignoreCase = true) }
        }
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.trim()
            list = list.filter {
                it.title.contains(q, ignoreCase = true) ||
                it.summary.contains(q, ignoreCase = true) ||
                it.contentMarkdown.contains(q, ignoreCase = true)
            }
        }
        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedArticles: StateFlow<List<GuideArticleEntity>> = repository.getBookmarkedArticles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val checklistItems: StateFlow<List<ChecklistItemEntity>> = repository.getAllChecklistItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val deadlines: StateFlow<List<DeadlineEntity>> = repository.getAllDeadlines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectChecklistStage(stage: String) {
        _uiState.value = _uiState.value.copy(selectedChecklistStage = stage)
    }

    fun selectArticle(article: GuideArticleEntity?) {
        _uiState.value = _uiState.value.copy(selectedArticle = article)
    }

    fun setShowAddDeadlineDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddDeadlineDialog = show)
    }

    fun setShowAddChecklistDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddChecklistDialog = show)
    }

    fun setShowAddGuideDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddGuideDialog = show)
    }

    fun toggleBookmark(article: GuideArticleEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(article.id, article.isBookmarked)
        }
    }

    fun addGuideArticle(title: String, categoryId: String, summary: String, contentMarkdown: String, imageUrl: String) {
        viewModelScope.launch {
            repository.addArticle(title, categoryId, summary, contentMarkdown, imageUrl)
            setShowAddGuideDialog(false)
        }
    }

    fun deleteGuideArticle(articleId: String) {
        viewModelScope.launch {
            repository.deleteArticle(articleId)
        }
    }

    fun toggleChecklistItem(item: ChecklistItemEntity) {
        viewModelScope.launch {
            repository.toggleChecklistItem(item.id, item.isCompleted)
        }
    }

    fun addChecklistItem(title: String, description: String, stage: String, isMandatory: Boolean) {
        viewModelScope.launch {
            repository.addChecklistItem(title, description, stage, isMandatory)
            setShowAddChecklistDialog(false)
        }
    }

    fun deleteChecklistItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteChecklistItem(itemId)
        }
    }

    fun toggleDeadline(deadline: DeadlineEntity) {
        viewModelScope.launch {
            repository.toggleDeadline(deadline.id, deadline.isCompleted)
        }
    }

    fun addDeadline(title: String, category: String, dueDateEpochMs: Long, notes: String) {
        viewModelScope.launch {
            repository.addDeadline(title, category, dueDateEpochMs, notes)
            setShowAddDeadlineDialog(false)
        }
    }

    fun deleteDeadline(deadlineId: String) {
        viewModelScope.launch {
            repository.deleteDeadline(deadlineId)
        }
    }
}
