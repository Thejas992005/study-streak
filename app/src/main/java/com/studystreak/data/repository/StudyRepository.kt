package com.studystreak.data.repository

import com.studystreak.data.dao.StudySessionDao
import com.studystreak.data.dao.SubjectDuration
import com.studystreak.data.models.StudySessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyRepository @Inject constructor(
    private val studySessionDao: StudySessionDao
) {
    fun getAllSessions(): Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()

    fun getSessionsForDate(dateMillis: Long): Flow<List<StudySessionEntity>> =
        studySessionDao.getSessionsForDate(dateMillis)

    fun getSessionsBetweenDates(startDate: Long, endDate: Long): Flow<List<StudySessionEntity>> =
        studySessionDao.getSessionsBetweenDates(startDate, endDate)

    suspend fun getSessionById(sessionId: Long): StudySessionEntity? =
        studySessionDao.getSessionById(sessionId)

    fun getTotalDurationForDate(dateMillis: Long): Flow<Long> =
        studySessionDao.getTotalDurationForDate(dateMillis)

    fun getTotalDurationBetweenDates(startDate: Long, endDate: Long): Flow<Long> =
        studySessionDao.getTotalDurationBetweenDates(startDate, endDate)

    suspend fun getSubjectDurationsBetweenDates(startDate: Long, endDate: Long): List<SubjectDuration> =
        studySessionDao.getSubjectDurationsBetweenDates(startDate, endDate)

    fun getTotalStudyTime(): Flow<Long> = studySessionDao.getTotalStudyTime()

    suspend fun insertSession(session: StudySessionEntity): Long =
        studySessionDao.insertSession(session)

    suspend fun updateSession(session: StudySessionEntity) =
        studySessionDao.updateSession(session)

    suspend fun deleteSession(session: StudySessionEntity) =
        studySessionDao.deleteSession(session)
}
