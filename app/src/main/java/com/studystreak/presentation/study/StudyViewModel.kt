package com.studystreak.presentation.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studystreak.data.dao.SubjectDuration
import com.studystreak.data.models.StudySessionEntity
import com.studystreak.data.repository.StudyRepository
import com.studystreak.domain.usecases.EndStudySessionUseCase
import com.studystreak.domain.usecases.StartStudySessionUseCase
import com.studystreak.utils.Constants
import com.studystreak.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimerState { IDLE, RUNNING, PAUSED }
enum class TimerMode { WORK, SHORT_BREAK, LONG_BREAK }

data class StudyUiState(
    val todaySessions: List<StudySessionEntity> = emptyList(),
    val todayDurationMs: Long = 0,
    val weeklyDurationMs: Long = 0
)

data class TimerUiState(
    val timerState: TimerState = TimerState.IDLE,
    val timerMode: TimerMode = TimerMode.WORK,
    val remainingMs: Long = Constants.POMODORO_WORK_DURATION,
    val totalMs: Long = Constants.POMODORO_WORK_DURATION,
    val selectedSubject: String = Constants.DEFAULT_SUBJECTS.first(),
    val completedPomodoros: Int = 0,
    val elapsedStudyMs: Long = 0
)

data class AnalyticsUiState(
    val todayMs: Long = 0,
    val weeklyMs: Long = 0,
    val monthlyMs: Long = 0,
    val subjectDurations: List<SubjectDuration> = emptyList(),
    val dailyDurations: List<Pair<String, Long>> = emptyList()
)

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val startStudySessionUseCase: StartStudySessionUseCase,
    private val endStudySessionUseCase: EndStudySessionUseCase
) : ViewModel() {

    private val today = DateUtils.getStartOfDay()
    private val startOfWeek = DateUtils.getStartOfWeek()
    private val endOfWeek = DateUtils.getEndOfDay(startOfWeek + 6 * 24 * 60 * 60 * 1000L)

    val studyUiState: StateFlow<StudyUiState> = combine(
        studyRepository.getSessionsForDate(today),
        studyRepository.getTotalDurationForDate(today),
        studyRepository.getTotalDurationBetweenDates(startOfWeek, endOfWeek)
    ) { sessions, todayDuration, weeklyDuration ->
        StudyUiState(
            todaySessions = sessions,
            todayDurationMs = todayDuration,
            weeklyDurationMs = weeklyDuration
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudyUiState())

    private val _timerState = MutableStateFlow(TimerUiState())
    val timerUiState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    private val _analyticsState = MutableStateFlow(AnalyticsUiState())
    val analyticsUiState: StateFlow<AnalyticsUiState> = _analyticsState.asStateFlow()

    private var timerJob: Job? = null
    private var currentSessionId: Long? = null

    fun selectSubject(subject: String) {
        _timerState.update { it.copy(selectedSubject = subject) }
    }

    fun startTimer() {
        viewModelScope.launch {
            if (_timerState.value.timerState == TimerState.IDLE) {
                currentSessionId = startStudySessionUseCase(_timerState.value.selectedSubject)
            }
            _timerState.update { it.copy(timerState = TimerState.RUNNING) }
            startCountdown()
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.update { it.copy(timerState = TimerState.PAUSED) }
    }

    fun resumeTimer() {
        _timerState.update { it.copy(timerState = TimerState.RUNNING) }
        startCountdown()
    }

    fun stopTimer() {
        timerJob?.cancel()
        val elapsed = _timerState.value.elapsedStudyMs
        viewModelScope.launch {
            currentSessionId?.let { id ->
                if (elapsed > 0) {
                    endStudySessionUseCase(id, elapsed)
                }
            }
            currentSessionId = null
            _timerState.value = TimerUiState(selectedSubject = _timerState.value.selectedSubject)
        }
    }

    private fun startCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingMs > 0 && _timerState.value.timerState == TimerState.RUNNING) {
                delay(1000L)
                _timerState.update { state ->
                    val newRemaining = (state.remainingMs - 1000L).coerceAtLeast(0)
                    val newElapsed = if (state.timerMode == TimerMode.WORK) {
                        state.elapsedStudyMs + 1000L
                    } else state.elapsedStudyMs
                    state.copy(
                        remainingMs = newRemaining,
                        elapsedStudyMs = newElapsed
                    )
                }
            }
            // Timer completed
            if (_timerState.value.remainingMs <= 0) {
                onTimerComplete()
            }
        }
    }

    private fun onTimerComplete() {
        val current = _timerState.value
        when (current.timerMode) {
            TimerMode.WORK -> {
                val newPomodoros = current.completedPomodoros + 1
                val nextMode = if (newPomodoros % Constants.POMODORO_SESSIONS_BEFORE_LONG_BREAK == 0) {
                    TimerMode.LONG_BREAK
                } else {
                    TimerMode.SHORT_BREAK
                }
                val nextDuration = when (nextMode) {
                    TimerMode.SHORT_BREAK -> Constants.POMODORO_SHORT_BREAK
                    TimerMode.LONG_BREAK -> Constants.POMODORO_LONG_BREAK
                    else -> Constants.POMODORO_WORK_DURATION
                }
                _timerState.update {
                    it.copy(
                        timerState = TimerState.IDLE,
                        timerMode = nextMode,
                        remainingMs = nextDuration,
                        totalMs = nextDuration,
                        completedPomodoros = newPomodoros
                    )
                }
            }
            TimerMode.SHORT_BREAK, TimerMode.LONG_BREAK -> {
                _timerState.update {
                    it.copy(
                        timerState = TimerState.IDLE,
                        timerMode = TimerMode.WORK,
                        remainingMs = Constants.POMODORO_WORK_DURATION,
                        totalMs = Constants.POMODORO_WORK_DURATION
                    )
                }
            }
        }
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            val startOfMonth = DateUtils.getStartOfMonth()
            val endOfMonth = DateUtils.getEndOfDay()

            val weekDays = DateUtils.getCurrentWeekDays()
            val dailyDurations = weekDays.map { dayStart ->
                val dayEnd = DateUtils.getEndOfDay(dayStart)
                val label = DateUtils.formatDateShort(dayStart).substringBefore(",").takeLast(6)
                studyRepository.getTotalDurationBetweenDates(dayStart, dayEnd).first() to label
            }.map { (duration, label) -> label to duration }

            val subjectDurations = studyRepository.getSubjectDurationsBetweenDates(startOfMonth, endOfMonth)

            val todayMs = studyRepository.getTotalDurationForDate(today).first()
            val weeklyMs = studyRepository.getTotalDurationBetweenDates(startOfWeek, endOfWeek).first()
            val monthlyMs = studyRepository.getTotalDurationBetweenDates(startOfMonth, endOfMonth).first()

            _analyticsState.value = AnalyticsUiState(
                todayMs = todayMs,
                weeklyMs = weeklyMs,
                monthlyMs = monthlyMs,
                subjectDurations = subjectDurations,
                dailyDurations = dailyDurations
            )
        }
    }
}
