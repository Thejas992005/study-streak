package com.studystreak.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studystreak.data.repository.AssignmentRepository
import com.studystreak.utils.Constants
import com.studystreak.utils.DateUtils
import com.studystreak.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class AssignmentDueWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val assignmentRepository: AssignmentRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val today = DateUtils.getStartOfDay()
            val tomorrow = today + 24 * 60 * 60 * 1000L
            val endOfTomorrow = DateUtils.getEndOfDay(tomorrow)
            val upcoming = assignmentRepository.getTodayAssignments(tomorrow, endOfTomorrow).first()

            upcoming.forEach { assignment ->
                NotificationHelper.showNotification(
                    context = applicationContext,
                    channelId = Constants.CHANNEL_ASSIGNMENT_REMINDERS,
                    notificationId = (2000 + assignment.id).toInt(),
                    title = "📋 Assignment Due Tomorrow!",
                    message = "'${assignment.title}' is due tomorrow. Don't forget to complete it!"
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
