package com.example.studyguide.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.AccentRose
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftEmerald
import com.example.studyguide.ui.theme.SoftRose

data class ChecklistStageInfo(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun ChecklistScreen(
    items: List<ChecklistItemEntity>,
    selectedStage: String,
    showAddDialog: Boolean,
    onShowAddDialogChange: (Boolean) -> Unit,
    onStageSelected: (String) -> Unit,
    onToggleItem: (ChecklistItemEntity) -> Unit,
    onDeleteItem: (String) -> Unit,
    onAddItem: (title: String, description: String, stage: String, isMandatory: Boolean) -> Unit
) {
    val stages = listOf(
        ChecklistStageInfo("pre_arrival", "Pre-Arrival", Icons.Default.FlightLand),
        ChecklistStageInfo("first_week", "First Week", Icons.Default.Apartment),
        ChecklistStageInfo("first_month", "First Month", Icons.Default.School)
    )

    val filteredItems = items.filter { it.stage == selectedStage }
    val totalCount = filteredItems.size
    val completedCount = filteredItems.count { it.isCompleted }
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animatedStageProgress"
    )

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
                    contentDescription = "Add Task",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OffWhite)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Master Segmented Stage Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(LightZinc)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stages.forEach { stage ->
                    val isSelected = stage.key == selectedStage
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(if (isSelected) 3.dp else 0.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PureWhite else Color.Transparent)
                            .pressScaleClickable { onStageSelected(stage.key) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = stage.icon,
                                contentDescription = null,
                                tint = if (isSelected) DeepCharcoal else MutedZinc,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stage.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DeepCharcoal else MutedZinc
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stage Progress Overview Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Stage Completion",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepCharcoal,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$completedCount of $totalCount steps completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedZinc
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoftEmerald)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${(progress * 100).toInt()}% Done",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentEmerald,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
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

            // Celebratory Banner if 100% complete
            if (totalCount > 0 && completedCount == totalCount) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    backgroundColor = SoftEmerald,
                    borderColor = AccentEmerald.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentEmerald,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Stage Mastered!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AccentEmerald
                            )
                            Text(
                                text = "You've completed all tasks for this stage. Great job!",
                                style = MaterialTheme.typography.bodySmall,
                                color = DeepCharcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Task List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    MasterChecklistItemCard(
                        item = item,
                        onToggle = { onToggleItem(item) },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddChecklistDialog(
            currentStage = selectedStage,
            onDismiss = { onShowAddDialogChange(false) },
            onConfirm = onAddItem
        )
    }
}

@Composable
fun MasterChecklistItemCard(
    item: ChecklistItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        backgroundColor = if (item.isCompleted) PureWhite.copy(alpha = 0.7f) else PureWhite,
        shape = RoundedCornerShape(18.dp),
        elevation = if (item.isCompleted) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Custom Animated Checkbox Circle
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (item.isCompleted) AccentEmerald else LightZinc)
                    .border(
                        1.5.dp,
                        if (item.isCompleted) AccentEmerald else BorderZinc,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (item.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = PureWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        fontWeight = FontWeight.Bold,
                        color = if (item.isCompleted) MutedZinc else DeepCharcoal,
                        modifier = Modifier.weight(1f)
                    )

                    if (item.isMandatory) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftRose)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "REQUIRED",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentRose,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Task",
                    tint = MutedZinc,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun AddChecklistDialog(
    currentStage: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, stage: String, isMandatory: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isMandatory by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlaylistAdd,
                    contentDescription = null,
                    tint = DeepCharcoal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add Checklist Task",
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
                    label = { Text("Task Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Details (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightZinc)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Mandatory / Legal Requirement",
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepCharcoal,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = isMandatory,
                        onCheckedChange = { isMandatory = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PureWhite,
                            checkedTrackColor = AccentRose
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), description.trim(), currentStage, isMandatory)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepCharcoal,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = title.isNotBlank()
            ) {
                Text("Save Task", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedZinc, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
