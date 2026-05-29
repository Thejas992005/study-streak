package com.studystreak.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studystreak.presentation.theme.*
import com.studystreak.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    habitId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val isEdit = habitId != null
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Constants.HABIT_CATEGORIES.first()) }
    var dailyTarget by remember { mutableIntStateOf(1) }
    var selectedIcon by remember { mutableStateOf(Constants.HABIT_ICONS.first()) }
    var selectedColor by remember { mutableLongStateOf(0xFF6366F1) }
    var isLoaded by remember { mutableStateOf(!isEdit) }

    val habitColors = listOf(
        0xFF6366F1, 0xFF8B5CF6, 0xFFEC4899, 0xFFEF4444,
        0xFFF97316, 0xFFFBBF24, 0xFF10B981, 0xFF3B82F6,
        0xFF14B8A6, 0xFF6B7280
    )

    LaunchedEffect(habitId) {
        if (habitId != null) {
            viewModel.getHabitById(habitId)?.let { habit ->
                name = habit.name
                category = habit.category
                dailyTarget = habit.dailyTarget
                selectedIcon = habit.icon
                selectedColor = habit.color
                isLoaded = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Habit" else "Add Habit") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                viewModel.saveHabit(
                                    habitId = habitId,
                                    name = name,
                                    category = category,
                                    dailyTarget = dailyTarget,
                                    reminderTime = null,
                                    color = selectedColor,
                                    icon = selectedIcon
                                )
                                onNavigateBack()
                            }
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Category
            Text("Category", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Constants.HABIT_CATEGORIES) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }

            // Icon
            Text("Icon", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Constants.HABIT_ICONS) { icon ->
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable { selectedIcon = icon },
                        color = if (selectedIcon == icon)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = icon, fontSize = 24.sp)
                        }
                    }
                }
            }

            // Color
            Text("Color", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(habitColors) { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(color))
                            .clickable { selectedColor = color }
                            .then(
                                if (selectedColor == color) Modifier.padding(2.dp) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColor == color) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                    }
                }
            }

            // Daily Target
            Text("Daily Target", style = MaterialTheme.typography.titleSmall)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { if (dailyTarget > 1) dailyTarget-- }
                ) {
                    Icon(
                        Icons.Filled.Remove,
                        "Decrease",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "$dailyTarget",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { dailyTarget++ }
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Increase",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
