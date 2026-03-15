package com.struxo.kit.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Struxo application theme.
 *
 * Wraps [MaterialTheme] with the project's color schemes, typography,
 * and (in the future) shape definitions. All screens must be wrapped
 * in [AppTheme] so that Material 3 tokens resolve correctly.
 *
 * @param darkTheme Whether to use the dark color scheme.
 * @param content The composable content.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
