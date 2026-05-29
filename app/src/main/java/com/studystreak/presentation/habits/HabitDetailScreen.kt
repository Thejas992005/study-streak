package com.studystreak.presentation.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studystreak.presentation.components.*
import com.studystreak.presentation.theme.*
import com.studystreak.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Long,
    onNavigateBack: () -> Unit,
    onEditHabit: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val detail by viewModel.habitDetail.collectAsStateWithLifecycle()

    LaunchedEffect(habitId) {
        viewModel.loadHabitDetail(habitId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail.habit?.name ?: "Habit Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditHabit) {
                        Icon(Icons.Filled.Edit, "Edit")
                    }
                }
            )
        }
    ) { padding ->
        detail.habit?.let { habit ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Habit header
                item {
                    StudyStreakCard(
                        gradientColors = listOf(Indigo50, Violet40)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = habit.icon, fontSize = 48.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = habit.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = androidx.compose.ui.graphics.Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = habit.category,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // Stats
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StudyStreakCard(modifier = Modifier.weight(1f)) {
                            Text("🔥 Streak", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = "${detail.streak} days",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = FlameOrange
                            )
                        }
                        StudyStreakCard(modifier = Modifier.weight(1f)) {
                            Text("✅ Total", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = "${detail.totalCompletions}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Green50
                            )
                        }
                        StudyStreakCard(modifier = Modifier.weight(1f)) {
                            Text("📊 Rate", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = "${(detail.completionRate * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Indigo60
                            )
                        }
                    }
                }

                // Completion Progress
                item {
                    StudyStreakCard {
                        Text(
                            text = "Completion Rate",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AnimatedLinearProgressBar(
                            progress = detail.completionRate,
                            height = 12.dp,
                            color = Indigo60
                        )
                    }
                }

                // Recent History
                item {
                    Text(
                        text = "Recent Completions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val recentCompletions = detail.completions.take(10)
                if (recentCompletions.isEmpty()) {
                    item {
                        Text(
                            text = "No completions yet. Start building your habit!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    recentCompletions.forEach { completion ->
                        item {
                            Card(
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(
                                    containerColor = Green50.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.CheckCircle,
                                            contentDescription = null,
                                            tint = Green50,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = DateUtils.formatDate(completion.dateMillis),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Text(
                                        text = DateUtils.formatTime(completion.completedAt),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
