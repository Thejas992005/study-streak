package com.studystreak.data.repository

import com.studystreak.data.dao.HabitDao
import com.studystreak.data.models.HabitCompletionEntity
import com.studystreak.data.models.HabitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    fun getActiveHabits(): Flow<List<HabitEntity>> = habitDao.getActiveHabits()
    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()
    suspend fun getHabitById(habitId: Long): HabitEntity? = habitDao.getHabitById(habitId)
    suspend fun insertHabit(habit: HabitEntity): Long = habitDao.insertHabit(habit)
    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)
    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)
    suspend fun deleteHabitById(habitId: Long) = habitDao.deleteHabitById(habitId)

    // Completions
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>> =
        habitDao.getCompletionsForHabit(habitId)

    fun getCompletionsForDate(dateMillis: Long): Flow<List<HabitCompletionEntity>> =
        habitDao.getCompletionsForDate(dateMillis)

    suspend fun getCompletionForHabitOnDate(habitId: Long, dateMillis: Long): HabitCompletionEntity? =
        habitDao.getCompletionForHabitOnDate(habitId, dateMillis)

    suspend fun getCompletionsBetweenDates(habitId: Long, startDate: Long, endDate: Long): List<HabitCompletionEntity> =
        habitDao.getCompletionsBetweenDates(habitId, startDate, endDate)

    suspend fun insertCompletion(completion: HabitCompletionEntity) =
        habitDao.insertCompletion(completion)

    suspend fun deleteCompletion(completion: HabitCompletionEntity) =
        habitDao.deleteCompletion(completion)

    suspend fun getCompletionCount(habitId: Long): Int = habitDao.getCompletionCount(habitId)
    suspend fun getCompletedDaysCount(habitId: Long): Int = habitDao.getCompletedDaysCount(habitId)
    suspend fun getCompletedHabitsCountForDate(dateMillis: Long): Int =
        habitDao.getCompletedHabitsCountForDate(dateMillis)
}
