package com.parking.parkingtop.core.di.navigation

sealed class DeepLinkEvent {
    data class OpenReview(
        val parkingLotId: String,
        val reservationId: String,
        val parkingName: String
    ) : DeepLinkEvent()
}