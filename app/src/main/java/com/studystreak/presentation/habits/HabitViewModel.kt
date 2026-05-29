package com.studystreak.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studystreak.data.models.HabitCompletionEntity
import com.studystreak.data.models.HabitEntity
import com.studystreak.data.repository.HabitRepository
import com.studystreak.domain.usecases.CalculateStreakUseCase
import com.studystreak.domain.usecases.CompleteHabitUseCase
import com.studystreak.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitWithStatus(
    val habit: HabitEntity,
    val isCompletedToday: Boolean = false,
    val streak: Int = 0,
    val completionRate: Float = 0f
)

data class HabitDetailState(
    val habit: HabitEntity? = null,
    val completions: List<HabitCompletionEntity> = emptyList(),
    val streak: Int = 0,
    val totalCompletions: Int = 0,
    val completionRate: Float = 0f
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val completeHabitUseCase: CompleteHabitUseCase,
    private val calculateStreakUseCase: CalculateStreakUseCase
) : ViewModel() {

    private val today = DateUtils.getStartOfDay()

    val habitsWithStatus: StateFlow<List<HabitWithStatus>> = combine(
        habitRepository.getActiveHabits(),
        habitRepository.getCompletionsForDate(today)
    ) { habits, completions ->
        val completedIds = completions.filter { it.isCompleted }.map { it.habitId }.toSet()
        habits.map { habit ->
            val streak = calculateStreakUseCase.forHabit(habit.id)
            val totalDays = ((System.currentTimeMillis() - habit.createdAt) / (24 * 60 * 60 * 1000L)).toInt().coerceAtLeast(1)
            val completedDays = habitRepository.getCompletedDaysCount(habit.id)
            HabitWithStatus(
                habit = habit,
                isCompletedToday = habit.id in completedIds,
                streak = streak,
                completionRate = (completedDays.toFloat() / totalDays).coerceIn(0f, 1f)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _habitDetail = MutableStateFlow(HabitDetailState())
    val habitDetail: StateFlow<HabitDetailState> = _habitDetail.asStateFlow()

    fun loadHabitDetail(habitId: Long) {
        viewModelScope.launch {
            val habit = habitRepository.getHabitById(habitId) ?: return@launch
            val streak = calculateStreakUseCase.forHabit(habitId)
            val totalCompletions = habitRepository.getCompletionCount(habitId)
            val totalDays = ((System.currentTimeMillis() - habit.createdAt) / (24 * 60 * 60 * 1000L)).toInt().coerceAtLeast(1)
            val completedDays = habitRepository.getCompletedDaysCount(habitId)

            habitRepository.getCompletionsForHabit(habitId).collect { completions ->
                _habitDetail.value = HabitDetailState(
                    habit = habit,
                    completions = completions,
                    streak = streak,
                    totalCompletions = totalCompletions,
                    completionRate = (completedDays.toFloat() / totalDays).coerceIn(0f, 1f)
                )
            }
        }
    }

    fun completeHabit(habitId: Long) {
        viewModelScope.launch {
            completeHabitUseCase(habitId)
        }
    }

    fun saveHabit(
        habitId: Long?,
        name: String,
        category: String,
        dailyTarget: Int,
        reminderTime: String?,
        color: Long,
        icon: String
    ) {
        viewModelScope.launch {
            if (habitId != null) {
                val existing = habitRepository.getHabitById(habitId) ?: return@launch
                habitRepository.updateHabit(
                    existing.copy(
                        name = name,
                        category = category,
                        dailyTarget = dailyTarget,
                        reminderTime = reminderTime,
                        color = color,
                        icon = icon
                    )
                )
            } else {
                habitRepository.insertHabit(
                    HabitEntity(
                        name = name,
                        category = category,
                        dailyTarget = dailyTarget,
                        reminderTime = reminderTime,
                        color = color,
                        icon = icon
                    )
                )
            }
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepository.deleteHabitById(habitId)
        }
    }

    suspend fun getHabitById(habitId: Long): HabitEntity? {
        return habitRepository.getHabitById(habitId)
    }
}
