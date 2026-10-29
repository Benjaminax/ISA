package com.example.studyguide.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.ui.components.CategoryBadge
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.GlassHeroCard
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentAmber
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.AccentRose
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftAmber
import com.example.studyguide.ui.theme.SoftEmerald
import com.example.studyguide.ui.theme.SoftRose
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DeadlineFilterOption(
    val key: String,
    val label: String,
    val icon: ImageVector? = null
)

@Composable
fun DeadlinesScreen(
    deadlines: List<DeadlineEntity>,
    showAddDialog: Boolean,
    onShowAddDialogChange: (Boolean) -> Unit,
    onToggleDeadline: (DeadlineEntity) -> Unit,
    onDeleteDeadline: (String) -> Unit,
    onAddDeadline: (title: String, category: String, dueDateEpochMs: Long, notes: String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }

    val now = System.currentTimeMillis()
    val pendingDeadlines = deadlines.filter { !it.isCompleted }.sortedBy { it.dueDateEpochMs }
    val completedDeadlines = deadlines.filter { it.isCompleted }.sortedByDescending { it.dueDateEpochMs }
    val urgentDeadlines = pendingDeadlines.filter {
        val days = (it.dueDateEpochMs - now) / (1000 * 60 * 60 * 24)
        days <= 7
    }

    val displayedDeadlines = when (selectedFilter) {
        "Urgent" -> urgentDeadlines
        "Upcoming" -> pendingDeadlines
        "Completed" -> completedDeadlines
        else -> deadlines.sortedWith(
            compareBy<DeadlineEntity> { it.isCompleted }.thenBy { it.dueDateEpochMs }
        )
    }

    val totalCount = deadlines.size
    val completedCount = completedDeadlines.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    Scaffold(
        containerColor = OffWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onShowAddDialogChange(true) },
                containerColor = DeepCharcoal,
                contentColor = PureWhite,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .padding(bottom = 88.dp)
                    .shadow(8.dp, RoundedCornerShape(18.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Deadline",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(OffWhite)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Master Hero Card for Deadlines
            GlassHeroCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TIMELINE MONITOR",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${pendingDeadlines.size} Pending • ${completedDeadlines.size} Done",
                                style = MaterialTheme.typography.titleMedium,
                                color = PureWhite,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (urgentDeadlines.isNotEmpty()) SoftRose else SoftEmerald)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (urgentDeadlines.isNotEmpty()) Icons.Default.PriorityHigh else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (urgentDeadlines.isNotEmpty()) AccentRose else AccentEmerald,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (urgentDeadlines.isNotEmpty()) "${urgentDeadlines.size} Urgent" else "On Track",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (urgentDeadlines.isNotEmpty()) AccentRose else AccentEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AccentEmerald,
                        trackColor = PureWhite.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs
            val filterOptions = listOf(
                DeadlineFilterOption("All", "All (${deadlines.size})"),
                DeadlineFilterOption("Urgent", "Urgent (${urgentDeadlines.size})", Icons.Default.Alarm),
                DeadlineFilterOption("Upcoming", "Pending (${pendingDeadlines.size})"),
                DeadlineFilterOption("Completed", "Completed (${completedDeadlines.size})", Icons.Default.CheckCircle)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter.key
                    Box(
                        modifier = Modifier
                            .shadow(if (isSelected) 3.dp else 0.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) DeepCharcoal else PureWhite)
                            .border(
                                1.dp,
                                if (isSelected) DeepCharcoal else BorderZinc,
                                RoundedCornerShape(20.dp)
                            )
                            .pressScaleClickable { selectedFilter = filter.key }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (filter.icon != null) {
                                Icon(
                                    imageVector = filter.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) PureWhite else DeepCharcoal,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                            Text(
                                text = filter.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PureWhite else DeepCharcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (displayedDeadlines.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = AccentEmerald,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No deadlines in this section",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap + below to add a reminder or switch filters.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedZinc
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedDeadlines, key = { it.id }) { deadline ->
                        DeadlineRowCard(
                            deadline = deadline,
                            onToggle = { onToggleDeadline(deadline) },
                            onDelete = { onDeleteDeadline(deadline.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddDeadlineDialog(
            onDismiss = { onShowAddDialogChange(false) },
            onConfirm = onAddDeadline
        )
    }
}

@Composable
fun DeadlineRowCard(
    deadline: DeadlineEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
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
        diffDays == 1 -> Triple("1 day left", SoftRose, AccentRose)
        diffDays <= 3 -> Triple("$diffDays days left", SoftRose, AccentRose)
        diffDays <= 7 -> Triple("$diffDays days left", SoftAmber, AccentAmber)
        else -> Triple("$diffDays days left", LightZinc, MutedZinc)
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        backgroundColor = if (deadline.isCompleted) PureWhite.copy(alpha = 0.75f) else PureWhite,
        shape = RoundedCornerShape(18.dp),
        elevation = if (deadline.isCompleted) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar Badge Block
            Box(
                modifier = Modifier
                    .size(52.dp)
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

            Spacer(modifier = Modifier.width(12.dp))

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
                            .padding(horizontal = 8.dp, vertical = 2.dp)
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = deadline.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = if (deadline.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    fontWeight = FontWeight.Bold,
                    color = if (deadline.isCompleted) MutedZinc else DeepCharcoal
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

            Spacer(modifier = Modifier.width(8.dp))

            // Completion Circle
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

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = MutedZinc,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeadlineDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, dueDateEpochMs: Long, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Visa") }
    var selectedPresetDays by remember { mutableStateOf(7L) }
    var customDaysString by remember { mutableStateOf("7") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf("Visa", "Finance", "Academic", "Housing", "Health", "General")
    var dropdownExpanded by remember { mutableStateOf(false) }

    val presets = listOf(
        3L to "+3 Days",
        7L to "+1 Week",
        14L to "+2 Weeks",
        30L to "+1 Month"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = AccentAmber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add New Deadline",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepCharcoal
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Deadline Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepCharcoal,
                            unfocusedBorderColor = BorderZinc
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, fontWeight = FontWeight.Medium) },
                                onClick = {
                                    category = cat
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Preset Due Date Chips
                Column {
                    Text(
                        text = "Due In",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedZinc,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presets.forEach { (days, label) ->
                            val isSelected = selectedPresetDays == days
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) DeepCharcoal else LightZinc)
                                    .pressScaleClickable {
                                        selectedPresetDays = days
                                        customDaysString = days.toString()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PureWhite else DeepCharcoal
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Instructions (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val days = customDaysString.toLongOrNull() ?: selectedPresetDays
                        val targetMs = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
                        onConfirm(title.trim(), category, targetMs, notes.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepCharcoal,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = title.isNotBlank()
            ) {
                Text("Save Deadline", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedZinc, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
