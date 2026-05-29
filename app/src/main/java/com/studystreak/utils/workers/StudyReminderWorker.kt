package com.studystreak.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studystreak.utils.Constants
import com.studystreak.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StudyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        NotificationHelper.showNotification(
            context = applicationContext,
            channelId = Constants.CHANNEL_STUDY_REMINDERS,
            notificationId = 1002,
            title = "📖 Time to Study!",
            message = "A focused session awaits. Start your Pomodoro timer!"
        )
        return Result.success()
    }
}
