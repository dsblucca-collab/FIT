package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryPink,
    secondary = SecondaryCyan,
    tertiary = AccentYellow,
    background = CyberBlack,
    surface = CyberDarkSurface,
    surfaceVariant = CyberMediumSurface,
    onPrimary = CyberBlack,
    onSecondary = CyberBlack,
    onBackground = CyberTextPrimary,
    onSurface = CyberTextPrimary,
    onSurfaceVariant = CyberTextSecondary,
    outline = DarkOutline
  )

private val LightColorScheme = DarkColorScheme // Always force Cyberspace aesthetics as requested!

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme for the retro-futuristic grid aesthetics!
  dynamicColor: Boolean = false, // Disable dynamic colors key to guarantee the strict Neon Tokyo primary/secondary branding
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
