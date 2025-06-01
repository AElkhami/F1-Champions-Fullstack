package com.elkhami.f1champions.core.network

/**
 * Created by A.Elkhami on 23/05/2025.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: AppError?, val throwable: Throwable? = null) : Result<Nothing>()
}