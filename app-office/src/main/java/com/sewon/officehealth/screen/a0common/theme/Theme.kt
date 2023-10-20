package com.sewon.officehealth.screen.a0common.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController


// Material 3 color schemes
private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  onPrimary = DarkOnPrimary,
  primaryContainer = DarkPrimaryContainer,
  onPrimaryContainer = DarkOnPrimaryContainer,
  inversePrimary = DarkPrimaryInverse,
  secondary = DarkSecondary,
  onSecondary = DarkOnSecondary,
  secondaryContainer = DarkSecondaryContainer,
  onSecondaryContainer = DarkOnSecondaryContainer,
  tertiary = DarkTertiary,
  onTertiary = DarkOnTertiary,
  tertiaryContainer = DarkTertiaryContainer,
  onTertiaryContainer = DarkOnTertiaryContainer,
  error = DarkError,
  onError = DarkOnError,
  errorContainer = DarkErrorContainer,
  onErrorContainer = DarkOnErrorContainer,
  background = DarkBackground,
  onBackground = DarkOnBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  inverseSurface = DarkInverseSurface,
  inverseOnSurface = DarkInverseOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline
)

@Composable
fun OfficeHealthTheme(
  content: @Composable () -> Unit
) {

  val systemUiController = rememberSystemUiController()

//  systemUiController.setNavigationBarColor(
//    color = Color.Red
//  )

  systemUiController.setStatusBarColor(color = DarkBackground)
  systemUiController.setNavigationBarColor(color = DarkSurface)

  androidx.compose.material3.MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = typography,
    shapes = shapes,
    content = content
  )
}
