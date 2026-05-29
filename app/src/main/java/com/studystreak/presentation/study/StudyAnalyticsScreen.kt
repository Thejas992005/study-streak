package com.studystreak.presentation.study

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studystreak.presentation.components.StudyStreakCard
import com.studystreak.presentation.theme.*
import com.studystreak.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyAnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val state by viewModel.analyticsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadAnalytics() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Analytics") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Today", DateUtils.formatDuration(state.todayMs), Indigo60, Modifier.weight(1f))
                StatCard("Week", DateUtils.formatDuration(state.weeklyMs), Violet50, Modifier.weight(1f))
                StatCard("Month", DateUtils.formatDuration(state.monthlyMs), Amber40, Modifier.weight(1f))
            }

            // Weekly Bar Chart
            StudyStreakCard {
                Text("Weekly Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                if (state.dailyDurations.isNotEmpty()) {
                    WeeklyBarChart(
                        data = state.dailyDurations,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                } else {
                    Text("No data yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Subject Breakdown
            StudyStreakCard {
                Text("Subject Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                if (state.subjectDurations.isNotEmpty()) {
                    val total = state.subjectDurations.sumOf { it.totalDuration }.toFloat()
                    val colors = listOf(ChartBlue, ChartPurple, ChartPink, ChartTeal, ChartOrange)
                    SubjectPieChart(
                        data = state.subjectDurations.mapIndexed { i, sd ->
                            Triple(sd.subject, sd.totalDuration, colors[i % colors.size])
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    state.subjectDurations.forEachIndexed { i, sd ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Canvas(modifier = Modifier.size(12.dp)) {
                                    drawCircle(color = colors[i % colors.size])
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(sd.subject, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                DateUtils.formatDuration(sd.totalDuration),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (i < state.subjectDurations.lastIndex) Spacer(modifier = Modifier.height(4.dp))
                    }
                } else {
                    Text("No data yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Average
            if (state.weeklyMs > 0) {
                StudyStreakCard {
                    Text("Average Daily Study Time", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateUtils.formatDuration(state.weeklyMs / 7),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    StudyStreakCard(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun WeeklyBarChart(data: List<Pair<String, Long>>, modifier: Modifier = Modifier) {
    val maxDuration = data.maxOfOrNull { it.second } ?: 1L
    val barColor = Indigo60
    val textColor = Neutral50

    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2f)
        val spacing = barWidth
        val chartHeight = size.height - 30f

        data.forEachIndexed { index, (label, duration) ->
            val barHeight = if (maxDuration > 0) (duration.toFloat() / maxDuration * chartHeight) else 0f
            val x = index * (barWidth + spacing) + spacing / 2

            // Bar
            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, chartHeight - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )

            // Label
            drawContext.canvas.nativeCanvas.drawText(
                label.takeLast(3),
                x + barWidth / 2,
                size.height,
                android.graphics.Paint().apply {
                    this.color = textColor.hashCode()
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun SubjectPieChart(data: List<Triple<String, Long, Color>>, modifier: Modifier = Modifier) {
    val total = data.sumOf { it.second }.toFloat()

    Canvas(modifier = modifier) {
        val diameter = minOf(size.width, size.height) * 0.8f
        val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
        var startAngle = -90f

        data.forEach { (_, duration, color) ->
            val sweep = if (total > 0) (duration / total) * 360f else 0f
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                topLeft = topLeft,
                size = Size(diameter, diameter)
            )
            startAngle += sweep
        }

        // Center hole for donut effect
        drawCircle(
            color = Color.White,
            radius = diameter * 0.3f,
            center = Offset(size.width / 2, size.height / 2)
        )
    }
}
