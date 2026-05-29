package com.studystreak.data.repository

import com.studystreak.data.dao.AssignmentDao
import com.studystreak.data.models.AssignmentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentRepository @Inject constructor(
    private val assignmentDao: AssignmentDao
) {
    fun getAllAssignments(): Flow<List<AssignmentEntity>> = assignmentDao.getAllAssignments()

    fun getAssignmentsByStatus(status: String): Flow<List<AssignmentEntity>> =
        assignmentDao.getAssignmentsByStatus(status)

    fun getUpcomingAssignments(todayMillis: Long): Flow<List<AssignmentEntity>> =
        assignmentDao.getUpcomingAssignments(todayMillis)

    fun getOverdueAssignments(todayMillis: Long): Flow<List<AssignmentEntity>> =
        assignmentDao.getOverdueAssignments(todayMillis)

    fun getTodayAssignments(startOfDay: Long, endOfDay: Long): Flow<List<AssignmentEntity>> =
        assignmentDao.getTodayAssignments(startOfDay, endOfDay)

    suspend fun getAssignmentById(assignmentId: Long): AssignmentEntity? =
        assignmentDao.getAssignmentById(assignmentId)

    fun getPendingCount(): Flow<Int> = assignmentDao.getPendingCount()

    suspend fun insertAssignment(assignment: AssignmentEntity): Long =
        assignmentDao.insertAssignment(assignment)

    suspend fun updateAssignment(assignment: AssignmentEntity) =
        assignmentDao.updateAssignment(assignment)

    suspend fun deleteAssignment(assignment: AssignmentEntity) =
        assignmentDao.deleteAssignment(assignment)

    suspend fun deleteAssignmentById(assignmentId: Long) =
        assignmentDao.deleteAssignmentById(assignmentId)
}
