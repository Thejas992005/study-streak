package com.studystreak.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.studystreak.presentation.theme.FlameOrange
import com.studystreak.presentation.theme.FlameYellow

@Composable
fun StreakBadge(
    streak: Int,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flame")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_pulse"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Streak",
            tint = if (streak > 0) FlameOrange else MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .size(if (streak > 7) 28.dp else 24.dp)
                .then(
                    if (streak > 0) Modifier.scale(scale) else Modifier
                )
        )
        if (showLabel) {
            Text(
                text = if (streak > 0) "$streak Day Streak" else "No Streak",
                style = MaterialTheme.typography.labelLarge,
                color = if (streak > 0) FlameOrange else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StreakDisplay(
    currentStreak: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StreakBadge(streak = currentStreak, showLabel = false)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$currentStreak",
                style = MaterialTheme.typography.headlineMedium,
                color = FlameOrange
            )
            Text(
                text = "Current",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = "Best Streak",
                tint = FlameYellow,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$bestStreak",
                style = MaterialTheme.typography.headlineMedium,
                color = FlameYellow
            )
            Text(
                text = "Best",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
