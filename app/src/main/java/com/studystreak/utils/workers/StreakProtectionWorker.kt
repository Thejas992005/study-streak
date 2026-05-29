package com.studystreak.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studystreak.data.repository.UserStatsRepository
import com.studystreak.utils.Constants
import com.studystreak.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StreakProtectionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userStatsRepository: UserStatsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val stats = userStatsRepository.getUserStatsOnce()
            if (stats != null && stats.currentStreak > 0) {
                NotificationHelper.showNotification(
                    context = applicationContext,
                    channelId = Constants.CHANNEL_STREAK_PROTECTION,
                    notificationId = 4001,
                    title = "🔥 Don't Break Your Streak!",
                    message = "You have a ${stats.currentStreak} day streak! Complete a habit before midnight."
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
