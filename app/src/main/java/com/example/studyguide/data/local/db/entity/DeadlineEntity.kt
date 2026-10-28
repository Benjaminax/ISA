package com.example.studyguide.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deadlines")
data class DeadlineEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val dueDateEpochMs: Long,
    val isCompleted: Boolean = false,
    val reminderScheduled: Boolean = false,
    val notes: String = ""
)

