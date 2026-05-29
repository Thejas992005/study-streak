package com.studystreak.domain.usecases

import com.studystreak.data.models.ExamEntity
import com.studystreak.data.models.HabitEntity
import com.studystreak.data.repository.*
import com.studystreak.data.models.UserStatsEntity
import com.studystreak.utils.DateUtils
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DashboardData(
    val greeting: String = DateUtils.getGreeting(),
    val userName: String = "Student",
    val totalHabits: Int = 0,
    val completedHabitsToday: Int = 0,
    val completionPercentage: Float = 0f,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val todayStudyTimeMs: Long = 0,
    val weeklyStudyTimeMs: Long = 0,
    val upcomingExams: List<ExamEntity> = emptyList(),
    val pendingAssignmentsCount: Int = 0,
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val habits: List<HabitEntity> = emptyList(),
    val completedHabitIds: Set<Long> = emptySet()
)

class GetDashboardDataUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val studyRepository: StudyRepository,
    private val assignmentRepository: AssignmentRepository,
    private val examRepository: ExamRepository,
    private val userStatsRepository: UserStatsRepository
) {
    operator fun invoke(): Flow<DashboardData> {
        val today = DateUtils.getStartOfDay()
        val startOfWeek = DateUtils.getStartOfWeek()
        val endOfWeek = DateUtils.getEndOfDay(startOfWeek + 6 * 24 * 60 * 60 * 1000L)

        return combine(
            habitRepository.getActiveHabits(),
            habitRepository.getCompletionsForDate(today),
            studyRepository.getTotalDurationForDate(today),
            studyRepository.getTotalDurationBetweenDates(startOfWeek, endOfWeek),
            examRepository.getNextExams(today, 3),
            assignmentRepository.getPendingCount(),
            userStatsRepository.getUserStats()
        ) { values ->
            val habits = values[0] as List<*>
            val completions = values[1] as List<*>
            val todayStudy = values[2] as Long
            val weeklyStudy = values[3] as Long
            val exams = values[4] as List<*>
            val pendingCount = values[5] as Int
            val stats = values[6] as UserStatsEntity?

            @Suppress("UNCHECKED_CAST")
            val habitList = habits as List<HabitEntity>
            @Suppress("UNCHECKED_CAST")
            val completedIds = (completions as List<com.studystreak.data.models.HabitCompletionEntity>)
                .filter { it.isCompleted }
                .map { it.habitId }
                .toSet()

            val totalHabits = habitList.size
            val completedCount = completedIds.size
            val percentage = if (totalHabits > 0) completedCount.toFloat() / totalHabits else 0f

            DashboardData(
                greeting = DateUtils.getGreeting(),
                userName = stats?.userName ?: "Student",
                totalHabits = totalHabits,
                completedHabitsToday = completedCount,
                completionPercentage = percentage,
                currentStreak = stats?.currentStreak ?: 0,
                bestStreak = stats?.bestStreak ?: 0,
                todayStudyTimeMs = todayStudy,
                weeklyStudyTimeMs = weeklyStudy,
                @Suppress("UNCHECKED_CAST")
                upcomingExams = exams as List<ExamEntity>,
                pendingAssignmentsCount = pendingCount,
                totalXp = stats?.totalXp ?: 0,
                currentLevel = stats?.currentLevel ?: 1,
                habits = habitList,
                completedHabitIds = completedIds
            )
        }
    }
}
