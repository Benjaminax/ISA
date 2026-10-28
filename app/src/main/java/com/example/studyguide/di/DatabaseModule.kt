package com.example.studyguide.di

import android.content.Context
import androidx.room.Room
import com.example.studyguide.data.local.db.AppDatabase
import com.example.studyguide.data.local.db.dao.ChecklistItemDao
import com.example.studyguide.data.local.db.dao.DeadlineDao
import com.example.studyguide.data.local.db.dao.GuideArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "study_guide_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGuideArticleDao(database: AppDatabase): GuideArticleDao {
        return database.guideArticleDao()
    }

    @Provides
    fun provideChecklistItemDao(database: AppDatabase): ChecklistItemDao {
        return database.checklistItemDao()
    }

    @Provides
    fun provideDeadlineDao(database: AppDatabase): DeadlineDao {
        return database.deadlineDao()
    }
}
