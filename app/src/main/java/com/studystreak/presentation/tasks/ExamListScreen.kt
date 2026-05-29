package com.studystreak.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studystreak.data.models.ExamEntity
import com.studystreak.presentation.components.*
import com.studystreak.presentation.theme.*
import com.studystreak.utils.DateUtils

@Composable
fun ExamListScreen(
    exams: List<ExamEntity>,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    if (exams.isEmpty()) {
        EmptyStateView(
            icon = "📝",
            title = "No Upcoming Exams",
            subtitle = "Add exams to see countdowns",
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Upcoming Exams",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(exams, key = { it.id }) { exam ->
            ExamCard(exam = exam, onEdit = onEdit, onDelete = onDelete)
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun ExamCard(
    exam: ExamEntity,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    val daysUntil = DateUtils.daysUntil(exam.date)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Countdown circle
            val urgencyColor = when {
                daysUntil <= 1 -> Red50
                daysUntil <= 3 -> Orange50
                daysUntil <= 7 -> Amber40
                else -> Green50
            }
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = urgencyColor.copy(alpha = 0.15f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (daysUntil <= 0) "!" else "$daysUntil",
                            style = MaterialTheme.typography.titleLarge,
                            color = urgencyColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (daysUntil <= 0) "Today" else "days",
                            style = MaterialTheme.typography.labelSmall,
                            color = urgencyColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exam.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = exam.subject,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = DateUtils.formatDate(exam.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Filled.MoreVert, "More")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { showMenu = false; onEdit(exam.id) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { showMenu = false; onDelete(exam.id) }
                    )
                }
            }
        }
    }
}
