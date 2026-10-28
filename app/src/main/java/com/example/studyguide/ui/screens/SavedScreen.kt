package com.example.studyguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.PureWhite

@Composable
fun SavedScreen(
    bookmarkedArticles: List<GuideArticleEntity>,
    onSelectArticle: (GuideArticleEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (bookmarkedArticles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "No saved articles yet. Tap the star icon on any guide to save it for offline reading!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedZinc
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(bookmarkedArticles) { article ->
                    ArticleItemCard(article = article, onClick = { onSelectArticle(article) })
                }
            }
        }
    }
}
