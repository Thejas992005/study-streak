package com.studystreak.presentation.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studystreak.presentation.components.*
import com.studystreak.presentation.theme.*
import com.studystreak.utils.Constants
import com.studystreak.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    onNavigateBack: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val timerState by viewModel.timerUiState.collectAsStateWithLifecycle()
    val allSubjects = Constants.DEFAULT_SUBJECTS + "Custom"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Session") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (timerState.timerState != TimerState.IDLE) {
                            viewModel.stopTimer()
                        }
                        onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Subject Selector
            Text("Select Subject", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allSubjects) { subject ->
                    FilterChip(
                        selected = timerState.selectedSubject == subject,
                        onClick = {
                            if (timerState.timerState == TimerState.IDLE) {
                                viewModel.selectSubject(subject)
                            }
                        },
                        label = { Text(subject) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timer Mode Label
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = when (timerState.timerMode) {
                    TimerMode.WORK -> Indigo60.copy(alpha = 0.15f)
                    TimerMode.SHORT_BREAK -> Green50.copy(alpha = 0.15f)
                    TimerMode.LONG_BREAK -> Amber40.copy(alpha = 0.15f)
                }
            ) {
                Text(
                    text = when (timerState.timerMode) {
                        TimerMode.WORK -> "🎯 Focus Time"
                        TimerMode.SHORT_BREAK -> "☕ Short Break"
                        TimerMode.LONG_BREAK -> "🌴 Long Break"
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = when (timerState.timerMode) {
                        TimerMode.WORK -> Indigo60
                        TimerMode.SHORT_BREAK -> Green50
                        TimerMode.LONG_BREAK -> Amber40
                    }
                )
            }

            // Circular Timer
            val progress = if (timerState.totalMs > 0) {
                timerState.remainingMs.toFloat() / timerState.totalMs.toFloat()
            } else 0f

            AnimatedCircularProgressBar(
                progress = progress,
                modifier = Modifier.size(240.dp),
                strokeWidth = 12.dp,
                color = when (timerState.timerMode) {
                    TimerMode.WORK -> Indigo60
                    TimerMode.SHORT_BREAK -> Green50
                    TimerMode.LONG_BREAK -> Amber40
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                TimerDisplay(timeText = DateUtils.formatTimer(timerState.remainingMs))
            }

            // Completed Pomodoros
            Text(
                text = "🍅 ${timerState.completedPomodoros} Pomodoros",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Total Study Time
            Text(
                text = "Total: ${DateUtils.formatDuration(timerState.elapsedStudyMs)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (timerState.timerState) {
                    TimerState.IDLE -> {
                        Button(
                            onClick = { viewModel.startTimer() },
                            modifier = Modifier
                                .height(56.dp)
                                .width(200.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Filled.PlayArrow, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    TimerState.RUNNING -> {
                        OutlinedButton(
                            onClick = { viewModel.stopTimer() },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Filled.Stop, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Stop")
                        }
                        Button(
                            onClick = { viewModel.pauseTimer() },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Filled.Pause, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pause")
                        }
                    }
                    TimerState.PAUSED -> {
                        OutlinedButton(
                            onClick = { viewModel.stopTimer() },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Filled.Stop, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Stop")
                        }
                        Button(
                            onClick = { viewModel.resumeTimer() },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Filled.PlayArrow, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resume")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
