package com.parking.parkingtop.features.cliente.Reviews.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewRequest(
    @SerialName("parkingLotId")  val parkingLotId: String,
    @SerialName("reservationId") val reservationId: String,
    @SerialName("rating")        val rating: Int,
    @SerialName("comment")       val comment: String? = null
)

@Serializable
data class ReviewDTO(
    @SerialName("id")              val id: String,
    @SerialName("userId")          val userId: String,
    @SerialName("parkingLotId")    val parkingLotId: String,
    @SerialName("reservationId")   val reservationId: String,
    @SerialName("rating")          val rating: Int,
    @SerialName("comment")         val comment: String? = null,
    @SerialName("ownerResponse")   val ownerResponse: String? = null,
    @SerialName("createdAt")       val createdAt: String,
    @SerialName("user")            val user: ReviewUserDTO? = null,
    @SerialName("parkingLot")      val parkingLot: ReviewParkingLotDTO? = null
)

@Serializable
data class ReviewUserDTO(
    @SerialName("id")       val id: String,
    @SerialName("fullName") val fullName: String
)

@Serializable
data class ReviewParkingLotDTO(
    @SerialName("id")      val id: String,
    @SerialName("name")    val name: String,
    @SerialName("address") val address: String? = null,
    @SerialName("city")    val city: String? = null
)