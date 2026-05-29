package com.studystreak.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studystreak.domain.usecases.CompleteHabitUseCase
import com.studystreak.domain.usecases.DashboardData
import com.studystreak.domain.usecases.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getDashboardDataUseCase: GetDashboardDataUseCase,
    private val completeHabitUseCase: CompleteHabitUseCase
) : ViewModel() {

    val dashboardData: StateFlow<DashboardData> = getDashboardDataUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardData()
        )

    fun completeHabit(habitId: Long) {
        viewModelScope.launch {
            completeHabitUseCase(habitId)
        }
    }
}
