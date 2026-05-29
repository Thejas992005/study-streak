package com.studystreak.navigation

import kotlinx.serialization.Serializable

// Bottom navigation destinations
@Serializable data object HomeRoute
@Serializable data object HabitsRoute
@Serializable data object StudyRoute
@Serializable data object TasksRoute
@Serializable data object ProfileRoute

// Detail destinations
@Serializable data class HabitDetailRoute(val habitId: Long)
@Serializable data class AddEditHabitRoute(val habitId: Long = -1L)
@Serializable data object StudySessionRoute
@Serializable data object StudyAnalyticsRoute
@Serializable data class AddEditAssignmentRoute(val assignmentId: Long = -1L)
@Serializable data class AddEditExamRoute(val examId: Long = -1L)
@Serializable data object SettingsRoute
