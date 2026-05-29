package com.studystreak.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studystreak.data.models.AssignmentEntity
import com.studystreak.data.models.ExamEntity
import com.studystreak.data.repository.AssignmentRepository
import com.studystreak.data.repository.ExamRepository
import com.studystreak.domain.usecases.CompleteAssignmentUseCase
import com.studystreak.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val todayAssignments: List<AssignmentEntity> = emptyList(),
    val upcomingAssignments: List<AssignmentEntity> = emptyList(),
    val completedAssignments: List<AssignmentEntity> = emptyList(),
    val overdueAssignments: List<AssignmentEntity> = emptyList(),
    val upcomingExams: List<ExamEntity> = emptyList()
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository,
    private val examRepository: ExamRepository,
    private val completeAssignmentUseCase: CompleteAssignmentUseCase
) : ViewModel() {

    private val today = DateUtils.getStartOfDay()
    private val endOfToday = DateUtils.getEndOfDay()

    val tasksUiState: StateFlow<TasksUiState> = combine(
        assignmentRepository.getTodayAssignments(today, endOfToday),
        assignmentRepository.getUpcomingAssignments(endOfToday),
        assignmentRepository.getAssignmentsByStatus("Completed"),
        assignmentRepository.getOverdueAssignments(today),
        examRepository.getUpcomingExams(today)
    ) { todayList, upcoming, completed, overdue, exams ->
        TasksUiState(
            todayAssignments = todayList,
            upcomingAssignments = upcoming,
            completedAssignments = completed,
            overdueAssignments = overdue,
            upcomingExams = exams
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TasksUiState())

    fun completeAssignment(assignmentId: Long) {
        viewModelScope.launch {
            completeAssignmentUseCase(assignmentId)
        }
    }

    fun saveAssignment(
        assignmentId: Long?,
        title: String,
        subject: String,
        description: String,
        dueDate: Long,
        priority: String
    ) {
        viewModelScope.launch {
            if (assignmentId != null) {
                val existing = assignmentRepository.getAssignmentById(assignmentId) ?: return@launch
                assignmentRepository.updateAssignment(
                    existing.copy(
                        title = title, subject = subject,
                        description = description, dueDate = dueDate, priority = priority
                    )
                )
            } else {
                assignmentRepository.insertAssignment(
                    AssignmentEntity(
                        title = title, subject = subject,
                        description = description, dueDate = dueDate, priority = priority
                    )
                )
            }
        }
    }

    fun deleteAssignment(assignmentId: Long) {
        viewModelScope.launch { assignmentRepository.deleteAssignmentById(assignmentId) }
    }

    fun saveExam(examId: Long?, name: String, subject: String, date: Long) {
        viewModelScope.launch {
            if (examId != null) {
                val existing = examRepository.getExamById(examId) ?: return@launch
                examRepository.updateExam(existing.copy(name = name, subject = subject, date = date))
            } else {
                examRepository.insertExam(ExamEntity(name = name, subject = subject, date = date))
            }
        }
    }

    fun deleteExam(examId: Long) {
        viewModelScope.launch { examRepository.deleteExamById(examId) }
    }

    suspend fun getAssignmentById(id: Long) = assignmentRepository.getAssignmentById(id)
    suspend fun getExamById(id: Long) = examRepository.getExamById(id)
}
