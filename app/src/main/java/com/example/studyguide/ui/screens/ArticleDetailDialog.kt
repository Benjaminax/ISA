package com.example.studyguide.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.components.CategoryBadge
import com.example.studyguide.ui.components.CategoryThemes
import com.example.studyguide.ui.components.pressScaleClickable
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.OffWhite
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite

@Composable
fun ArticleDetailDialog(
    article: GuideArticleEntity,
    onDismiss: () -> Unit,
    onToggleBookmark: (GuideArticleEntity) -> Unit
) {
    val categoryStyle = CategoryThemes.getStyle(article.categoryId)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = OffWhite
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Scrollable Article Body
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Image / Hero Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
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

                        // Gradient Scrim for readable header
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            DeepCharcoal.copy(alpha = 0.6f),
                                            Color.Transparent,
                                            OffWhite
                                        )
                                    )
                                )
                        )

                        // Central category icon if image is absent or loading
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(PureWhite.copy(alpha = 0.2f))
                                    .border(1.5.dp, PureWhite.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryStyle.icon,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    // Article Content Body
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Category & Reading Time Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryBadge(category = article.categoryId)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = MutedZinc,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${article.readTimeMinutes} min read",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedZinc,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Article Title
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = DeepCharcoal,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Curated Executive Summary Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderZinc, RoundedCornerShape(18.dp)),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(categoryStyle.softBackground),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = categoryStyle.primaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Executive Takeaway",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = categoryStyle.primaryColor
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = article.summary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DeepCharcoal,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Markdown Section Renderer
                        ArticleMarkdownContent(content = article.contentMarkdown)

                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }

                // Floating Top Bar with Frosted Glass Look
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(8.dp, CircleShape, ambientColor = DeepCharcoal.copy(alpha = 0.2f))
                            .clip(CircleShape)
                            .background(PureWhite)
                            .pressScaleClickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepCharcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .shadow(8.dp, CircleShape, ambientColor = DeepCharcoal.copy(alpha = 0.2f))
                                .clip(CircleShape)
                                .background(PureWhite)
                                .pressScaleClickable { onToggleBookmark(article) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (article.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (article.isBookmarked) AccentEmerald else DeepCharcoal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Sticky Bottom Action Dock
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, PureWhite.copy(alpha = 0.95f), PureWhite)
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Button(
                        onClick = { onToggleBookmark(article) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(6.dp, RoundedCornerShape(20.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (article.isBookmarked) DeepCharcoal else PureBlack,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = if (article.isBookmarked) AccentEmerald else PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (article.isBookmarked) "Saved to Bookmarks" else "Save for Offline Reading",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleMarkdownContent(content: String) {
    val lines = content.lines()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        lines.forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.startsWith("# ") -> {
                    Text(
                        text = trimmed.removePrefix("# ").trim(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoal
                    )
                }
                trimmed.startsWith("### ") -> {
                    Text(
                        text = trimmed.removePrefix("### ").trim(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoal
                    )
                }
                trimmed.startsWith("* ") || trimmed.startsWith("- ") -> {
                    val itemText = trimmed.removePrefix("* ").removePrefix("- ").trim()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 9.dp, end = 10.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentEmerald)
                        )
                        Text(
                            text = itemText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DeepCharcoal,
                            lineHeight = 24.sp
                        )
                    }
                }
                trimmed.firstOrNull()?.isDigit() == true && trimmed.contains(". ") -> {
                    val parts = trimmed.split(". ", limit = 2)
                    val num = parts.getOrNull(0) ?: ""
                    val text = parts.getOrNull(1) ?: ""
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(1.dp, BorderZinc, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(DeepCharcoal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = num,
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DeepCharcoal,
                            lineHeight = 22.sp
                        )
                    }
                }
                trimmed.isNotEmpty() -> {
                    Text(
                        text = trimmed,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepCharcoal,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}
