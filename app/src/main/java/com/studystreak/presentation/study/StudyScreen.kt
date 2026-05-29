package com.studystreak.presentation.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studystreak.presentation.components.*
import com.studystreak.presentation.theme.*
import com.studystreak.utils.DateUtils

@Composable
fun StudyScreen(
    onStartSession: () -> Unit,
    onViewAnalytics: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val state by viewModel.studyUiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "Study Tracker",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Quick Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StudyStreakCard(
                    modifier = Modifier.weight(1f),
                    gradientColors = listOf(Indigo50, Violet40)
                ) {
                    Text("Today", style = MaterialTheme.typography.labelMedium, color = androidx.compose.ui.graphics.Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateUtils.formatDuration(state.todayDurationMs),
                        style = MaterialTheme.typography.headlineSmall,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                StudyStreakCard(
                    modifier = Modifier.weight(1f),
                    gradientColors = listOf(Violet40, Indigo60)
                ) {
                    Text("This Week", style = MaterialTheme.typography.labelMedium, color = androidx.compose.ui.graphics.Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateUtils.formatDuration(state.weeklyDurationMs),
                        style = MaterialTheme.typography.headlineSmall,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Start Session Button
        item {
            Button(
                onClick = onStartSession,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Study Session", style = MaterialTheme.typography.titleMedium)
            }
        }

        // Analytics Button
        item {
            OutlinedButton(
                onClick = onViewAnalytics,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.BarChart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Analytics")
            }
        }

        // Today's Sessions
        item {
            Text(
                text = "Today's Sessions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (state.todaySessions.isEmpty()) {
            item {
                EmptyStateView(
                    icon = "📖",
                    title = "No Sessions Today",
                    subtitle = "Start a Pomodoro session to track your study time"
                )
            }
        } else {
            items(state.todaySessions) { session ->
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = session.subject,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = DateUtils.formatTime(session.startTime),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = DateUtils.formatDuration(session.durationMs),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
