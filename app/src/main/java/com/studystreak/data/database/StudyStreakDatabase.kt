package com.studystreak.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.studystreak.data.dao.*
import com.studystreak.data.models.*

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class,
        StudySessionEntity::class,
        AssignmentEntity::class,
        ExamEntity::class,
        UserStatsEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class StudyStreakDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun examDao(): ExamDao
    abstract fun userStatsDao(): UserStatsDao
}
