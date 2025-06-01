package com.elkhami.f1champions.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by A.Elkhami on 22/05/2025.
 */

data class Dimens(
    val xSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xLarge: Dp = 32.dp,

    val verticalSpace: Dp = 16.dp,

    val f1IconSize: Dp = 72.dp,
    val flagIconSize: Dp = 120.dp,
    val cardCorner: Dp = 16.dp,
    val elevation: Dp = 4.dp,

    val borderWidth: Dp = 1.dp,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }