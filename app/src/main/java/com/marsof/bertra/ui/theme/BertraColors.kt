package com.marsof.bertra.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BertraColors(
    val primary: Color,
    val secondary : Color,
    val tertiary : Color,
    val additional : Color,
    val greenButton : Color,
    val redButton : Color,
    val blueButton : Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
)

val LightCustomColors = BertraColors(
    primary = Color(0xFFA59D84),
    secondary = Color(0xFFC1BAA1),
    tertiary = Color(0xFFD7D3BF),
    additional = Color(0xFFECEBDE),
    greenButton = Color(0xFF5A9E6F),
    redButton = Color(0xFFD44A4A),
    blueButton = Color(0xFF4A76A8),
    textPrimary = Color(0xFFECEBDE),
    textSecondary = Color(0xFFD7D3BF),
    textTertiary = Color(0xFFFFFFFF),
)

val DarkCustomColors = BertraColors(
    primary = Color(0xFF37353E),
    secondary = Color(0xFF44444E),
    tertiary = Color(0xFF715A5A),
    additional = Color(0xFFD3DAD9),
    greenButton = Color(0xFF4A9E7F),
    redButton = Color(0xFFB5585A),
    blueButton = Color(0xFF4A6FA5),
    textPrimary = Color(0xFFD3DAD9),
    textSecondary = Color(0xFF9AA1A0),
    textTertiary = Color(0xFFFFFFFF),
)

val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }