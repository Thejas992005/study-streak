package com.studystreak.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.studystreak.presentation.habits.*
import com.studystreak.presentation.home.HomeScreen
import com.studystreak.presentation.profile.ProfileScreen
import com.studystreak.presentation.profile.SettingsScreen
import com.studystreak.presentation.study.*
import com.studystreak.presentation.tasks.*

data class BottomNavItem(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(HomeRoute, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(HabitsRoute, "Habits", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle),
    BottomNavItem(StudyRoute, "Study", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    BottomNavItem(TasksRoute, "Tasks", Icons.Filled.Assignment, Icons.Outlined.Assignment),
    BottomNavItem(ProfileRoute, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun StudyStreakNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        // Bottom nav tabs
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToHabitDetail = { habitId ->
                    navController.navigate(HabitDetailRoute(habitId))
                }
            )
        }

        composable<HabitsRoute> {
            HabitListScreen(
                onAddHabit = { navController.navigate(AddEditHabitRoute()) },
                onHabitClick = { habitId ->
                    navController.navigate(HabitDetailRoute(habitId))
                },
                onEditHabit = { habitId ->
                    navController.navigate(AddEditHabitRoute(habitId))
                }
            )
        }

        composable<StudyRoute> {
            StudyScreen(
                onStartSession = { navController.navigate(StudySessionRoute) },
                onViewAnalytics = { navController.navigate(StudyAnalyticsRoute) }
            )
        }

        composable<TasksRoute> {
            TasksScreen(
                onAddAssignment = { navController.navigate(AddEditAssignmentRoute()) },
                onAddExam = { navController.navigate(AddEditExamRoute()) },
                onEditAssignment = { id -> navController.navigate(AddEditAssignmentRoute(id)) },
                onEditExam = { id -> navController.navigate(AddEditExamRoute(id)) }
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(
                onNavigateToSettings = { navController.navigate(SettingsRoute) }
            )
        }

        // Detail screens
        composable<HabitDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<HabitDetailRoute>()
            HabitDetailScreen(
                habitId = route.habitId,
                onNavigateBack = { navController.popBackStack() },
                onEditHabit = { navController.navigate(AddEditHabitRoute(route.habitId)) }
            )
        }

        composable<AddEditHabitRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditHabitRoute>()
            AddEditHabitScreen(
                habitId = if (route.habitId == -1L) null else route.habitId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<StudySessionRoute> {
            StudySessionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<StudyAnalyticsRoute> {
            StudyAnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<AddEditAssignmentRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditAssignmentRoute>()
            AddEditAssignmentScreen(
                assignmentId = if (route.assignmentId == -1L) null else route.assignmentId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<AddEditExamRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditExamRoute>()
            AddEditExamScreen(
                examId = if (route.examId == -1L) null else route.examId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun StudyStreakBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar for main tabs
    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hasRoute(item.route::class) == true
    }

    if (showBottomBar) {
        NavigationBar {
            bottomNavItems.forEach { item ->
                val selected = currentDestination?.hasRoute(item.route::class) == true
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) }
                )
            }
        }
    }
}
