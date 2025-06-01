package com.elkhami.f1champions.core.network

import retrofit2.HttpException
import java.io.IOException

/**
 * Created by A.Elkhami on 23/05/2025.
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: HttpException) {
        Result.Error(AppError.Http(e.code()), e)
    } catch (e: IOException) {
        Result.Error(AppError.Network, e)
    } catch (e: Exception) {
        Result.Error(AppError.Unknown, e)
    }
}


