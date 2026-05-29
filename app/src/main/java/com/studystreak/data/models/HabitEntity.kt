package com.studystreak.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val dailyTarget: Int = 1,
    val reminderTime: String? = null,
    val color: Long = 0xFF6366F1, // Indigo default
    val icon: String = "📚",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
