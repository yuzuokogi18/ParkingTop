package com.example.parkingtop.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null
)

@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val details: List<ErrorDetail>? = null
)

@Serializable
data class ErrorDetail(
    val field: String,
    val message: String
)

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean,
    val data: List<T>,
    val pagination: PaginationDTO? = null
)

@Serializable
data class PaginationDTO(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
