package com.example.parkingtop.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
    val meta: MetaDto? = null
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
data class MetaDto(
    val page: Int? = null,
    val perPage: Int? = null,
    val total: Int? = null,
    val totalPages: Int? = null
)

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean,
    val data: List<T>,
    val pagination: PaginationDto? = null
)

@Serializable
data class PaginationDto(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPages: Int
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RequestPayoutBody(
    val amount: Double,
    val bankAccount: String? = null,
    val accountHolder: String? = null,
    val notes: String? = null
)

@Serializable
data class RejectPayoutBody(
    val reason: String
)

@Serializable
data class PayoutListData(
    val payouts: List<PayoutDto>,
    val pagination: PaginationDto
)

@Serializable
data class PayoutDto(
    val id: String,
    val userId: String,
    val amount: String,
    val status: String,
    val requestedAt: String,
    val processedAt: String? = null,
    val completedAt: String? = null,
    val rejectedAt: String? = null,
    val rejectionReason: String? = null,
    val mpTransferId: String? = null,
    val bankAccount: String? = null,
    val accountHolder: String? = null,
    val notes: String? = null,
    val approvedBy: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class OwnerBalanceDto(
    val id: String,
    val userId: String,
    val availableBalance: String,
    val pendingBalance: String,
    val totalEarnings: String,
    val totalWithdrawn: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class PayoutStatsDto(
    val byStatus: Map<String, Int>,
    val totalCompleted: String,
    val totalPending: Int,
    val totalApproved: Int,
    val totalRejected: Int
)

@Serializable
data class BalanceStatsDto(
    val totalOwners: Int? = null,
    val totalAvailable: String? = null,
    val totalPending: String? = null,
    val totalWithdrawn: String? = null
)

@Serializable
data class TopEarnerDto(
    val userId: String? = null,
    val fullName: String? = null,
    val totalEarnings: String? = null
)
