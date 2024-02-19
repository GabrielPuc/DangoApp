package com.gdbm.dangoapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = CustomPalette(
    primary = MainColorDarker,
    secondary = GreenBackground,
    tertiary = GreenOk,
    background = BackgroundColorDark,
    textColor = Color.White,
    primaryContainerColor = GreenBackground,
    secondaryContainerColor = PinkBackground,
    contrastColor = GreenContrast,
    backgroundCanvas = Black,
    mainContrast = MainContrast
)

private val LightColorScheme = CustomPalette(
    primary = MainColor,
    secondary = PinkBackground,
    tertiary = GreenOk,
    background = BackgroundColor,
    textColor = TextColor,
    primaryContainerColor = GreenBackground,
    secondaryContainerColor = PinkBackground,
    contrastColor = GreenContrast,
    backgroundCanvas = Color.White,
    mainContrast = MainContrast
)

private val DefaultDark = darkColorScheme(
    primary = MainColorDarker,
    secondary = GreenBackground,
    tertiary = GreenOk,
    background = BackgroundColorDark
)

private val DefaultLight = lightColorScheme(
    primary = MainColor,
    secondary = PinkBackground,
    tertiary = GreenOk,
    background = BackgroundColor
)




@Composable
fun DangoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val colors = if (darkTheme) DefaultDark else DefaultLight

    CompositionLocalProvider (
        CustomColorsPalette provides colorScheme
            ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }
}