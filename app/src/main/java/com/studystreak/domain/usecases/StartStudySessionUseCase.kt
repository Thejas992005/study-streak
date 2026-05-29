package com.studystreak.domain.usecases

import com.studystreak.data.models.StudySessionEntity
import com.studystreak.data.repository.StudyRepository
import com.studystreak.utils.DateUtils
import javax.inject.Inject

class StartStudySessionUseCase @Inject constructor(
    private val studyRepository: StudyRepository
) {
    /**
     * Creates a new study session and returns its ID.
     */
    suspend operator fun invoke(subject: String): Long {
        val now = System.currentTimeMillis()
        val session = StudySessionEntity(
            subject = subject,
            startTime = now,
            dateMillis = DateUtils.getStartOfDay(now)
        )
        return studyRepository.insertSession(session)
    }
}
