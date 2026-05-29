package com.studystreak.domain.usecases

import com.studystreak.data.models.HabitCompletionEntity
import com.studystreak.data.repository.HabitRepository
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.Constants
import com.studystreak.utils.DateUtils
import javax.inject.Inject

class CompleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val userStatsRepository: UserStatsRepository,
    private val awardXpUseCase: AwardXpUseCase,
    private val calculateStreakUseCase: CalculateStreakUseCase
) {
    /**
     * Marks a habit as completed for today.
     * Awards XP, updates streak, and checks for streak bonuses.
     * Returns true if already completed today (toggle off).
     */
    suspend operator fun invoke(habitId: Long): Boolean {
        val today = DateUtils.getStartOfDay()
        val existing = habitRepository.getCompletionForHabitOnDate(habitId, today)

        if (existing != null) {
            // Toggle off — remove completion
            habitRepository.deleteCompletion(existing)
            calculateStreakUseCase.updateGlobalStreak()
            return true
        }

        // Mark complete
        val completion = HabitCompletionEntity(
            habitId = habitId,
            dateMillis = today
        )
        habitRepository.insertCompletion(completion)
        userStatsRepository.incrementCompletedHabits()

        // Award XP
        awardXpUseCase(Constants.XP_COMPLETE_HABIT)

        // Calculate and update streak
        val streak = calculateStreakUseCase.forHabit(habitId)
        calculateStreakUseCase.updateGlobalStreak()

        // Streak bonuses
        if (streak == 7) {
            awardXpUseCase(Constants.XP_STREAK_7_DAYS)
        }
        if (streak == 30) {
            awardXpUseCase(Constants.XP_STREAK_30_DAYS)
        }

        return false
    }
}
