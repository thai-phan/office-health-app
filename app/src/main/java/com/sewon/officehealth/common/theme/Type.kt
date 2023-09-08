
package com.sewon.officehealth.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sewon.officehealth.R

private val fonts = FontFamily(
    Font(R.font.pretendard_regular),
)

val typography = typographyFromDefaults(
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

fun typographyFromDefaults(
    headlineLarge: TextStyle?,
    headlineMedium: TextStyle?,
    headlineSmall: TextStyle?,
    titleLarge: TextStyle?,
    titleMedium: TextStyle?,
    titleSmall: TextStyle?,
    bodyLarge: TextStyle?,
    bodyMedium: TextStyle?,
    bodySmall: TextStyle?,
    labelLarge: TextStyle?,
    labelMedium: TextStyle?,
    labelSmall: TextStyle?,

): Typography {
    val defaults = Typography()
    return Typography(
        headlineLarge = defaults.headlineLarge.merge(headlineLarge),
        headlineMedium = defaults.headlineMedium.merge(headlineMedium),
        headlineSmall = defaults.headlineSmall.merge(headlineSmall),
        titleLarge = defaults.titleLarge.merge(titleLarge),
        titleMedium = defaults.titleMedium.merge(titleMedium),
        titleSmall = defaults.titleSmall.merge(titleSmall),
        bodyLarge = defaults.bodyLarge.merge(bodyLarge),
        bodyMedium = defaults.bodyMedium.merge(bodyMedium),
        bodySmall = defaults.bodySmall.merge(bodySmall),
        labelLarge = defaults.labelLarge.merge(labelLarge),
        labelMedium = defaults.labelMedium.merge(labelMedium),
        labelSmall = defaults.labelSmall.merge(labelSmall),
    )
}

