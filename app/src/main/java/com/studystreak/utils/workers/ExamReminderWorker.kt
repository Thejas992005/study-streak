package com.studystreak.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studystreak.data.repository.ExamRepository
import com.studystreak.utils.Constants
import com.studystreak.utils.DateUtils
import com.studystreak.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ExamReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val examRepository: ExamRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val today = DateUtils.getStartOfDay()
            val exams = examRepository.getUpcomingExams(today).first()

            exams.forEach { exam ->
                val daysUntil = DateUtils.daysUntil(exam.date)
                if (daysUntil in listOf(0, 1, 3, 7)) {
                    val message = when (daysUntil) {
                        0 -> "Your ${exam.subject} exam is TODAY! Good luck! 🍀"
                        1 -> "Your ${exam.subject} exam is TOMORROW! Final review time!"
                        3 -> "Your ${exam.subject} exam is in 3 days. Keep preparing!"
                        7 -> "Your ${exam.subject} exam is in 1 week. Stay focused!"
                        else -> return@forEach
                    }
                    NotificationHelper.showNotification(
                        context = applicationContext,
                        channelId = Constants.CHANNEL_EXAM_REMINDERS,
                        notificationId = (3000 + exam.id).toInt(),
                        title = "📝 ${exam.name}",
                        message = message
                    )
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
