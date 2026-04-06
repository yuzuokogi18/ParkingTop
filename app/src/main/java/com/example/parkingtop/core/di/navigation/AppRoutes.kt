package com.example.parkingtop.core.di.navigation

object AppRoutes {
    const val WELCOME                 = "welcome"
    const val LOGIN                   = "login"
    const val REGISTER                = "register"
    const val SUBSCRIPTION            = "subscription"
    const val HOME_CLIENT             = "home_client"
    const val HOME_OWNER              = "home_owner"
    const val MY_SPACES               = "my_spaces"
    const val CREATE_PARKING          = "create_parking"
    const val EDIT_PARKING            = "edit_parking/{parkingId}"
    const val BUSQUEDA                = "busqueda"
    const val DETALLE_ESTACIONAMIENTO = "detalle_estacionamiento/{parkingId}?isOwner={isOwner}"
    const val RESERVA                 = "reserva"
    const val PERFIL                  = "perfil"
    const val CREATE_VEHICLE          = "create_vehicle"
    const val EDIT_VEHICLE            = "edit_vehicle/{vehicleId}/{licensePlate}/{brand}/{model}/{color}/{isDefault}"
    const val UPDATE_PROFILE          = "update_profile/{name}/{phone}/{imageUrl}"
    const val NOTIFICATIONS           = "notifications"

    fun parkingDetail(parkingId: String, isOwner: Boolean = false) = 
        "detalle_estacionamiento/$parkingId?isOwner=$isOwner"

    fun editParking(parkingId: String) = "edit_parking/$parkingId"

    fun editVehicle(
        vehicleId: String, licensePlate: String, brand: String,
        model: String, color: String, isDefault: Boolean
    ) = "edit_vehicle/$vehicleId/$licensePlate/$brand/$model/$color/$isDefault"

    fun updateProfile(name: String, phone: String, imageUrl: String) =
        "update_profile/${android.net.Uri.encode(name)}/${android.net.Uri.encode(phone)}/${android.net.Uri.encode(imageUrl)}"
}
