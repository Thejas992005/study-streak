package com.studystreak.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Long = 1, // Single-row table
    val userName: String = "Student",
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalStudyTimeMs: Long = 0,
    val completedHabitsCount: Int = 0,
    val completedAssignmentsCount: Int = 0,
    val lastActiveDate: Long = 0,
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val themeColor: String = "indigo"
)
