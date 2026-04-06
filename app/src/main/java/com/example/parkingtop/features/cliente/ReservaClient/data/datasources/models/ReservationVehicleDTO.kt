package com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationVehicleDTO(
    @SerialName("id")           val id: String,
    @SerialName("brand")        val brand: String?,
    @SerialName("model")        val model: String?,
    @SerialName("licensePlate") val licensePlate: String,
    @SerialName("vehicleType")  val vehicleType: String,
    @SerialName("color")        val color: String?,
    @SerialName("isDefault")    val isDefault: Boolean
)


@Serializable
data class ParkingSpotDTO(
    @SerialName("id")          val id: String,
    @SerialName("spotNumber")  val spotNumber: String,
    @SerialName("status")      val status: String,       // available | occupied | reserved | maintenance
    @SerialName("vehicleType") val vehicleType: String,
    @SerialName("floor")       val floor: String? = null,
    @SerialName("section")     val section: String? = null
)

// Response del backend al crear reserva
@Serializable
data class CreateReservationResponseDTO(
    @SerialName("reservation")    val reservation: ReservationDTO,
    @SerialName("payment")        val payment: PaymentDTO,
    @SerialName("paymentUrl")     val paymentUrl: String? = null,   // solo si MercadoPago
    @SerialName("paymentId")      val paymentId: String? = null,
    @SerialName("paymentMethod")  val paymentMethod: String,
    @SerialName("message")        val message: String? = null        // solo si cash
)

@Serializable
data class CreateReservationRequest(
    @SerialName("parkingLotId")  val parkingLotId: String,
    @SerialName("parkingSpotId") val parkingSpotId: String? = null,
    @SerialName("vehicleId")     val vehicleId: String? = null,
    @SerialName("startTime")     val startTime: String,
    @SerialName("endTime")       val endTime: String,
    // ✅ EncodeDefault.ALWAYS fuerza que se incluya aunque sea el valor default
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("paymentMethod") val paymentMethod: String = "mercadopago",
)

@Serializable
data class ReservationDTO(
    @SerialName("id")              val id: String,
    @SerialName("reservationCode") val reservationCode: String,
    @SerialName("status")          val status: String,
    @SerialName("totalCost")       val totalCost: String,
    @SerialName("startTime")       val startTime: String,
    @SerialName("endTime")         val endTime: String
)

@Serializable
data class PaymentDTO(
    @SerialName("id")            val id: String,
    @SerialName("status")        val status: String,
    @SerialName("paymentMethod") val paymentMethod: String,
    @SerialName("amount")        val amount: String
)
