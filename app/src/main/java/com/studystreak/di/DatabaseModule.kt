package com.studystreak.di

import android.content.Context
import androidx.room.Room
import com.studystreak.data.dao.*
import com.studystreak.data.database.StudyStreakDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StudyStreakDatabase {
        return Room.databaseBuilder(
            context,
            StudyStreakDatabase::class.java,
            "studystreak.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHabitDao(database: StudyStreakDatabase): HabitDao = database.habitDao()

    @Provides
    fun provideStudySessionDao(database: StudyStreakDatabase): StudySessionDao = database.studySessionDao()

    @Provides
    fun provideAssignmentDao(database: StudyStreakDatabase): AssignmentDao = database.assignmentDao()

    @Provides
    fun provideExamDao(database: StudyStreakDatabase): ExamDao = database.examDao()

    @Provides
    fun provideUserStatsDao(database: StudyStreakDatabase): UserStatsDao = database.userStatsDao()
}
