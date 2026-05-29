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
import com.studystreak.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExamScreen(
    examId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val isEdit = examId != null
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf(Constants.DEFAULT_SUBJECTS.first()) }
    var date by remember { mutableLongStateOf(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000L) }
    var isLoaded by remember { mutableStateOf(!isEdit) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)

    LaunchedEffect(examId) {
        if (examId != null) {
            viewModel.getExamById(examId)?.let { e ->
                name = e.name; subject = e.subject; date = e.date; isLoaded = true
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { date = it }
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
                title = { Text(if (isEdit) "Edit Exam" else "Add Exam") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                viewModel.saveExam(examId, name, subject, date)
                                onNavigateBack()
                            }
                        },
                        enabled = name.isNotBlank()
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
                value = name, onValueChange = { name = it },
                label = { Text("Exam Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true
            )

            Text("Subject", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Constants.DEFAULT_SUBJECTS + "Other") { s ->
                    FilterChip(selected = subject == s, onClick = { subject = s }, label = { Text(s) })
                }
            }

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Date: ${DateUtils.formatDate(date)}")
            }

            // Show countdown preview
            val daysUntil = DateUtils.daysUntil(date)
            if (daysUntil >= 0) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = when {
                            daysUntil == 0 -> "📢 Exam is Today!"
                            daysUntil == 1 -> "⚡ Exam is Tomorrow!"
                            else -> "📅 Exam in $daysUntil days"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
