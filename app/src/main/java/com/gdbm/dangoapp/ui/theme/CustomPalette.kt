package com.gdbm.dangoapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomPalette(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val backgroundCanvas: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val primaryContainerColor: Color = Color.Unspecified,
    val secondaryContainerColor: Color = Color.Unspecified,
    val contrastColor: Color = Color.Unspecified,
    val mainContrast:Color = Color.Unspecified
)

val CustomColorsPalette = staticCompositionLocalOf { CustomPalette() }