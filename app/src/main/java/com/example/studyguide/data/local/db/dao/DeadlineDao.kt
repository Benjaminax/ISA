package com.example.studyguide.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studyguide.data.local.db.entity.DeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {

    @Query("SELECT * FROM deadlines ORDER BY dueDateEpochMs ASC")
    fun getAllDeadlines(): Flow<List<DeadlineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadline(deadline: DeadlineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadlines(deadlines: List<DeadlineEntity>)

    @Update
    suspend fun updateDeadline(deadline: DeadlineEntity)

    @Query("UPDATE deadlines SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, isCompleted: Boolean)

    @Query("DELETE FROM deadlines WHERE id = :id")
    suspend fun deleteDeadlineById(id: String)

    @Query("SELECT COUNT(*) FROM deadlines")
    suspend fun getDeadlineCount(): Int
}
