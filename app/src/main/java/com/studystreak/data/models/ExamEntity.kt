package com.studystreak.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val subject: String,
    val date: Long,
    val createdAt: Long = System.currentTimeMillis()
)
