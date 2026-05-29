package com.studystreak.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Indigo50,
    onPrimary = Neutral99,
    primaryContainer = Indigo90,
    onPrimaryContainer = Indigo10,
    secondary = Violet50,
    onSecondary = Neutral99,
    secondaryContainer = Violet90,
    onSecondaryContainer = Indigo10,
    tertiary = Amber40,
    onTertiary = Neutral10,
    tertiaryContainer = Amber90,
    onTertiaryContainer = Neutral10,
    error = Red50,
    onError = Neutral99,
    errorContainer = Red90,
    onErrorContainer = Neutral10,
    background = Neutral95,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = Neutral40,
    outline = Neutral60,
    outlineVariant = Neutral80,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral90,
    inversePrimary = Indigo80
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = Indigo20,
    primaryContainer = Indigo40,
    onPrimaryContainer = Indigo90,
    secondary = Violet60,
    onSecondary = Indigo10,
    secondaryContainer = Violet40,
    onSecondaryContainer = Violet90,
    tertiary = Amber50,
    onTertiary = Neutral10,
    tertiaryContainer = Amber40,
    onTertiaryContainer = Amber90,
    error = Red60,
    onError = Neutral10,
    errorContainer = Red40,
    onErrorContainer = Red90,
    background = SurfaceDark,
    onBackground = Neutral90,
    surface = SurfaceDarkVariant,
    onSurface = Neutral90,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = Neutral70,
    outline = Neutral50,
    outlineVariant = Neutral30,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral20,
    inversePrimary = Indigo50
)

@Composable
fun StudyStreakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = StudyStreakTypography,
        shapes = StudyStreakShapes,
        content = content
    )
}
