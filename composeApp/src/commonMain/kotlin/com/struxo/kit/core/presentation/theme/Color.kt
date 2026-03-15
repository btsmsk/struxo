package com.struxo.kit.core.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ──────────────────────────────────────────────────────────
//  Brand Palette
// ──────────────────────────────────────────────────────────

private val Primary = Color(0xFF2E86AB)          // Struxo Blue
private val Secondary = Color(0xFF0D6B4E)        // Struxo Green
private val Tertiary = Color(0xFFE8A317)         // Struxo Amber

// ──────────────────────────────────────────────────────────
//  Light Theme Tokens
// ──────────────────────────────────────────────────────────

private val LightPrimary = Primary
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFD0E8F2)
private val LightOnPrimaryContainer = Color(0xFF0A2E3F)

private val LightSecondary = Secondary
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFC5E8DB)
private val LightOnSecondaryContainer = Color(0xFF002818)

private val LightTertiary = Tertiary
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFFFFF0CC)
private val LightOnTertiaryContainer = Color(0xFF3D2800)

private val LightBackground = Color(0xFFFCFCFC)
private val LightOnBackground = Color(0xFF1B1B1F)
private val LightSurface = Color(0xFFFCFCFC)
private val LightOnSurface = Color(0xFF1B1B1F)
private val LightSurfaceVariant = Color(0xFFE2E2EC)
private val LightOnSurfaceVariant = Color(0xFF45464F)
private val LightOutline = Color(0xFF757680)
private val LightOutlineVariant = Color(0xFFC5C6D0)

private val LightError = Color(0xFFBA1A1A)
private val LightOnError = Color(0xFFFFFFFF)
private val LightErrorContainer = Color(0xFFFFDAD6)
private val LightOnErrorContainer = Color(0xFF410002)

// ──────────────────────────────────────────────────────────
//  Dark Theme Tokens
// ──────────────────────────────────────────────────────────

private val DarkPrimary = Color(0xFF8ECAE6)       // Lightened blue for dark surfaces
private val DarkOnPrimary = Color(0xFF003549)
private val DarkPrimaryContainer = Color(0xFF1A6688)
private val DarkOnPrimaryContainer = Color(0xFFD0E8F2)

private val DarkSecondary = Color(0xFF8BD4B8)     // Lightened green for dark surfaces
private val DarkOnSecondary = Color(0xFF00382A)
private val DarkSecondaryContainer = Color(0xFF005139)
private val DarkOnSecondaryContainer = Color(0xFFC5E8DB)

private val DarkTertiary = Color(0xFFFFCC5C)      // Lightened amber for dark surfaces
private val DarkOnTertiary = Color(0xFF3D2800)
private val DarkTertiaryContainer = Color(0xFF7A5100)
private val DarkOnTertiaryContainer = Color(0xFFFFF0CC)

private val DarkBackground = Color(0xFF1B1B1F)
private val DarkOnBackground = Color(0xFFE4E2E6)
private val DarkSurface = Color(0xFF1B1B1F)
private val DarkOnSurface = Color(0xFFE4E2E6)
private val DarkSurfaceVariant = Color(0xFF45464F)
private val DarkOnSurfaceVariant = Color(0xFFC5C6D0)
private val DarkOutline = Color(0xFF8F909A)
private val DarkOutlineVariant = Color(0xFF45464F)

private val DarkError = Color(0xFFFFB4AB)
private val DarkOnError = Color(0xFF690005)
private val DarkErrorContainer = Color(0xFF93000A)
private val DarkOnErrorContainer = Color(0xFFFFDAD6)

// ──────────────────────────────────────────────────────────
//  Material 3 Color Schemes
// ──────────────────────────────────────────────────────────

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
)
