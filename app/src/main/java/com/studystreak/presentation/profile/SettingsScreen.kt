package com.studystreak.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studystreak.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.profileUiState.collectAsStateWithLifecycle()
    val stats = state.stats

    var showNameDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(stats.userName) }

    // Update nameInput when stats load
    LaunchedEffect(stats.userName) {
        nameInput = stats.userName
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Change Name") },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Your Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nameInput.isNotBlank()) {
                        viewModel.updateUserName(nameInput)
                        showNameDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data") },
            text = { Text("This will permanently erase all your habits, sessions, assignments, exams, XP, and streaks. This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllData()
                    showResetDialog = false
                }) {
                    Text("Reset", color = Red50)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile
            ListItem(
                headlineContent = { Text("Name") },
                supportingContent = { Text(stats.userName) },
                leadingContent = { Icon(Icons.Filled.Person, null) },
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingContent = {
                    IconButton(onClick = { showNameDialog = true }) {
                        Icon(Icons.Filled.Edit, "Edit Name")
                    }
                }
            )
            HorizontalDivider()

            // Appearance
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Dark Mode") },
                leadingContent = { Icon(Icons.Filled.DarkMode, null) },
                trailingContent = {
                    Switch(
                        checked = stats.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )
                }
            )
            HorizontalDivider()

            // Theme Color
            ListItem(
                headlineContent = { Text("Theme Color") },
                supportingContent = { Text(stats.themeColor.replaceFirstChar { it.uppercase() }) },
                leadingContent = { Icon(Icons.Filled.Palette, null) }
            )
            HorizontalDivider()

            // Notifications
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Enable Notifications") },
                leadingContent = { Icon(Icons.Filled.Notifications, null) },
                trailingContent = {
                    Switch(
                        checked = stats.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) }
                    )
                }
            )
            HorizontalDivider()

            // Data
            Text(
                text = "Data",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Reset All Data") },
                supportingContent = { Text("Erase everything and start fresh") },
                leadingContent = { Icon(Icons.Filled.DeleteForever, null, tint = Red50) },
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingContent = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(Icons.Filled.ChevronRight, "Reset")
                    }
                }
            )

            // About
            Text(
                text = "About",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("StudyStreak") },
                supportingContent = { Text("Version 1.0.0") },
                leadingContent = { Icon(Icons.Filled.Info, null) }
            )
        }
    }
}
