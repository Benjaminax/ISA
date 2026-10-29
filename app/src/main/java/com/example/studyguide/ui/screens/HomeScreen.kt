package com.example.studyguide.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.components.CategoryBadge
import com.example.studyguide.ui.components.CategoryThemes
import com.example.studyguide.ui.components.CircularReadinessGauge
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.GlassHeroCard
import com.example.studyguide.ui.components.GlassPill
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentAmber
import com.example.studyguide.ui.theme.AccentCyan
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.AccentIndigo
import com.example.studyguide.ui.theme.AccentRose
import com.example.studyguide.ui.theme.AccentViolet
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftAmber
import com.example.studyguide.ui.theme.SoftCyan
import com.example.studyguide.ui.theme.SoftEmerald
import com.example.studyguide.ui.theme.SoftIndigo
import com.example.studyguide.ui.theme.SoftPurple
import com.example.studyguide.ui.theme.SoftRose
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    articles: List<GuideArticleEntity>,
    checklistItems: List<ChecklistItemEntity>,
    deadlines: List<DeadlineEntity>,
    onNavigateToTab: (Int) -> Unit,
    onSelectArticle: (GuideArticleEntity) -> Unit,
    onToggleDeadline: (DeadlineEntity) -> Unit = {}
) {
    val totalChecklist = checklistItems.size
    val completedChecklist = checklistItems.count { it.isCompleted }
    val checklistProgress = if (totalChecklist > 0) completedChecklist.toFloat() / totalChecklist else 0f

    val totalDeadlines = deadlines.size
    val completedDeadlines = deadlines.count { it.isCompleted }
    val overallReadiness = if ((totalChecklist + totalDeadlines) > 0) {
        (completedChecklist + completedDeadlines).toFloat() / (totalChecklist + totalDeadlines)
    } else 0f

    val upcomingDeadlines = deadlines.filter { !it.isCompleted }.sortedBy { it.dueDateEpochMs }.take(3)
    val featuredArticles = articles.take(5)

    val animatedProgress by animateFloatAsState(
        targetValue = checklistProgress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animatedChecklistProgress"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Master Greeting Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(6.dp, CircleShape, ambientColor = DeepCharcoal.copy(alpha = 0.15f))
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(DeepCharcoal, Color(0xFF312E81))
                                )
                            )
                            .border(2.dp, PureWhite, CircleShape)
                            .pressScaleClickable { onNavigateToTab(5) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Scholar",
                            tint = PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Scholar Victor",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepCharcoal
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Destination: USA",
                                tint = AccentIndigo,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Fall 2026 • United States Departure",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedZinc,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(4.dp, CircleShape, ambientColor = DeepCharcoal.copy(alpha = 0.08f))
                        .clip(CircleShape)
                        .background(PureWhite)
                        .border(1.dp, BorderZinc, CircleShape)
                        .pressScaleClickable { onNavigateToTab(3) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = DeepCharcoal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Master Hero Departure Card with Readiness Gauge
        item {
            GlassHeroCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        GlassPill(
                            backgroundColor = PureWhite.copy(alpha = 0.15f),
                            borderColor = PureWhite.copy(alpha = 0.25f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlightTakeoff,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "114 DAYS TO TAKEOFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PureWhite,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Study Abroad\nMission Control",
                            style = MaterialTheme.typography.headlineLarge,
                            color = PureWhite,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${upcomingDeadlines.size} critical actions pending before departure.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureWhite.copy(alpha = 0.75f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(PureWhite)
                                    .pressScaleClickable { onNavigateToTab(2) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Tasks",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepCharcoal
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = DeepCharcoal,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(PureWhite.copy(alpha = 0.16f))
                                    .pressScaleClickable { onNavigateToTab(3) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Deadlines",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Circular Readiness Ring
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularReadinessGauge(
                            progress = overallReadiness,
                            size = 80.dp,
                            strokeWidth = 7.dp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Ready Score",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Quick Hub Navigation Cards
        item {
            Column {
                Text(
                    text = "Quick Hub",
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepCharcoal,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickHubCard(
                        title = "Visa Guide",
                        badge = "5 min read",
                        icon = Icons.Default.AssignmentInd,
                        bgSoft = SoftIndigo,
                        accentColor = AccentIndigo,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(1) }
                    )

                    QuickHubCard(
                        title = "Tasks",
                        badge = "$completedChecklist/$totalChecklist Done",
                        icon = Icons.Default.FactCheck,
                        bgSoft = SoftEmerald,
                        accentColor = AccentEmerald,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(2) }
                    )

                    QuickHubCard(
                        title = "Deadlines",
                        badge = "${upcomingDeadlines.size} Active",
                        icon = Icons.Default.Alarm,
                        bgSoft = SoftAmber,
                        accentColor = AccentAmber,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(3) }
                    )

                    QuickHubCard(
                        title = "Housing",
                        badge = "Guides",
                        icon = Icons.Default.Apartment,
                        bgSoft = SoftCyan,
                        accentColor = AccentCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(1) }
                    )
                }
            }
        }

        // Stats Analytics Strip
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tasks stat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderZinc, RoundedCornerShape(18.dp))
                        .pressScaleClickable { onNavigateToTab(2) }
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "$completedChecklist/$totalChecklist",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentEmerald
                        )
                        Text(
                            text = "Tasks Done",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedZinc,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                // Deadlines stat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderZinc, RoundedCornerShape(18.dp))
                        .pressScaleClickable { onNavigateToTab(3) }
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "${upcomingDeadlines.size}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (upcomingDeadlines.isEmpty()) AccentEmerald else AccentAmber
                        )
                        Text(
                            text = "Pending Deadlines",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedZinc,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                // Readiness stat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(PureWhite)
                        .border(1.dp, BorderZinc, RoundedCornerShape(18.dp))
                        .pressScaleClickable { onNavigateToTab(5) }
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "${(overallReadiness * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentIndigo
                        )
                        Text(
                            text = "Overall Ready",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedZinc,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Onboarding Readiness Progress Card        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToTab(2) }
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(SoftEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AccentEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Onboarding Checklist",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DeepCharcoal,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$completedChecklist of $totalChecklist essential steps completed",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedZinc
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Checklists",
                            tint = DeepCharcoal,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AccentEmerald,
                        trackColor = LightZinc
                    )
                }
            }
        }

        // Upcoming Deadlines Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SoftAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Upcoming Deadlines",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightZinc)
                        .pressScaleClickable { onNavigateToTab(3) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (upcomingDeadlines.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentEmerald,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "All caught up! No pending deadlines right now.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedZinc
                        )
                    }
                }
            }
        } else {
            items(upcomingDeadlines) { deadline ->
                MasterDeadlineItemCard(
                    deadline = deadline,
                    onToggle = { onToggleDeadline(deadline) }
                )
            }
        }

        // Featured Guides Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SoftIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = AccentIndigo,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Curated Guides",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightZinc)
                        .pressScaleClickable { onNavigateToTab(1) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Explore All",
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        items(featuredArticles) { article ->
            MasterArticleCard(
                article = article,
                onClick = { onSelectArticle(article) }
            )
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun QuickHubCard(
    title: String,
    badge: String,
    icon: ImageVector,
    bgSoft: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.pressScaleClickable(onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepCharcoal,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MasterDeadlineItemCard(
    deadline: DeadlineEntity,
    onToggle: () -> Unit
) {
    val date = Date(deadline.dueDateEpochMs)
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthStr = monthFormat.format(date).uppercase()
    val dayStr = dayFormat.format(date)

    val now = System.currentTimeMillis()
    val diffDays = ((deadline.dueDateEpochMs - now) / (1000 * 60 * 60 * 24)).toInt()

    val (urgencyText, urgencyBg, urgencyColor) = when {
        deadline.isCompleted -> Triple("Completed", SoftEmerald, AccentEmerald)
        diffDays < 0 -> Triple("Overdue", SoftRose, AccentRose)
        diffDays == 0 -> Triple("Due Today", SoftRose, AccentRose)
        diffDays <= 3 -> Triple("$diffDays days left", SoftRose, AccentRose)
        diffDays <= 7 -> Triple("$diffDays days left", SoftAmber, AccentAmber)
        else -> Triple("$diffDays days left", LightZinc, MutedZinc)
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = PureWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar Badge Block
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightZinc)
                    .border(1.dp, BorderZinc, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = monthStr,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MutedZinc,
                        fontSize = 10.sp
                    )
                    Text(
                        text = dayStr,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepCharcoal,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBadge(category = deadline.category)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(urgencyBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = urgencyText,
                            style = MaterialTheme.typography.labelSmall,
                            color = urgencyColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = deadline.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepCharcoal,
                    fontWeight = FontWeight.Bold
                )

                if (deadline.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = deadline.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (deadline.isCompleted) AccentEmerald else LightZinc)
                    .border(
                        1.5.dp,
                        if (deadline.isCompleted) AccentEmerald else BorderZinc,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (deadline.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = PureWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MasterArticleCard(
    article: GuideArticleEntity,
    onClick: () -> Unit
) {
    val categoryStyle = CategoryThemes.getStyle(article.categoryId)

    GlassCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        elevation = 2.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                    .background(categoryStyle.gradient),
                contentAlignment = Alignment.Center
            ) {
                if (article.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Gradient scrim overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DeepCharcoal.copy(alpha = 0.65f)
                                )
                            )
                        )
                )

                // Category pill on top of image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBadge(category = article.categoryId)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureBlack.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${article.readTimeMinutes} min",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepCharcoal,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedZinc,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
