package com.elkhami.f1champions.core.exception

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = loggerWithPrefix()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidationException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.warn("Validation error: ${ex.message}")
        return ResponseEntity.badRequest()
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.BAD_REQUEST.value(),
                    error = "Validation Error",
                    message = ex.message ?: "Invalid input data",
                ),
            )
    }

    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientException(ex: WebClientResponseException): ResponseEntity<ErrorResponse> {
        logger.error("External API error: ${ex.message}")
        return ResponseEntity.status(ex.statusCode)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = ex.statusCode.value(),
                    error = "External API Error",
                    message = "Failed to fetch data from external service",
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error: ${ex.message}")
        return ResponseEntity.internalServerError()
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    error = "Internal Server Error",
                    message = "An unexpected error occurred",
                ),
            )
    }
} 
