package com.studystreak.domain.usecases

import com.studystreak.data.repository.AssignmentRepository
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.Constants
import javax.inject.Inject

class CompleteAssignmentUseCase @Inject constructor(
    private val assignmentRepository: AssignmentRepository,
    private val userStatsRepository: UserStatsRepository,
    private val awardXpUseCase: AwardXpUseCase
) {
    /**
     * Marks an assignment as completed and awards XP.
     */
    suspend operator fun invoke(assignmentId: Long) {
        val assignment = assignmentRepository.getAssignmentById(assignmentId) ?: return
        val updated = assignment.copy(status = "Completed")
        assignmentRepository.updateAssignment(updated)
        userStatsRepository.incrementCompletedAssignments()
        awardXpUseCase(Constants.XP_COMPLETE_ASSIGNMENT)
    }
}
