package com.studystreak.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studystreak.presentation.theme.*
import com.studystreak.utils.Constants

@Composable
fun XpProgressBar(
    totalXp: Int,
    currentLevel: Int,
    modifier: Modifier = Modifier
) {
    val progress = Constants.getLevelProgress(totalXp)
    val levelTitle = Constants.LEVEL_TITLES[currentLevel] ?: "Beginner"
    val nextLevelXp = Constants.getXpForNextLevel(currentLevel)
    val currentThreshold = Constants.LEVEL_THRESHOLDS[currentLevel] ?: 0

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Level $currentLevel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = levelTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$totalXp XP",
                style = MaterialTheme.typography.titleMedium,
                color = Amber40
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedLinearProgressBar(
            progress = progress,
            color = Indigo60,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            height = 10.dp
        )
        if (nextLevelXp != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${totalXp - currentThreshold} / ${nextLevelXp - currentThreshold} XP to Level ${currentLevel + 1}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
