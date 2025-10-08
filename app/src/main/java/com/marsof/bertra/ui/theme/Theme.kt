package com.marsof.bertra.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
//    primary = PrimaryDark,
//    secondary = SecondaryDark,
//    tertiary = TertiaryDark,
//    onTertiary = onTertiaryBlack,
//    surface = AdditionalDark,
//    outlineVariant = AdditionalDark,
//    primaryContainer = PrimaryDark,
//    onPrimaryContainer = AdditionalDark,
)

private val LightColorScheme = lightColorScheme(
//    primary = PrimaryLight,
//    secondary = SecondaryLight,
//    tertiary = TertiaryLight,
//    onTertiary = onTertiaryLight,
//    surface = AdditionalLight,
//    primaryContainer = AdditionalLight,
//    onPrimaryContainer = onTertiaryLight,
)

@Composable
fun BertraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}