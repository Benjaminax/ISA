package com.example.studyguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.components.CircularReadinessGauge
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.GlassHeroCard
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
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftAmber
import com.example.studyguide.ui.theme.SoftCyan
import com.example.studyguide.ui.theme.SoftEmerald
import com.example.studyguide.ui.theme.SoftIndigo
import com.example.studyguide.ui.theme.SoftPurple
import com.example.studyguide.ui.theme.SoftRose

@Composable
fun ProfileScreen(
    articles: List<GuideArticleEntity>,
    checklistItems: List<ChecklistItemEntity>,
    deadlines: List<DeadlineEntity>,
    onNavigateToTab: (Int) -> Unit
) {
    val totalChecklist = checklistItems.size
    val completedChecklist = checklistItems.count { it.isCompleted }
    val totalDeadlines = deadlines.size
    val completedDeadlines = deadlines.count { it.isCompleted }
    val bookmarkedCount = articles.count { it.isBookmarked }
    val overallReadiness = if ((totalChecklist + totalDeadlines) > 0) {
        (completedChecklist + completedDeadlines).toFloat() / (totalChecklist + totalDeadlines)
    } else 0f

    val pendingDeadlines = deadlines.count { !it.isCompleted }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp)
    ) {
        // Identity Hero Card
        item {
            GlassHeroCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(10.dp, CircleShape, ambientColor = PureWhite.copy(alpha = 0.3f))
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                    )
                                )
                                .border(2.dp, PureWhite.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Scholar Victor",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = null,
                                    tint = PureWhite.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "United States • Fall 2026",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PureWhite.copy(alpha = 0.75f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlightTakeoff,
                                    contentDescription = null,
                                    tint = PureWhite.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "114 days to departure",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PureWhite.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Readiness Gauge
                    CircularReadinessGauge(
                        progress = overallReadiness,
                        size = 72.dp,
                        strokeWidth = 7.dp
                    )
                }
            }
        }

        // Stats Grid
        item {
            Text(
                text = "Progress Overview",
                style = MaterialTheme.typography.titleLarge,
                color = DeepCharcoal,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileStatCard(
                    label = "Tasks Done",
                    value = "$completedChecklist",
                    subtitle = "of $totalChecklist total",
                    icon = Icons.Default.CheckCircle,
                    accentColor = AccentEmerald,
                    softBg = SoftEmerald,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(2) }
                )
                ProfileStatCard(
                    label = "Deadlines",
                    value = "$pendingDeadlines",
                    subtitle = "pending action",
                    icon = Icons.Default.Alarm,
                    accentColor = AccentAmber,
                    softBg = SoftAmber,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(3) }
                )
                ProfileStatCard(
                    label = "Saved",
                    value = "$bookmarkedCount",
                    subtitle = "guides saved",
                    icon = Icons.Default.Star,
                    accentColor = AccentIndigo,
                    softBg = SoftIndigo,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(4) }
                )
            }
        }

        // Checklist Category Breakdown
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Checklist Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Your readiness by preparation stage",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val stages = listOf(
                        Triple("Pre-Arrival", "pre_arrival", AccentIndigo),
                        Triple("First Week", "first_week", AccentCyan),
                        Triple("First Month", "first_month", AccentViolet)
                    )

                    stages.forEach { (label, key, color) ->
                        val stageItems = checklistItems.filter { it.stage == key }
                        val stageTotal = stageItems.size
                        val stageDone = stageItems.count { it.isCompleted }
                        val stageProgress = if (stageTotal > 0) stageDone.toFloat() / stageTotal else 0f

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepCharcoal
                                )
                                Text(
                                    text = "$stageDone / $stageTotal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedZinc,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator(
                                progress = { stageProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = color,
                                trackColor = LightZinc
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        // Profile / Account Settings Section
        item {
            Text(
                text = "Profile & Settings",
                style = MaterialTheme.typography.titleLarge,
                color = DeepCharcoal,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ProfileMenuRow(
                        icon = Icons.Default.AccountCircle,
                        iconBg = SoftIndigo,
                        iconColor = AccentIndigo,
                        label = "Personal Information",
                        subtitle = "Scholar Victor • victor@email.com"
                    )
                    ProfileDivider()
                    ProfileMenuRow(
                        icon = Icons.Default.Language,
                        iconBg = SoftCyan,
                        iconColor = AccentCyan,
                        label = "Study Destination",
                        subtitle = "United States — Fall 2026"
                    )
                    ProfileDivider()
                    ProfileMenuRow(
                        icon = Icons.Default.Notifications,
                        iconBg = SoftAmber,
                        iconColor = AccentAmber,
                        label = "Notifications & Reminders",
                        subtitle = "Deadline alerts, weekly digest"
                    )
                    ProfileDivider()
                    ProfileMenuRow(
                        icon = Icons.Default.Shield,
                        iconBg = SoftEmerald,
                        iconColor = AccentEmerald,
                        label = "Privacy & Data",
                        subtitle = "Manage your data & preferences"
                    )
                    ProfileDivider()
                    ProfileMenuRow(
                        icon = Icons.Default.Settings,
                        iconBg = SoftPurple,
                        iconColor = AccentViolet,
                        label = "App Settings",
                        subtitle = "Theme, language, accessibility"
                    )
                }
            }
        }

        // Resources & Support
        item {
            Text(
                text = "Resources",
                style = MaterialTheme.typography.titleLarge,
                color = DeepCharcoal,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ProfileMenuRow(
                        icon = Icons.Default.LocalHospital,
                        iconBg = SoftRose,
                        iconColor = AccentRose,
                        label = "Emergency Contacts",
                        subtitle = "Campus health, embassy, crisis line"
                    )
                    ProfileDivider()
                    ProfileMenuRow(
                        icon = Icons.Default.Info,
                        iconBg = SoftIndigo,
                        iconColor = AccentIndigo,
                        label = "About ISA Companion",
                        subtitle = "Version 1.0 • Build 2026.10"
                    )
                }
            }
        }

        // App version footer
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(DeepCharcoal, Color(0xFF312E81)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ISA Companion",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoal
                    )
                    Text(
                        text = "Study Abroad Mission Control • v1.0",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedZinc
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    label: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    softBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    GlassCard(
        modifier = modifier.pressScaleClickable(onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(softBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = DeepCharcoal
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = DeepCharcoal,
                fontSize = 10.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MutedZinc,
                fontSize = 9.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    label: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScaleClickable {}
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = DeepCharcoal
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MutedZinc,
                maxLines = 1
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MutedZinc,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 72.dp)
            .height(1.dp)
            .background(BorderZinc)
    )
}

