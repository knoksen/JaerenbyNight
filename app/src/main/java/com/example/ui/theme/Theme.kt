package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NightColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = MidnightBackground,
    primaryContainer = MidnightSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = NeonViolet,
    onSecondary = TextPrimary,
    secondaryContainer = MidnightSurfaceVariant,
    onSecondaryContainer = NeonViolet,
    tertiary = SafeGreen,
    onTertiary = MidnightBackground,
    tertiaryContainer = SafeGreenContainer,
    onTertiaryContainer = SafeGreen,
    background = MidnightBackground,
    onBackground = TextPrimary,
    surface = MidnightSurface,
    onSurface = TextPrimary,
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = MidnightCardBorder,
    error = DangerRed,
    onError = TextPrimary
)

@Composable
fun NightGuideTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NightColorScheme,
        typography = Typography,
        content = content
    )
}
