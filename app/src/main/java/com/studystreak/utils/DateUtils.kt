package com.studystreak.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    /** Returns start-of-day millis (midnight) for the given timestamp. */
    fun getStartOfDay(timeMillis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /** Returns end-of-day millis (23:59:59.999) for the given timestamp. */
    fun getEndOfDay(timeMillis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    /** Returns start-of-day millis for N days ago. */
    fun getDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        return getStartOfDay(calendar.timeInMillis)
    }

    /** Returns start-of-week millis (Monday). */
    fun getStartOfWeek(timeMillis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeMillis
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        return getStartOfDay(calendar.timeInMillis)
    }

    /** Returns start-of-month millis. */
    fun getStartOfMonth(timeMillis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeMillis
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return getStartOfDay(calendar.timeInMillis)
    }

    /** Formats millis to "MMM dd, yyyy". */
    fun formatDate(timeMillis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    /** Formats millis to "MMM dd". */
    fun formatDateShort(timeMillis: Long): String {
        val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    /** Formats millis to "hh:mm a". */
    fun formatTime(timeMillis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    /** Formats duration millis to "Xh Ym" string. */
    fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "${totalSeconds}s"
        }
    }

    /** Formats duration millis to "MM:SS" for timer display. */
    fun formatTimer(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    /** Returns days between now and target date. Negative if past. */
    fun daysUntil(targetMillis: Long): Int {
        val now = getStartOfDay()
        val target = getStartOfDay(targetMillis)
        val diffMs = target - now
        return (diffMs / (24 * 60 * 60 * 1000)).toInt()
    }

    /** Returns the greeting based on current hour. */
    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            hour < 21 -> "Good Evening"
            else -> "Good Night"
        }
    }

    /** Returns start-of-day millis for each day of the current week. */
    fun getCurrentWeekDays(): List<Long> {
        val startOfWeek = getStartOfWeek()
        return (0..6).map { day ->
            val cal = Calendar.getInstance().apply {
                timeInMillis = startOfWeek
                add(Calendar.DAY_OF_YEAR, day)
            }
            getStartOfDay(cal.timeInMillis)
        }
    }
}
