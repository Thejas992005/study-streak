package com.studystreak.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    fun createNotificationChannels(context: Context) {
        val channels = listOf(
            NotificationChannel(
                Constants.CHANNEL_HABIT_REMINDERS,
                "Habit Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Reminders to complete your daily habits" },

            NotificationChannel(
                Constants.CHANNEL_STUDY_REMINDERS,
                "Study Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Reminders to study" },

            NotificationChannel(
                Constants.CHANNEL_ASSIGNMENT_REMINDERS,
                "Assignment Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Alerts for upcoming assignment deadlines" },

            NotificationChannel(
                Constants.CHANNEL_EXAM_REMINDERS,
                "Exam Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Alerts for upcoming exams" },

            NotificationChannel(
                Constants.CHANNEL_STREAK_PROTECTION,
                "Streak Protection",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Reminders to maintain your streak" }
        )

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        channels.forEach { manager.createNotificationChannel(it) }
    }

    fun showNotification(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String
    ) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Notification permission not granted
        }
    }
}
