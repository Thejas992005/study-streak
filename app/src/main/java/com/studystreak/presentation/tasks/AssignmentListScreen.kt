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
import com.studystreak.data.models.AssignmentEntity
import com.studystreak.presentation.components.*
import com.studystreak.presentation.theme.*
import com.studystreak.utils.DateUtils

@Composable
fun AssignmentListScreen(
    todayAssignments: List<AssignmentEntity>,
    upcomingAssignments: List<AssignmentEntity>,
    completedAssignments: List<AssignmentEntity>,
    overdueAssignments: List<AssignmentEntity>,
    onComplete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    val hasAny = todayAssignments.isNotEmpty() || upcomingAssignments.isNotEmpty() ||
            completedAssignments.isNotEmpty() || overdueAssignments.isNotEmpty()

    if (!hasAny) {
        EmptyStateView(
            icon = "📋",
            title = "No Assignments",
            subtitle = "Add assignments to track your work",
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Overdue
        if (overdueAssignments.isNotEmpty()) {
            item {
                Text("⚠️ Overdue", style = MaterialTheme.typography.titleSmall, color = Red50, fontWeight = FontWeight.Bold)
            }
            items(overdueAssignments, key = { it.id }) { assignment ->
                AssignmentCard(assignment, onComplete, onEdit, onDelete)
            }
        }

        // Today
        if (todayAssignments.isNotEmpty()) {
            item {
                Text("📅 Today", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            items(todayAssignments, key = { it.id }) { assignment ->
                AssignmentCard(assignment, onComplete, onEdit, onDelete)
            }
        }

        // Upcoming
        if (upcomingAssignments.isNotEmpty()) {
            item {
                Text("🔜 Upcoming", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            items(upcomingAssignments, key = { it.id }) { assignment ->
                AssignmentCard(assignment, onComplete, onEdit, onDelete)
            }
        }

        // Completed
        if (completedAssignments.isNotEmpty()) {
            item {
                Text("✅ Completed", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            items(completedAssignments.take(5), key = { it.id }) { assignment ->
                AssignmentCard(assignment, onComplete, onEdit, onDelete, showComplete = false)
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun AssignmentCard(
    assignment: AssignmentEntity,
    onComplete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    showComplete: Boolean = true
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = assignment.subject,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PriorityBadge(priority = assignment.priority)
                    StatusBadge(status = assignment.status)
                }
            }

            if (assignment.description.isNotBlank()) {
                Text(
                    text = assignment.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due: ${DateUtils.formatDate(assignment.dueDate)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row {
                    if (showComplete && assignment.status != "Completed") {
                        IconButton(onClick = { onComplete(assignment.id) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.CheckCircle, "Complete", tint = Green50, modifier = Modifier.size(20.dp))
                        }
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.MoreVert, "More", modifier = Modifier.size(20.dp))
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Edit") }, onClick = { showMenu = false; onEdit(assignment.id) })
                            DropdownMenuItem(text = { Text("Delete") }, onClick = { showMenu = false; onDelete(assignment.id) })
                        }
                    }
                }
            }
        }
    }
}
