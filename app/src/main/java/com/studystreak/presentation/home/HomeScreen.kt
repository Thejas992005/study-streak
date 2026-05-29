package com.studystreak.presentation.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
fun HomeScreen(
    onNavigateToHabitDetail: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val data by viewModel.dashboardData.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting
        item {
            Column {
                Text(
                    text = data.greeting + " 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = data.userName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Today's Progress Card
        item {
            StudyStreakCard(
                gradientColors = listOf(Indigo50, Violet40)
            ) {
                Text(
                    text = "Today's Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedCircularProgressBar(
                        progress = data.completionPercentage,
                        modifier = Modifier.size(80.dp),
                        strokeWidth = 8.dp,
                        color = Amber50,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${(data.completionPercentage * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = "${data.completedHabitsToday} of ${data.totalHabits} habits",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = if (data.completedHabitsToday == data.totalHabits && data.totalHabits > 0)
                                "All done! 🎉" else "${data.totalHabits - data.completedHabitsToday} remaining",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Streak Card
        item {
            StudyStreakCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 Streak",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                StreakDisplay(
                    currentStreak = data.currentStreak,
                    bestStreak = data.bestStreak,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Study Statistics
        item {
            StudyStreakCard {
                Text(
                    text = "📖 Study Statistics",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = DateUtils.formatDuration(data.todayStudyTimeMs),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = DateUtils.formatDuration(data.weeklyStudyTimeMs),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Violet50,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "This Week",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Upcoming Exams
        if (data.upcomingExams.isNotEmpty()) {
            item {
                Text(
                    text = "📝 Upcoming Exams",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(data.upcomingExams) { exam ->
                        StudyStreakCard(
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text(
                                text = exam.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = exam.subject,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            CountdownChip(daysRemaining = DateUtils.daysUntil(exam.date))
                        }
                    }
                }
            }
        }

        // Pending Assignments
        item {
            StudyStreakCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📋 Pending Assignments",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${data.pendingAssignmentsCount} assignments pending",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = if (data.pendingAssignmentsCount > 0)
                            Orange50.copy(alpha = 0.15f) else Green50.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${data.pendingAssignmentsCount}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (data.pendingAssignmentsCount > 0) Orange50 else Green50,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // XP Progress
        item {
            StudyStreakCard {
                Text(
                    text = "⭐ XP Progress",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                XpProgressBar(
                    totalXp = data.totalXp,
                    currentLevel = data.currentLevel
                )
            }
        }

        // Bottom spacing
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
