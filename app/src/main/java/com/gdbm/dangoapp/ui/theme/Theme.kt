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
    primary = MainColorDarker,//Purple40,
    secondary = GreenBackground,
    tertiary = GreenOk,
    background = BackgroundColorDark,
    textColor = Color.White,
    primaryContainerColor = GreenBackground,
    secondaryContainerColor = PinkBackground,
    contrastColor = GreenContrast,
    backgroundCanvas = Black

)

private val LightColorScheme = CustomPalette(
    primary = MainColor,//Purple40,
    secondary = PinkBackground,
    tertiary = GreenOk,
    background = BackgroundColor,
    textColor = TextColor,
    primaryContainerColor = GreenBackground,
    secondaryContainerColor = PinkBackground,
    contrastColor = GreenContrast,
    backgroundCanvas = Color.White

)

private val DefaultDark = darkColorScheme(
    primary = MainColorDarker,//Purple40,
    secondary = GreenBackground,
    tertiary = GreenOk,
    background = BackgroundColorDark
)

private val DefaultLight = lightColorScheme(
    primary = MainColor,//Purple40,
    secondary = PinkBackground,
    tertiary = GreenOk,
    background = BackgroundColor

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/*private val DarkColorSchemea = darkColorScheme(
    primary = MainColorDarker,//Purple40,
    secondary = GreenBackground,
    tertiary = GreenOk,
    background = BackgroundColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = MainColor,//Purple40,
    secondary = PinkBackground,
    tertiary = GreenOk,
    background = BackgroundColor

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)*/




@Composable
fun JapaneseTrainerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

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