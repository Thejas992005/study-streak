package com.studystreak.data.dao

import androidx.room.*
import com.studystreak.data.models.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Query("SELECT * FROM exams ORDER BY date ASC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE date >= :todayMillis ORDER BY date ASC")
    fun getUpcomingExams(todayMillis: Long): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE date >= :todayMillis ORDER BY date ASC LIMIT :limit")
    fun getNextExams(todayMillis: Long, limit: Int = 3): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE id = :examId")
    suspend fun getExamById(examId: Long): ExamEntity?

    @Query("SELECT COUNT(*) FROM exams WHERE date >= :todayMillis")
    fun getUpcomingExamCount(todayMillis: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity): Long

    @Update
    suspend fun updateExam(exam: ExamEntity)

    @Delete
    suspend fun deleteExam(exam: ExamEntity)

    @Query("DELETE FROM exams WHERE id = :examId")
    suspend fun deleteExamById(examId: Long)
}
