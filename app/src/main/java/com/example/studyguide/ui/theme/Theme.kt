package com.example.studyguide.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MinimalWhiteBlackColorScheme = lightColorScheme(
    primary = PrimaryBlack,
    onPrimary = OnPrimaryWhite,
    primaryContainer = PrimaryContainerBlack,
    onPrimaryContainer = OnPrimaryContainerWhite,
    secondary = SecondaryZinc,
    onSecondary = OnSecondaryWhite,
    secondaryContainer = SecondaryContainerZinc,
    onSecondaryContainer = OnSecondaryContainerBlack,
    tertiary = TertiaryZinc,
    onTertiary = OnTertiaryWhite,
    tertiaryContainer = TertiaryContainerZinc,
    onTertiaryContainer = OnTertiaryContainerBlack,
    background = BackgroundWhite,
    onBackground = OnBackgroundBlack,
    surface = SurfaceWhite,
    onSurface = OnSurfaceBlack,
    surfaceVariant = SurfaceVariantZinc,
    onSurfaceVariant = OnSurfaceVariantMuted,
    outline = OutlineZinc,
    outlineVariant = OutlineVariantZinc,
    surfaceContainerLowest = SurfaceContainerLowestWhite,
    surfaceContainerLow = SurfaceContainerLowZinc,
    surfaceContainer = SurfaceContainerZinc,
    surfaceContainerHigh = SurfaceContainerHighZinc,
    surfaceContainerHighest = SurfaceContainerHighestZinc,
    error = ErrorRed,
    onError = OnErrorWhite,
    errorContainer = ErrorContainerRed,
    onErrorContainer = OnErrorContainerRed
)

@Composable
fun StudyGuideTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = MinimalWhiteBlackColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true
                insetsController.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
