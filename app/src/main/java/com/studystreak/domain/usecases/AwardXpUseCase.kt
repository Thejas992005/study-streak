package com.studystreak.domain.usecases

import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.Constants
import javax.inject.Inject

class AwardXpUseCase @Inject constructor(
    private val userStatsRepository: UserStatsRepository
) {
    /**
     * Awards XP to the user and checks for level-up.
     * Returns the new level if leveled up, null otherwise.
     */
    suspend operator fun invoke(xpAmount: Int): Int? {
        userStatsRepository.addXp(xpAmount)
        val stats = userStatsRepository.getUserStatsOnce() ?: return null
        val newLevel = Constants.getLevelForXp(stats.totalXp)
        if (newLevel > stats.currentLevel) {
            userStatsRepository.updateLevel(newLevel)
            return newLevel
        }
        return null
    }
}
