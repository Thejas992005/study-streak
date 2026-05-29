package com.studystreak.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studystreak.data.repository.HabitRepository
import com.studystreak.utils.Constants
import com.studystreak.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class HabitReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val habitRepository: HabitRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val habits = habitRepository.getActiveHabits().first()
            if (habits.isNotEmpty()) {
                val habit = habits.random()
                NotificationHelper.showNotification(
                    context = applicationContext,
                    channelId = Constants.CHANNEL_HABIT_REMINDERS,
                    notificationId = 1001,
                    title = "Time for ${habit.name}! ${habit.icon}",
                    message = "Stay consistent and build your streak!"
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
