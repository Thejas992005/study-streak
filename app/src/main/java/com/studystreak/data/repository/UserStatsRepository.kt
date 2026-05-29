package com.studystreak.data.repository

import com.studystreak.data.dao.UserStatsDao
import com.studystreak.data.models.UserStatsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStatsRepository @Inject constructor(
    private val userStatsDao: UserStatsDao
) {
    fun getUserStats(): Flow<UserStatsEntity?> = userStatsDao.getUserStats()
    suspend fun getUserStatsOnce(): UserStatsEntity? = userStatsDao.getUserStatsOnce()
    suspend fun insertOrUpdate(stats: UserStatsEntity) = userStatsDao.insertOrUpdate(stats)
    suspend fun addXp(xp: Int) = userStatsDao.addXp(xp)
    suspend fun updateLevel(level: Int) = userStatsDao.updateLevel(level)
    suspend fun updateCurrentStreak(streak: Int) = userStatsDao.updateCurrentStreak(streak)
    suspend fun updateBestStreak(streak: Int) = userStatsDao.updateBestStreak(streak)
    suspend fun addStudyTime(durationMs: Long) = userStatsDao.addStudyTime(durationMs)
    suspend fun incrementCompletedHabits() = userStatsDao.incrementCompletedHabits()
    suspend fun incrementCompletedAssignments() = userStatsDao.incrementCompletedAssignments()
    suspend fun updateLastActiveDate(dateMillis: Long) = userStatsDao.updateLastActiveDate(dateMillis)
    suspend fun updateUserName(name: String) = userStatsDao.updateUserName(name)
    suspend fun updateDarkMode(isDark: Boolean) = userStatsDao.updateDarkMode(isDark)
    suspend fun updateNotifications(enabled: Boolean) = userStatsDao.updateNotifications(enabled)
    suspend fun updateThemeColor(color: String) = userStatsDao.updateThemeColor(color)
    suspend fun resetStats() = userStatsDao.resetStats()

    /** Ensures a row exists in user_stats. Called on app startup. */
    suspend fun ensureInitialized() {
        if (getUserStatsOnce() == null) {
            insertOrUpdate(UserStatsEntity())
        }
    }
}
