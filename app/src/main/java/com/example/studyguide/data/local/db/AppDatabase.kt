package com.example.studyguide.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studyguide.data.local.db.converter.AppTypeConverters
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity

@Database(
    entities = [
        GuideArticleEntity::class,
        ChecklistItemEntity::class,
        DeadlineEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    // DAOs will be added and expanded in step 5
}

