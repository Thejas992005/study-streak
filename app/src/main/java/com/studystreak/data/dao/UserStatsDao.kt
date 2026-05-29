package com.studystreak.data.dao

import androidx.room.*
import com.studystreak.data.models.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stats: UserStatsEntity)

    @Query("UPDATE user_stats SET totalXp = totalXp + :xp WHERE id = 1")
    suspend fun addXp(xp: Int)

    @Query("UPDATE user_stats SET currentLevel = :level WHERE id = 1")
    suspend fun updateLevel(level: Int)

    @Query("UPDATE user_stats SET currentStreak = :streak WHERE id = 1")
    suspend fun updateCurrentStreak(streak: Int)

    @Query("UPDATE user_stats SET bestStreak = :streak WHERE id = 1")
    suspend fun updateBestStreak(streak: Int)

    @Query("UPDATE user_stats SET totalStudyTimeMs = totalStudyTimeMs + :durationMs WHERE id = 1")
    suspend fun addStudyTime(durationMs: Long)

    @Query("UPDATE user_stats SET completedHabitsCount = completedHabitsCount + 1 WHERE id = 1")
    suspend fun incrementCompletedHabits()

    @Query("UPDATE user_stats SET completedAssignmentsCount = completedAssignmentsCount + 1 WHERE id = 1")
    suspend fun incrementCompletedAssignments()

    @Query("UPDATE user_stats SET lastActiveDate = :dateMillis WHERE id = 1")
    suspend fun updateLastActiveDate(dateMillis: Long)

    @Query("UPDATE user_stats SET userName = :name WHERE id = 1")
    suspend fun updateUserName(name: String)

    @Query("UPDATE user_stats SET isDarkMode = :isDark WHERE id = 1")
    suspend fun updateDarkMode(isDark: Boolean)

    @Query("UPDATE user_stats SET notificationsEnabled = :enabled WHERE id = 1")
    suspend fun updateNotifications(enabled: Boolean)

    @Query("UPDATE user_stats SET themeColor = :color WHERE id = 1")
    suspend fun updateThemeColor(color: String)

    @Query("DELETE FROM user_stats")
    suspend fun resetStats()
}
