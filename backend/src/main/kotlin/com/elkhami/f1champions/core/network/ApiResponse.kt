package com.elkhami.f1champions.core.network

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()

    data class Error(val message: String, val throwable: Throwable? = null) : ApiResponse<Nothing>()

    companion object {
        fun <T> success(data: T): ApiResponse<T> = Success(data)

        fun error(
            message: String,
            throwable: Throwable? = null,
        ): ApiResponse<Nothing> = Error(message, throwable)
    }
} 
