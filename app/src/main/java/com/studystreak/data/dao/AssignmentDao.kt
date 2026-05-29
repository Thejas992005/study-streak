package com.studystreak.data.dao

import androidx.room.*
import com.studystreak.data.models.AssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {

    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE status = :status ORDER BY dueDate ASC")
    fun getAssignmentsByStatus(status: String): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE status != 'Completed' AND dueDate >= :todayMillis ORDER BY dueDate ASC")
    fun getUpcomingAssignments(todayMillis: Long): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE status != 'Completed' AND dueDate < :todayMillis ORDER BY dueDate ASC")
    fun getOverdueAssignments(todayMillis: Long): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE status != 'Completed' AND dueDate BETWEEN :startOfDay AND :endOfDay ORDER BY dueDate ASC")
    fun getTodayAssignments(startOfDay: Long, endOfDay: Long): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE id = :assignmentId")
    suspend fun getAssignmentById(assignmentId: Long): AssignmentEntity?

    @Query("SELECT COUNT(*) FROM assignments WHERE status = 'Pending'")
    fun getPendingCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity): Long

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)

    @Query("DELETE FROM assignments WHERE id = :assignmentId")
    suspend fun deleteAssignmentById(assignmentId: Long)
}
