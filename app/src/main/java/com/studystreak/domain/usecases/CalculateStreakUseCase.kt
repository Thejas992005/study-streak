package com.studystreak.domain.usecases

import com.studystreak.data.repository.HabitRepository
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.DateUtils
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val userStatsRepository: UserStatsRepository
) {
    /**
     * Calculates the current streak for a specific habit by walking backward
     * from today through completion records.
     * Returns the streak count.
     */
    suspend fun forHabit(habitId: Long): Int {
        val today = DateUtils.getStartOfDay()
        var streak = 0
        var currentDay = today

        // Check up to 365 days back
        for (i in 0 until 365) {
            val completion = habitRepository.getCompletionForHabitOnDate(habitId, currentDay)
            if (completion != null && completion.isCompleted) {
                streak++
                currentDay -= 24 * 60 * 60 * 1000L // Go back one day
            } else if (i == 0) {
                // Today hasn't been completed yet — that's OK, check yesterday
                currentDay -= 24 * 60 * 60 * 1000L
                continue
            } else {
                break
            }
        }

        return streak
    }

    /**
     * Recalculates and updates the global user streak.
     * The global streak counts consecutive days where at least one habit was completed.
     */
    suspend fun updateGlobalStreak() {
        val today = DateUtils.getStartOfDay()
        var streak = 0
        var currentDay = today

        for (i in 0 until 365) {
            val completedCount = habitRepository.getCompletedHabitsCountForDate(currentDay)
            if (completedCount > 0) {
                streak++
                currentDay -= 24 * 60 * 60 * 1000L
            } else if (i == 0) {
                // Today not yet done — skip and check from yesterday
                currentDay -= 24 * 60 * 60 * 1000L
                continue
            } else {
                break
            }
        }

        userStatsRepository.updateCurrentStreak(streak)
        val stats = userStatsRepository.getUserStatsOnce()
        if (stats != null && streak > stats.bestStreak) {
            userStatsRepository.updateBestStreak(streak)
        }
        userStatsRepository.updateLastActiveDate(today)
    }
}
