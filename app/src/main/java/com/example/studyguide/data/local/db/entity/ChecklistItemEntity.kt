package com.example.studyguide.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey
    val id: String,
    val countryCode: String,
    val stage: String, // "pre_arrival", "first_week", "first_month"
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isMandatory: Boolean = true,
    val orderIndex: Int = 0
)

