package com.studystreak.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.studystreak.presentation.theme.*

@Composable
fun CountdownChip(
    daysRemaining: Int,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, text) = when {
        daysRemaining <= 0 -> Red50 to "Today!"
        daysRemaining == 1 -> Red50 to "Tomorrow"
        daysRemaining <= 3 -> Orange50 to "In $daysRemaining days"
        daysRemaining <= 7 -> Amber40 to "In $daysRemaining days"
        else -> Green50 to "In $daysRemaining days"
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Timer,
                contentDescription = null,
                tint = backgroundColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = backgroundColor,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
