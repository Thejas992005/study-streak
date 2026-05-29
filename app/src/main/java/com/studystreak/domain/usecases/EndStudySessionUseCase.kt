package com.studystreak.domain.usecases

import com.studystreak.data.repository.StudyRepository
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.Constants
import javax.inject.Inject

class EndStudySessionUseCase @Inject constructor(
    private val studyRepository: StudyRepository,
    private val userStatsRepository: UserStatsRepository,
    private val awardXpUseCase: AwardXpUseCase
) {
    /**
     * Ends a study session: updates duration, awards XP proportional to time studied.
     */
    suspend operator fun invoke(sessionId: Long, durationMs: Long) {
        val session = studyRepository.getSessionById(sessionId) ?: return
        val updated = session.copy(
            durationMs = durationMs,
            endTime = System.currentTimeMillis()
        )
        studyRepository.updateSession(updated)
        userStatsRepository.addStudyTime(durationMs)

        // Award XP: 15 XP per full hour
        val hours = durationMs / (60 * 60 * 1000.0)
        val xp = (hours * Constants.XP_STUDY_PER_HOUR).toInt()
        if (xp > 0) {
            awardXpUseCase(xp)
        }
    }
}
