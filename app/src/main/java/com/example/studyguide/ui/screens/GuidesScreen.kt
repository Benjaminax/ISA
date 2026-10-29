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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.components.GlassCard
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentIndigo
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureWhite

data class GuideCategoryFilter(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun GuidesScreen(
    articles: List<GuideArticleEntity>,
    selectedCategory: String,
    searchQuery: String,
    showAddDialog: Boolean,
    onShowAddDialogChange: (Boolean) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSelectArticle: (GuideArticleEntity) -> Unit,
    onAddArticle: (title: String, categoryId: String, summary: String, contentMarkdown: String, imageUrl: String) -> Unit
) {
    val categories = listOf(
        GuideCategoryFilter("All", "All", Icons.Default.Apps),
        GuideCategoryFilter("Visa", "Visa", Icons.Default.AssignmentInd),
        GuideCategoryFilter("Housing", "Housing", Icons.Default.Apartment),
        GuideCategoryFilter("Banking", "Banking", Icons.Default.AccountBalanceWallet),
        GuideCategoryFilter("Health", "Health", Icons.Default.LocalHospital),
        GuideCategoryFilter("Academic", "Academics", Icons.Default.School)
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
                    contentDescription = "Add Guide",
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

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(18.dp)),
                placeholder = {
                    Text(
                        "Search visa, housing, campus tips...",
                        color = MutedZinc,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DeepCharcoal,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search",
                                tint = MutedZinc,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepCharcoal,
                    unfocusedBorderColor = BorderZinc,
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite
                ),
                shape = RoundedCornerShape(18.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat.key.equals(selectedCategory, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) DeepCharcoal else PureWhite)
                            .border(
                                1.dp,
                                if (isSelected) DeepCharcoal else BorderZinc,
                                RoundedCornerShape(20.dp)
                            )
                            .pressScaleClickable { onCategorySelected(cat.key) }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = null,
                                tint = if (isSelected) PureWhite else DeepCharcoal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PureWhite else DeepCharcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Results count row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${articles.size} guides available",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedZinc,
                    fontWeight = FontWeight.SemiBold
                )

                if (searchQuery.isNotBlank() || selectedCategory != "All") {
                    Text(
                        text = "Reset filter",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentIndigo,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.pressScaleClickable {
                            onSearchQueryChanged("")
                            onCategorySelected("All")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Articles List
            if (articles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MutedZinc,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No matching guides found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoal
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try adjusting your search terms or explore a different category.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedZinc,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    onSearchQueryChanged("")
                                    onCategorySelected("All")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DeepCharcoal,
                                    contentColor = PureWhite
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Show All Guides", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    items(articles) { article ->
                        MasterArticleCard(
                            article = article,
                            onClick = { onSelectArticle(article) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddGuideDialog(
            onDismiss = { onShowAddDialogChange(false) },
            onConfirm = onAddArticle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGuideDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, categoryId: String, summary: String, contentMarkdown: String, imageUrl: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("Visa") }
    var summary by remember { mutableStateOf("") }
    var contentMarkdown by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=800&auto=format&fit=crop") }

    val categories = listOf("Visa", "Housing", "Banking", "Health", "Academic")
    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = null,
                    tint = AccentIndigo,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Create Guide",
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
                    label = { Text("Guide Title") },
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
                        value = categoryId,
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
                                    categoryId = cat
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Short Summary / Takeaway") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contentMarkdown,
                    onValueChange = { contentMarkdown = it },
                    label = { Text("Full Content / Instructions") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepCharcoal,
                        unfocusedBorderColor = BorderZinc
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), categoryId, summary.trim(), contentMarkdown.trim(), imageUrl.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepCharcoal,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = title.isNotBlank()
            ) {
                Text("Publish Guide", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedZinc, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
