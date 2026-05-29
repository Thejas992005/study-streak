package com.studystreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.navigation.StudyStreakBottomBar
import com.studystreak.navigation.StudyStreakNavHost
import com.studystreak.presentation.theme.StudyStreakTheme
import com.studystreak.utils.workers.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userStatsRepository: UserStatsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleWorkers()

        setContent {
            val userStats by userStatsRepository.getUserStats()
                .collectAsStateWithLifecycle(initialValue = null)
            val isDarkMode = userStats?.isDarkMode ?: false

            StudyStreakTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { StudyStreakBottomBar(navController) }
                ) { innerPadding ->
                    StudyStreakNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun scheduleWorkers() {
        val workManager = WorkManager.getInstance(this)

        // Daily habit reminder
        val habitWork = PeriodicWorkRequestBuilder<HabitReminderWorker>(
            24, TimeUnit.HOURS
        ).addTag("habit_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_habit_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            habitWork
        )

        // Daily study reminder
        val studyWork = PeriodicWorkRequestBuilder<StudyReminderWorker>(
            24, TimeUnit.HOURS
        ).addTag("study_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_study_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            studyWork
        )

        // Daily assignment due check
        val assignmentWork = PeriodicWorkRequestBuilder<AssignmentDueWorker>(
            24, TimeUnit.HOURS
        ).addTag("assignment_due")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_assignment_check",
            ExistingPeriodicWorkPolicy.KEEP,
            assignmentWork
        )

        // Daily exam reminder check
        val examWork = PeriodicWorkRequestBuilder<ExamReminderWorker>(
            24, TimeUnit.HOURS
        ).addTag("exam_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_exam_check",
            ExistingPeriodicWorkPolicy.KEEP,
            examWork
        )

        // Evening streak protection
        val streakWork = PeriodicWorkRequestBuilder<StreakProtectionWorker>(
            24, TimeUnit.HOURS
        ).addTag("streak_protection")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_streak_protection",
            ExistingPeriodicWorkPolicy.KEEP,
            streakWork
        )
    }
}
