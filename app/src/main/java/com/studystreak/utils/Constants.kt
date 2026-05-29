package com.studystreak.utils

object Constants {

    // XP Awards
    const val XP_COMPLETE_HABIT = 10
    const val XP_STUDY_PER_HOUR = 15
    const val XP_COMPLETE_ASSIGNMENT = 20
    const val XP_STREAK_7_DAYS = 50
    const val XP_STREAK_30_DAYS = 150

    // Level Thresholds
    val LEVEL_THRESHOLDS = mapOf(
        1 to 0,
        2 to 100,
        3 to 300,
        4 to 600,
        5 to 1000
    )

    // Level Titles
    val LEVEL_TITLES = mapOf(
        1 to "Beginner",
        2 to "Consistent Learner",
        3 to "Focused Student",
        4 to "Study Warrior",
        5 to "Academic Master"
    )

    // Pomodoro Defaults (in milliseconds)
    const val POMODORO_WORK_DURATION = 25 * 60 * 1000L       // 25 minutes
    const val POMODORO_SHORT_BREAK = 5 * 60 * 1000L          // 5 minutes
    const val POMODORO_LONG_BREAK = 15 * 60 * 1000L          // 15 minutes
    const val POMODORO_SESSIONS_BEFORE_LONG_BREAK = 4

    // Default Subjects
    val DEFAULT_SUBJECTS = listOf(
        "Mathematics",
        "Physics",
        "Chemistry",
        "Computer Science"
    )

    // Habit Categories
    val HABIT_CATEGORIES = listOf(
        "Study",
        "Coding",
        "Reading",
        "Fitness",
        "Sleep",
        "Custom"
    )

    // Habit Icons
    val HABIT_ICONS = listOf(
        "📚", "💻", "📖", "🏋️", "😴", "🎯",
        "✍️", "🧪", "🎨", "🎵", "🧘", "🏃"
    )

    // Notification Channels
    const val CHANNEL_HABIT_REMINDERS = "habit_reminders"
    const val CHANNEL_STUDY_REMINDERS = "study_reminders"
    const val CHANNEL_ASSIGNMENT_REMINDERS = "assignment_reminders"
    const val CHANNEL_EXAM_REMINDERS = "exam_reminders"
    const val CHANNEL_STREAK_PROTECTION = "streak_protection"

    // WorkManager Tags
    const val WORK_TAG_HABIT_REMINDER = "habit_reminder"
    const val WORK_TAG_STUDY_REMINDER = "study_reminder"
    const val WORK_TAG_ASSIGNMENT_DUE = "assignment_due"
    const val WORK_TAG_EXAM_REMINDER = "exam_reminder"
    const val WORK_TAG_STREAK_PROTECTION = "streak_protection"

    /** Calculate level from total XP. */
    fun getLevelForXp(totalXp: Int): Int {
        return LEVEL_THRESHOLDS.entries
            .sortedByDescending { it.value }
            .firstOrNull { totalXp >= it.value }
            ?.key ?: 1
    }

    /** Get XP required for next level. Returns null if max level. */
    fun getXpForNextLevel(currentLevel: Int): Int? {
        val nextLevel = currentLevel + 1
        return LEVEL_THRESHOLDS[nextLevel]
    }

    /** Get XP progress within current level (0.0 to 1.0). */
    fun getLevelProgress(totalXp: Int): Float {
        val currentLevel = getLevelForXp(totalXp)
        val currentThreshold = LEVEL_THRESHOLDS[currentLevel] ?: 0
        val nextThreshold = LEVEL_THRESHOLDS[currentLevel + 1] ?: return 1f
        val progress = (totalXp - currentThreshold).toFloat() / (nextThreshold - currentThreshold).toFloat()
        return progress.coerceIn(0f, 1f)
    }
}
