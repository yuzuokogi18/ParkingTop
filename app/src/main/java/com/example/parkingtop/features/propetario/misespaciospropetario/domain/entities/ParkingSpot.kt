package com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities

data class ParkingSpot(
    val id: String,
    val parkingLotId: String,
    val spotNumber: String,
    val status: SpotStatus,
    val vehicleType: String,
    val floor: String?,
    val section: String?,
    val updatedAt: String
)

enum class SpotStatus(val value: String) {
    AVAILABLE("available"),
    OCCUPIED("occupied"),
    RESERVED("reserved"),
    MAINTENANCE("maintenance"),
    UNKNOWN("unknown");

    companion object {
        fun fromString(status: String): SpotStatus {
            return entries.find { it.value == status } ?: UNKNOWN
        }
    }
}
