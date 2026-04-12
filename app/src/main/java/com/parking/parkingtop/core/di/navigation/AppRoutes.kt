package com.parking.parkingtop.core.di.navigation

import android.net.Uri

object AppRoutes {
    const val WELCOME                 = "welcome"
    const val LOGIN                   = "login"
    const val REGISTER                = "register"
    const val SUBSCRIPTION            = "subscription"
    const val HOME_CLIENT             = "home_client"
    const val HOME_OWNER              = "home_owner"
    const val MY_SPACES               = "my_spaces"
    const val CREATE_PARKING          = "create_parking?isFirst={isFirst}"
    const val EDIT_PARKING            = "edit_parking/{parkingId}"
    const val BUSQUEDA                = "busqueda"
    const val DETALLE_ESTACIONAMIENTO = "detalle_estacionamiento/{parkingId}?isOwner={isOwner}"

    // ✅ Solo una ruta de reserva con el argumento en el path
    const val RESERVA = "reserva/{parkingIdClient}"

    const val PERFIL                  = "perfil"
    const val CREATE_VEHICLE          = "create_vehicle"
    const val EDIT_VEHICLE            = "edit_vehicle/{vehicleId}/{licensePlate}/{brand}/{model}/{color}/{isDefault}"
    const val UPDATE_PROFILE          = "update_profile/{name}/{phone}/{imageUrl}"
    const val NOTIFICATIONS           = "notifications"
    const val PAYOUT_HISTORY          = "payout_history"
    const val REQUEST_PAYOUT          = "request_payout"
    const val PAYMENT_WEBVIEW         = "payment_webview/{url}"
    const val REVIEW                  = "review/{parkingLotId}/{reservationId}/{parkingName}"

    // ✅ Función helper para navegar a la reserva
    fun reserva(parkingId: String) = "reserva/$parkingId"

    fun review(
        parkingLotId: String,
        reservationId: String,
        parkingName: String
    ) = "review/$parkingLotId/$reservationId/${Uri.encode(parkingName)}"

    fun createParking(isFirst: Boolean = false) = "create_parking?isFirst=$isFirst"

    fun parkingDetail(parkingId: String, isOwner: Boolean = false) =
        "detalle_estacionamiento/$parkingId?isOwner=$isOwner"

    fun editParking(parkingId: String) = "edit_parking/$parkingId"

    fun editVehicle(
        vehicleId: String, licensePlate: String, brand: String,
        model: String, color: String, isDefault: Boolean
    ) = "edit_vehicle/$vehicleId/$licensePlate/$brand/$model/$color/$isDefault"

    fun updateProfile(name: String, phone: String, imageUrl: String) =
        "update_profile/${Uri.encode(name)}/${Uri.encode(phone)}/${Uri.encode(imageUrl)}"

    fun paymentWebView(url: String) = "payment_webview/${Uri.encode(url)}"
}