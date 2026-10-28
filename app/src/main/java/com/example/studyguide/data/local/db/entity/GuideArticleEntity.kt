package com.example.studyguide.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guide_articles")
data class GuideArticleEntity(
    @PrimaryKey
    val id: String,
    val countryCode: String,
    val categoryId: String,
    val title: String,
    val summary: String,
    val contentMarkdown: String,
    val imageUrl: String = "",
    val stepsJson: String = "[]",
    val officialLinksJson: String = "[]",
    val isBookmarked: Boolean = false,
    val readTimeMinutes: Int = 3,
    val lastUpdated: Long = System.currentTimeMillis()
)
