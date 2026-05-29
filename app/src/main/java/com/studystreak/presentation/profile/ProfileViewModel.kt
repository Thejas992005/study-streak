package com.studystreak.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studystreak.data.models.UserStatsEntity
import com.studystreak.data.repository.ExamRepository
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val stats: UserStatsEntity = UserStatsEntity(),
    val upcomingExamCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userStatsRepository: UserStatsRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    val profileUiState: StateFlow<ProfileUiState> = combine(
        userStatsRepository.getUserStats().filterNotNull(),
        examRepository.getUpcomingExamCount(DateUtils.getStartOfDay())
    ) { stats, examCount ->
        ProfileUiState(stats = stats, upcomingExamCount = examCount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())

    fun updateUserName(name: String) {
        viewModelScope.launch { userStatsRepository.updateUserName(name) }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch { userStatsRepository.updateDarkMode(isDark) }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { userStatsRepository.updateNotifications(enabled) }
    }

    fun updateThemeColor(color: String) {
        viewModelScope.launch { userStatsRepository.updateThemeColor(color) }
    }

    fun resetAllData() {
        viewModelScope.launch {
            userStatsRepository.resetStats()
            userStatsRepository.ensureInitialized()
        }
    }
}
