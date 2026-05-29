package com.studystreak.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studystreak.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAssignmentScreen(
    assignmentId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val isEdit = assignmentId != null
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf(Constants.DEFAULT_SUBJECTS.first()) }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableLongStateOf(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L) }
    var priority by remember { mutableStateOf("Medium") }
    var isLoaded by remember { mutableStateOf(!isEdit) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)

    LaunchedEffect(assignmentId) {
        if (assignmentId != null) {
            viewModel.getAssignmentById(assignmentId)?.let { a ->
                title = a.title; subject = a.subject; description = a.description
                dueDate = a.dueDate; priority = a.priority; isLoaded = true
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dueDate = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Assignment" else "Add Assignment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.saveAssignment(assignmentId, title, subject, description, dueDate, priority)
                                onNavigateBack()
                            }
                        },
                        enabled = title.isNotBlank()
                    ) { Text("Save", fontWeight = FontWeight.Bold) }
                }
            )
        }
    ) { padding ->
        if (!isLoaded) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true
            )

            // Subject
            Text("Subject", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Constants.DEFAULT_SUBJECTS + "Other") { s ->
                    FilterChip(selected = subject == s, onClick = { subject = s }, label = { Text(s) })
                }
            }

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Description (optional)") }, modifier = Modifier.fillMaxWidth(),
                minLines = 3, maxLines = 5
            )

            // Due Date
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Due: ${com.studystreak.utils.DateUtils.formatDate(dueDate)}")
            }

            // Priority
            Text("Priority", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Low", "Medium", "High").forEach { p ->
                    FilterChip(selected = priority == p, onClick = { priority = p }, label = { Text(p) })
                }
            }
        }
    }
}
