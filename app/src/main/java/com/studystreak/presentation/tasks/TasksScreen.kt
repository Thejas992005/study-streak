package com.studystreak.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onAddAssignment: () -> Unit,
    onAddExam: () -> Unit,
    onEditAssignment: (Long) -> Unit,
    onEditExam: (Long) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val state by viewModel.tasksUiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (pagerState.currentPage == 0) onAddAssignment()
                    else onAddExam()
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Assignments") },
                    icon = { Icon(Icons.Filled.Assignment, null) }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Exams") },
                    icon = { Icon(Icons.Filled.School, null) }
                )
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> AssignmentListScreen(
                        todayAssignments = state.todayAssignments,
                        upcomingAssignments = state.upcomingAssignments,
                        completedAssignments = state.completedAssignments,
                        overdueAssignments = state.overdueAssignments,
                        onComplete = { viewModel.completeAssignment(it) },
                        onEdit = onEditAssignment,
                        onDelete = { viewModel.deleteAssignment(it) }
                    )
                    1 -> ExamListScreen(
                        exams = state.upcomingExams,
                        onEdit = onEditExam,
                        onDelete = { viewModel.deleteExam(it) }
                    )
                }
            }
        }
    }
}
