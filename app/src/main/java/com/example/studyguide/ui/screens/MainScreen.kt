package com.example.studyguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyguide.ui.MainViewModel
import com.example.studyguide.ui.theme.DarkZinc
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite

data class NavigationTabItem(
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsStateWithLifecycle()
    val checklistItems by viewModel.checklistItems.collectAsStateWithLifecycle()
    val deadlines by viewModel.deadlines.collectAsStateWithLifecycle()

    val tabs = listOf(
        NavigationTabItem("Home", Icons.Default.Home),
        NavigationTabItem("Guides", Icons.Default.Info),
        NavigationTabItem("Checklists", Icons.Default.CheckCircle),
        NavigationTabItem("Deadlines", Icons.Default.DateRange),
        NavigationTabItem("Saved", Icons.Default.Star)
    )

    Scaffold(
        containerColor = PureWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (uiState.selectedTab) {
                            0 -> "StudyAbroad Guide"
                            1 -> "Guides & Resources"
                            2 -> "Onboarding Checklists"
                            3 -> "Deadlines & Reminders"
                            4 -> "Saved Articles"
                            else -> "StudyAbroad Guide"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkZinc
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite,
                    titleContentColor = DarkZinc
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = PureWhite,
                contentColor = DarkZinc
            ) {
                tabs.forEachIndexed { index, item ->
                    val isSelected = uiState.selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(index) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) PureBlack else MutedZinc
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PureBlack else MutedZinc
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = LightZinc
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(PureWhite)
        ) {
            when (uiState.selectedTab) {
                0 -> HomeScreen(
                    articles = articles,
                    checklistItems = checklistItems,
                    deadlines = deadlines,
                    onNavigateToTab = { viewModel.selectTab(it) },
                    onSelectArticle = { viewModel.selectArticle(it) }
                )
                1 -> GuidesScreen(
                    articles = articles,
                    selectedCategory = uiState.selectedCategory,
                    searchQuery = uiState.searchQuery,
                    showAddDialog = uiState.showAddGuideDialog,
                    onShowAddDialogChange = { viewModel.setShowAddGuideDialog(it) },
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                    onSelectArticle = { viewModel.selectArticle(it) },
                    onAddArticle = { title, catId, summary, content, imageUrl ->
                        viewModel.addGuideArticle(title, catId, summary, content, imageUrl)
                    }
                )
                2 -> ChecklistScreen(
                    items = checklistItems,
                    selectedStage = uiState.selectedChecklistStage,
                    showAddDialog = uiState.showAddChecklistDialog,
                    onShowAddDialogChange = { viewModel.setShowAddChecklistDialog(it) },
                    onStageSelected = { viewModel.selectChecklistStage(it) },
                    onToggleItem = { viewModel.toggleChecklistItem(it) },
                    onDeleteItem = { viewModel.deleteChecklistItem(it) },
                    onAddItem = { title, desc, stage, isMandatory ->
                        viewModel.addChecklistItem(title, desc, stage, isMandatory)
                    }
                )
                3 -> DeadlinesScreen(
                    deadlines = deadlines,
                    showAddDialog = uiState.showAddDeadlineDialog,
                    onShowAddDialogChange = { viewModel.setShowAddDeadlineDialog(it) },
                    onToggleDeadline = { viewModel.toggleDeadline(it) },
                    onDeleteDeadline = { viewModel.deleteDeadline(it) },
                    onAddDeadline = { title, cat, dueMs, notes ->
                        viewModel.addDeadline(title, cat, dueMs, notes)
                    }
                )
                4 -> SavedScreen(
                    bookmarkedArticles = bookmarkedArticles,
                    onSelectArticle = { viewModel.selectArticle(it) }
                )
            }
        }
    }

    uiState.selectedArticle?.let { article ->
        ArticleDetailDialog(
            article = article,
            onDismiss = { viewModel.selectArticle(null) },
            onToggleBookmark = { viewModel.toggleBookmark(it) }
        )
    }
}
