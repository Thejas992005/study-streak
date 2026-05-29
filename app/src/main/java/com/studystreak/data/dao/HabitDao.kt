package com.studystreak.data.dao

import androidx.room.*
import com.studystreak.data.models.HabitCompletionEntity
import com.studystreak.data.models.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Long)

    // Completions
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY dateMillis DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE dateMillis = :dateMillis")
    fun getCompletionsForDate(dateMillis: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateMillis = :dateMillis LIMIT 1")
    suspend fun getCompletionForHabitOnDate(habitId: Long, dateMillis: Long): HabitCompletionEntity?

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateMillis BETWEEN :startDate AND :endDate ORDER BY dateMillis ASC")
    suspend fun getCompletionsBetweenDates(habitId: Long, startDate: Long, endDate: Long): List<HabitCompletionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Delete
    suspend fun deleteCompletion(completion: HabitCompletionEntity)

    @Query("SELECT COUNT(*) FROM habit_completions WHERE habitId = :habitId AND isCompleted = 1")
    suspend fun getCompletionCount(habitId: Long): Int

    @Query("SELECT COUNT(DISTINCT dateMillis) FROM habit_completions WHERE habitId = :habitId AND isCompleted = 1")
    suspend fun getCompletedDaysCount(habitId: Long): Int

    @Query("SELECT COUNT(DISTINCT habitId) FROM habit_completions WHERE dateMillis = :dateMillis AND isCompleted = 1")
    suspend fun getCompletedHabitsCountForDate(dateMillis: Long): Int
}
