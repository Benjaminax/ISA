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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DarkZinc
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite

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
        "pre_arrival" to "Pre-Arrival",
        "first_week" to "First Week",
        "first_month" to "First Month"
    )

    val currentSelectedStageIndex = stages.indexOfFirst { it.first == selectedStage }.let { if (it < 0) 0 else it }

    val filteredItems = items.filter { it.stage == selectedStage }
    val totalCount = filteredItems.size
    val completedCount = filteredItems.count { it.isCompleted }
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    Scaffold(
        containerColor = PureWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onShowAddDialogChange(true) },
                containerColor = PureBlack,
                contentColor = PureWhite,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PureWhite)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Clean Monochrome Tab Row
            TabRow(
                selectedTabIndex = currentSelectedStageIndex,
                containerColor = PureWhite,
                contentColor = PureBlack,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[currentSelectedStageIndex]),
                        color = PureBlack,
                        height = 2.5.dp
                    )
                }
            ) {
                stages.forEachIndexed { index, (stageKey, stageLabel) ->
                    Tab(
                        selected = currentSelectedStageIndex == index,
                        onClick = { onStageSelected(stageKey) },
                        text = {
                            Text(
                                text = stageLabel,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (currentSelectedStageIndex == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (currentSelectedStageIndex == index) PureBlack else MutedZinc
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stage Readiness",
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkZinc,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$completedCount of $totalCount completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PureBlack,
                        trackColor = LightZinc
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Items list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredItems) { item ->
                    ChecklistItemCard(
                        item = item,
                        onToggle = { onToggleItem(item) },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
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
fun ChecklistItemCard(
    item: ChecklistItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        backgroundColor = if (item.isCompleted) LightZinc else PureWhite,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = PureBlack,
                    uncheckedColor = MutedZinc,
                    checkmarkColor = PureWhite
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

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
                        color = if (item.isCompleted) MutedZinc else DarkZinc,
                        modifier = Modifier.weight(1f)
                    )

                    if (item.isMandatory) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkZinc)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Required",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.height(24.dp).width(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = MutedZinc
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
        title = {
            Text(
                "Add Checklist Task",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkZinc
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PureBlack,
                        unfocusedBorderColor = BorderZinc
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PureBlack,
                        unfocusedBorderColor = BorderZinc
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isMandatory,
                        onCheckedChange = { isMandatory = it },
                        colors = CheckboxDefaults.colors(checkedColor = PureBlack)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mandatory / Required Task", style = MaterialTheme.typography.bodyMedium, color = DarkZinc)
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
                    containerColor = PureBlack,
                    contentColor = PureWhite
                ),
                enabled = title.isNotBlank()
            ) {
                Text("Save Task", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedZinc)
            }
        }
    )
}
