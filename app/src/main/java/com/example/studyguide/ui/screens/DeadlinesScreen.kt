package com.example.studyguide.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.GlassHeroCard
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
fun DeadlinesScreen(
    deadlines: List<DeadlineEntity>,
    showAddDialog: Boolean,
    onShowAddDialogChange: (Boolean) -> Unit,
    onToggleDeadline: (DeadlineEntity) -> Unit,
    onDeleteDeadline: (String) -> Unit,
    onAddDeadline: (title: String, category: String, dueDateEpochMs: Long, notes: String) -> Unit
) {
    val pendingDeadlines = deadlines.filter { !it.isCompleted }.sortedBy { it.dueDateEpochMs }
    val completedDeadlines = deadlines.filter { it.isCompleted }

    Scaffold(
        containerColor = PureWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onShowAddDialogChange(true) },
                containerColor = PureBlack,
                contentColor = PureWhite,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Deadline")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PureWhite)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Stats Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassHeroCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Pending",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${pendingDeadlines.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                GlassCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelMedium,
                            color = MutedZinc
                        )
                        Text(
                            text = "${completedDeadlines.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = DarkZinc,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (deadlines.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No deadlines added yet. Tap + to create one!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MutedZinc
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (pendingDeadlines.isNotEmpty()) {
                        item {
                            Text(
                                text = "Upcoming Deadlines",
                                style = MaterialTheme.typography.titleLarge,
                                color = DarkZinc,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(pendingDeadlines) { deadline ->
                            DeadlineRowCard(
                                deadline = deadline,
                                onToggle = { onToggleDeadline(deadline) },
                                onDelete = { onDeleteDeadline(deadline.id) }
                            )
                        }
                    }

                    if (completedDeadlines.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Completed",
                                style = MaterialTheme.typography.titleLarge,
                                color = MutedZinc,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(completedDeadlines) { deadline ->
                            DeadlineRowCard(
                                deadline = deadline,
                                onToggle = { onToggleDeadline(deadline) },
                                onDelete = { onDeleteDeadline(deadline.id) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
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
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dueDateString = dateFormat.format(Date(deadline.dueDateEpochMs))

    val now = System.currentTimeMillis()
    val diffDays = ((deadline.dueDateEpochMs - now) / (1000 * 60 * 60 * 24)).toInt()
    val daysTagText = when {
        deadline.isCompleted -> "Done"
        diffDays < 0 -> "Overdue"
        diffDays == 0 -> "Due Today"
        diffDays == 1 -> "1 day left"
        else -> "$diffDays days left"
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        backgroundColor = if (deadline.isCompleted) LightZinc else PureWhite,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = deadline.isCompleted,
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = deadline.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = if (deadline.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        fontWeight = FontWeight.Bold,
                        color = if (deadline.isCompleted) MutedZinc else DarkZinc,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (deadline.isCompleted) LightZinc else PureBlack)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = daysTagText,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (deadline.isCompleted) DarkZinc else PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Due: $dueDateString • ${deadline.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MutedZinc
                        )
                    }
                }

                if (deadline.notes.isNotBlank()) {
                    Text(
                        text = deadline.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedZinc
                    )
                }
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
    var daysFromNowString by remember { mutableStateOf("7") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf("Visa", "Finance", "Academic", "Housing", "Health", "General")
    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        title = {
            Text(
                "Add New Deadline",
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
                    label = { Text("Deadline Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PureBlack,
                        unfocusedBorderColor = BorderZinc
                    ),
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
                            focusedBorderColor = PureBlack,
                            unfocusedBorderColor = BorderZinc
                        ),
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

                OutlinedTextField(
                    value = daysFromNowString,
                    onValueChange = { daysFromNowString = it.filter { char -> char.isDigit() } },
                    label = { Text("Due in (Days from today)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PureBlack,
                        unfocusedBorderColor = BorderZinc
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PureBlack,
                        unfocusedBorderColor = BorderZinc
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val days = daysFromNowString.toLongOrNull() ?: 7
                        val targetMs = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
                        onConfirm(title.trim(), category, targetMs, notes.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PureBlack,
                    contentColor = PureWhite
                ),
                enabled = title.isNotBlank()
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedZinc)
            }
        }
    )
}
