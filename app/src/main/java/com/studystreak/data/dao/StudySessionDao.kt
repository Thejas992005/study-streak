package com.studystreak.data.dao

import androidx.room.*
import com.studystreak.data.models.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Query("SELECT * FROM study_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE dateMillis = :dateMillis ORDER BY startTime DESC")
    fun getSessionsForDate(dateMillis: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE dateMillis BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    fun getSessionsBetweenDates(startDate: Long, endDate: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): StudySessionEntity?

    @Query("SELECT COALESCE(SUM(durationMs), 0) FROM study_sessions WHERE dateMillis = :dateMillis")
    fun getTotalDurationForDate(dateMillis: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(durationMs), 0) FROM study_sessions WHERE dateMillis BETWEEN :startDate AND :endDate")
    fun getTotalDurationBetweenDates(startDate: Long, endDate: Long): Flow<Long>

    @Query("SELECT subject, SUM(durationMs) as totalDuration FROM study_sessions WHERE dateMillis BETWEEN :startDate AND :endDate GROUP BY subject ORDER BY totalDuration DESC")
    suspend fun getSubjectDurationsBetweenDates(startDate: Long, endDate: Long): List<SubjectDuration>

    @Query("SELECT COALESCE(SUM(durationMs), 0) FROM study_sessions")
    fun getTotalStudyTime(): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Update
    suspend fun updateSession(session: StudySessionEntity)

    @Delete
    suspend fun deleteSession(session: StudySessionEntity)
}

data class SubjectDuration(
    val subject: String,
    val totalDuration: Long
)
