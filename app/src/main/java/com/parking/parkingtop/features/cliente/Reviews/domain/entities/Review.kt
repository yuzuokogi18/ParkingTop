package com.parking.parkingtop.features.cliente.Reviews.domain.entities

data class Review(
    val id: String,
    val parkingLotId: String,
    val reservationId: String,
    val rating: Int,
    val comment: String?,
    val ownerResponse: String?,
    val createdAt: String,
    val parkingName: String
)