package com.elkhami.f1champions.core.network

import com.elkhami.f1champions.R
import com.elkhami.f1champions.core.ui.UiText

/**
 * Created by A.Elkhami on 23/05/2025.
 */
sealed class AppError {
    data object Network : AppError()
    data class Http(val code: Int) : AppError()
    data object Unknown : AppError()

    fun toUiText(): UiText = when (this) {
        is Network -> UiText.StringResource(R.string.error_network)
        is Http -> UiText.StringResource(R.string.error_http, code.toString())
        is Unknown -> UiText.StringResource(R.string.error_unknown)
    }
}