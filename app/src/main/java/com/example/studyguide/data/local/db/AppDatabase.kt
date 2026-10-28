package com.example.studyguide.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studyguide.data.local.db.converter.AppTypeConverters
import com.example.studyguide.data.local.db.dao.ChecklistItemDao
import com.example.studyguide.data.local.db.dao.DeadlineDao
import com.example.studyguide.data.local.db.dao.GuideArticleDao
import com.example.studyguide.data.local.db.entity.ChecklistItemEntity
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import com.example.studyguide.data.local.db.entity.GuideArticleEntity

@Database(
    entities = [
        GuideArticleEntity::class,
        ChecklistItemEntity::class,
        DeadlineEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun guideArticleDao(): GuideArticleDao
    abstract fun checklistItemDao(): ChecklistItemDao
    abstract fun deadlineDao(): DeadlineDao
}
