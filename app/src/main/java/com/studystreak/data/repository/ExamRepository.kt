package com.studystreak.data.repository

import com.studystreak.data.dao.ExamDao
import com.studystreak.data.models.ExamEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepository @Inject constructor(
    private val examDao: ExamDao
) {
    fun getAllExams(): Flow<List<ExamEntity>> = examDao.getAllExams()

    fun getUpcomingExams(todayMillis: Long): Flow<List<ExamEntity>> =
        examDao.getUpcomingExams(todayMillis)

    fun getNextExams(todayMillis: Long, limit: Int = 3): Flow<List<ExamEntity>> =
        examDao.getNextExams(todayMillis, limit)

    suspend fun getExamById(examId: Long): ExamEntity? = examDao.getExamById(examId)

    fun getUpcomingExamCount(todayMillis: Long): Flow<Int> = examDao.getUpcomingExamCount(todayMillis)

    suspend fun insertExam(exam: ExamEntity): Long = examDao.insertExam(exam)

    suspend fun updateExam(exam: ExamEntity) = examDao.updateExam(exam)

    suspend fun deleteExam(exam: ExamEntity) = examDao.deleteExam(exam)

    suspend fun deleteExamById(examId: Long) = examDao.deleteExamById(examId)
}
