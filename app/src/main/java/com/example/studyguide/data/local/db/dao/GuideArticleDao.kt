package com.example.studyguide.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studyguide.data.local.db.entity.GuideArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GuideArticleDao {

    @Query("SELECT * FROM guide_articles ORDER BY title ASC")
    fun getAllArticles(): Flow<List<GuideArticleEntity>>

    @Query("SELECT * FROM guide_articles WHERE categoryId = :categoryId ORDER BY title ASC")
    fun getArticlesByCategory(categoryId: String): Flow<List<GuideArticleEntity>>

    @Query("SELECT * FROM guide_articles WHERE isBookmarked = 1 ORDER BY title ASC")
    fun getBookmarkedArticles(): Flow<List<GuideArticleEntity>>

    @Query("SELECT * FROM guide_articles WHERE id = :id")
    fun getArticleById(id: String): Flow<GuideArticleEntity?>

    @Query("SELECT * FROM guide_articles WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%'")
    fun searchArticles(query: String): Flow<List<GuideArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: GuideArticleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<GuideArticleEntity>)

    @Update
    suspend fun updateArticle(article: GuideArticleEntity)

    @Query("UPDATE guide_articles SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: String, isBookmarked: Boolean)

    @Query("DELETE FROM guide_articles WHERE id = :id")
    suspend fun deleteArticleById(id: String)

    @Query("SELECT COUNT(*) FROM guide_articles")
    suspend fun getArticleCount(): Int
}
