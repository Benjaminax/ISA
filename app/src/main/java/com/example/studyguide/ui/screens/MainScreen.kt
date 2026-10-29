package com.example.studyguide.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyguide.ui.MainViewModel
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentAmber
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.AccentViolet
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftIndigo

data class NavigationTabItem(
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int = 0,
    val badgeColor: Color = AccentEmerald
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

    val pendingChecklists = checklistItems.count { !it.isCompleted }
    val pendingDeadlines = deadlines.count { !it.isCompleted }

    val tabs = listOf(
        NavigationTabItem("Home", Icons.Default.Home),
        NavigationTabItem("Guides", Icons.AutoMirrored.Filled.MenuBook),
        NavigationTabItem("Tasks", Icons.AutoMirrored.Filled.FactCheck, badgeCount = pendingChecklists, badgeColor = AccentEmerald),
        NavigationTabItem("Deadlines", Icons.Default.Event, badgeCount = pendingDeadlines, badgeColor = AccentAmber),
        NavigationTabItem("Saved", Icons.Default.Bookmark),
        NavigationTabItem("Profile", Icons.Default.AccountCircle)
    )

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = when (uiState.selectedTab) {
                                    0 -> "ISA Companion"
                                    1 -> "Explore Guides"
                                    2 -> "Onboarding Tasks"
                                    3 -> "Timeline & Deadlines"
                                    4 -> "Saved Articles"
                                    5 -> "My Profile"
                                    else -> "ISA Companion"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepCharcoal
                            )
                        }

                        // Status Tag with vector icon
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoftIndigo)
                                .border(1.dp, Color(0xFFC7D2FE), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = null,
                                    tint = DeepCharcoal,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Fall 2026",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DeepCharcoal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OffWhite,
                    titleContentColor = DeepCharcoal
                )
            )
        },
        bottomBar = {
            // Master-Class Floating Navigation Capsule Dock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 18.dp,
                            shape = RoundedCornerShape(36.dp),
                            ambientColor = DeepCharcoal.copy(alpha = 0.10f),
                            spotColor = DeepCharcoal.copy(alpha = 0.16f)
                        )
                        .clip(RoundedCornerShape(36.dp))
                        .background(PureWhite)
                        .border(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(PureWhite, BorderZinc)
                            ),
                            RoundedCornerShape(36.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tabs.forEachIndexed { index, item ->
                            val isSelected = uiState.selectedTab == index

                            if (isSelected) {
                                // Active Pill Badge with Icon + Title
                                val pillColor = when (index) {
                                    2 -> AccentEmerald
                                    3 -> AccentAmber
                                    5 -> AccentViolet
                                    else -> DeepCharcoal
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(26.dp))
                                        .background(pillColor)
                                        .clickable { viewModel.selectTab(index) }
                                        .padding(horizontal = 12.dp, vertical = 9.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = PureWhite,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = PureWhite,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            } else {
                                // Inactive Tab with notification dot
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { viewModel.selectTab(index) }
                                        .padding(9.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = MutedZinc,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    if (item.badgeCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(item.badgeColor)
                                                .border(1.dp, PureWhite, CircleShape)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(OffWhite)
        ) {
            // Animated tab content transitions
            AnimatedContent(
                targetState = uiState.selectedTab,
                transitionSpec = {
                    val direction = if (targetState > initialState) 1 else -1
                    (slideInHorizontally(
                        animationSpec = tween(280),
                        initialOffsetX = { it * direction / 4 }
                    ) + fadeIn(tween(280))) togetherWith
                    (slideOutHorizontally(
                        animationSpec = tween(200),
                        targetOffsetX = { -it * direction / 5 }
                    ) + fadeOut(tween(200)))
                },
                label = "tabTransition"
            ) { selectedTab ->
                when (selectedTab) {
                    0 -> HomeScreen(
                        articles = articles,
                        checklistItems = checklistItems,
                        deadlines = deadlines,
                        onNavigateToTab = { viewModel.selectTab(it) },
                        onSelectArticle = { viewModel.selectArticle(it) },
                        onToggleDeadline = { viewModel.toggleDeadline(it) }
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
                        onSelectArticle = { viewModel.selectArticle(it) },
                        onNavigateToGuides = { viewModel.selectTab(1) }
                    )
                    5 -> ProfileScreen(
                        articles = articles,
                        checklistItems = checklistItems,
                        deadlines = deadlines,
                        onNavigateToTab = { viewModel.selectTab(it) }
                    )
                }
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
