package com.studystreak.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String,
    val description: String = "",
    val dueDate: Long,
    val priority: String = "Medium", // Low, Medium, High
    val status: String = "Pending", // Pending, Completed, Overdue
    val createdAt: Long = System.currentTimeMillis()
)
