package com.example.studyguide.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.GlassHeroCard
import com.example.studyguide.ui.components.GlassPill
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DarkZinc
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    articles: List<GuideArticleEntity>,
    checklistItems: List<ChecklistItemEntity>,
    deadlines: List<DeadlineEntity>,
    onNavigateToTab: (Int) -> Unit,
    onSelectArticle: (GuideArticleEntity) -> Unit
) {
    val totalChecklist = checklistItems.size
    val completedChecklist = checklistItems.count { it.isCompleted }
    val checklistProgress = if (totalChecklist > 0) completedChecklist.toFloat() / totalChecklist else 0f

    val upcomingDeadlines = deadlines.filter { !it.isCompleted }.take(3)
    val featuredArticles = articles.take(4)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Liquid Glass Hero Banner
        item {
            GlassHeroCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlassPill(
                            backgroundColor = PureWhite.copy(alpha = 0.15f),
                            borderColor = PureWhite.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "Fall 2026 Departure",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(PureWhite.copy(alpha = 0.15f))
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Hello, Scholar.",
                        style = MaterialTheme.typography.headlineLarge,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your study abroad dashboard. Pre-arrival tasks, visa application deadlines, and expert guides.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PureWhite.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GlassPill(
                            backgroundColor = PureWhite.copy(alpha = 0.12f),
                            borderColor = PureWhite.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "${upcomingDeadlines.size} Pending Deadlines",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        GlassPill(
                            backgroundColor = PureWhite,
                            borderColor = PureWhite
                        ) {
                            Text(
                                text = "${(checklistProgress * 100).toInt()}% Ready",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Quick Tools Row
        item {
            Column {
                Text(
                    text = "Quick Tools",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkZinc,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        QuickActionChip(
                            title = "Visa Guide",
                            category = "Visa",
                            onClick = { onNavigateToTab(1) }
                        )
                    }
                    item {
                        QuickActionChip(
                            title = "Checklists",
                            category = "Tasks",
                            onClick = { onNavigateToTab(2) }
                        )
                    }
                    item {
                        QuickActionChip(
                            title = "Deadlines",
                            category = "Reminders",
                            onClick = { onNavigateToTab(3) }
                        )
                    }
                    item {
                        QuickActionChip(
                            title = "Housing & Bank",
                            category = "Guides",
                            onClick = { onNavigateToTab(1) }
                        )
                    }
                }
            }
        }

        // Checklist Progress Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToTab(2) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(PureBlack)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Onboarding Checklist",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DarkZinc,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$completedChecklist of $totalChecklist tasks completed",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedZinc
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Checklists",
                            tint = PureBlack
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { checklistProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PureBlack,
                        trackColor = LightZinc
                    )
                }
            }
        }

        // Upcoming Deadlines Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = PureBlack
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Upcoming Deadlines",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkZinc,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = PureBlack,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToTab(3) }
                )
            }
        }

        if (upcomingDeadlines.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No pending deadlines! You are all caught up.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedZinc,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(upcomingDeadlines) { deadline ->
                DeadlineItemCard(deadline = deadline)
            }
        }

        // Essential Guides Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PureBlack
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Essential Guides",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkZinc,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Explore All",
                    style = MaterialTheme.typography.labelLarge,
                    color = PureBlack,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToTab(1) }
                )
            }
        }

        items(featuredArticles) { article ->
            ArticleItemCard(article = article, onClick = { onSelectArticle(article) })
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun QuickActionChip(
    title: String,
    category: String,
    onClick: () -> Unit
) {
    GlassCard(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LightZinc)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = DarkZinc,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkZinc,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedZinc
                )
            }
        }
    }
}

@Composable
fun DeadlineItemCard(deadline: DeadlineEntity) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dueDateString = dateFormat.format(Date(deadline.dueDateEpochMs))

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PureBlack)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = deadline.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = deadline.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkZinc,
                    fontWeight = FontWeight.Bold
                )

                if (deadline.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = deadline.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            GlassPill(
                backgroundColor = LightZinc,
                borderColor = BorderZinc
            ) {
                Text(
                    text = dueDateString,
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkZinc,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ArticleItemCard(
    article: GuideArticleEntity,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            if (article.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DarkZinc)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = article.categoryId,
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${article.readTimeMinutes} min read",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedZinc
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Bookmark",
                            tint = if (article.isBookmarked) PureBlack else BorderZinc,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkZinc,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedZinc,
                    maxLines = 2
                )
            }
        }
    }
}
