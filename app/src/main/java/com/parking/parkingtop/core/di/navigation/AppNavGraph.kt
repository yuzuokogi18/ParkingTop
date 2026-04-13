package com.parking.parkingtop.core.di.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.parking.parkingtop.features.cliente.BusquedaClient.presentation.screens.BusquedaClientScreen
import com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.screens.CreateVehicleScreen
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens.DetalleEstacionamientoScreen
import com.parking.parkingtop.features.cliente.EditVehicleClient.presentation.screens.EditVehicleScreen
import com.parking.parkingtop.features.cliente.Perfil.presentation.screens.ProfileClientScreen
import com.parking.parkingtop.features.cliente.Perfil.presentation.screens.ProfileViewModel
import com.parking.parkingtop.features.cliente.Reviews.presentation.screens.ReviewScreen
import com.parking.parkingtop.features.cliente.updateProfile.presentation.screens.UpdateProfileScreen
import com.parking.parkingtop.features.login.presentation.screens.LoginScreen
import com.parking.parkingtop.features.login.presentation.screens.WelcomeScreen
import com.parking.parkingtop.features.register.presentation.screens.RegisterScreen
import com.example.parkingtop.features.reservations.presentation.screens.ReservationScreen
import com.parking.parkingtop.features.cliente.ReservaClient.presentation.screens.PaymentWebViewScreen
import com.parking.parkingtop.features.notifications.presentation.screens.NotificationsScreen
import com.parking.parkingtop.features.subscritionplan.screens.SubscriptionPlanScreen
import com.parking.parkingtop.features.propetario.homepropetario.presentation.screens.HomePropetarioScreen
import com.parking.parkingtop.features.propetario.crearestacionamiento.presentation.screens.CreateParkingScreen
import com.parking.parkingtop.features.propetario.crearestacionamiento.presentation.screens.UpdateParkingScreen
import com.parking.parkingtop.features.propetario.reservationpropetario.presentation.screens.ReservationOwnerScreen
import com.parking.parkingtop.features.propetario.horariopropetario.presentation.screens.AvailabilityScreen
import com.parking.parkingtop.features.propetario.misespaciospropetario.presentation.screens.MySpacesScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.WELCOME) {

        composable(AppRoutes.WELCOME) {
            WelcomeScreen(
                onLoginClick    = { navController.navigate(AppRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onSubscription  = { navController.navigate(AppRoutes.SUBSCRIPTION) },
                onHome          = { role -> 
                    val route = if (role == "owner") AppRoutes.HOME_OWNER else AppRoutes.BUSQUEDA
                    navController.navigate(route) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
            )
        }
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onSubscription = {
                    // Owner → va a elegir plan de suscripción
                    navController.navigate(AppRoutes.SUBSCRIPTION) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                    }
                },
                onHome = {
                    // Customer → va directo al home
                    navController.navigate(AppRoutes.BUSQUEDA) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(AppRoutes.LOGIN) }
            )
        }

        composable(AppRoutes.SUBSCRIPTION) {
            SubscriptionPlanScreen(
                onBackClick    = { navController.popBackStack() },
                onUpgradeClick = { navController.navigate(AppRoutes.HOME_OWNER) }
            )
        }

        composable(AppRoutes.HOME_OWNER) {
            HomePropetarioScreen(
                onAddParkingClick = { navController.navigate(AppRoutes.CREATE_PARKING) },
                onParkingClick = { parkingId ->
                    navController.navigate(AppRoutes.parkingDetail(parkingId, isOwner = true))
                },
                onEditParkingClick = { parkingId ->
                    navController.navigate(AppRoutes.editParking(parkingId))
                },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onMySpacesClick = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable(AppRoutes.CREATE_PARKING) {
            CreateParkingScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.EDIT_PARKING,
            arguments = listOf(navArgument("parkingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""
            UpdateParkingScreen(
                parkingId = parkingId,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.BUSQUEDA) {
            BusquedaClientScreen(
                onHomeClick    = { /* ya estás aquí */ },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { parkingId ->
                    navController.navigate(AppRoutes.parkingDetail(parkingId, isOwner = false))
                }
            )
        }

        composable(
            route = AppRoutes.DETALLE_ESTACIONAMIENTO,
            arguments = listOf(
                navArgument("parkingId") { type = NavType.StringType },
                navArgument("isOwner")   { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""
            val isOwner   = backStackEntry.arguments?.getBoolean("isOwner") ?: false
            DetalleEstacionamientoScreen(
                parkingId      = parkingId,
                isOwner        = isOwner,
                onBackClick    = { navController.popBackStack() },
                // ✅ Usa AppRoutes.reserva() que genera "reserva/{parkingId}"
                onReserveClick = { navController.navigate(AppRoutes.reserva(parkingId)) }
            )
        }


        composable(
            route = AppRoutes.RESERVA,   // "reserva/{parkingIdClient}"
            arguments = listOf(
                navArgument("parkingIdClient") { type = NavType.StringType }
            )
        ) {
            ReservationScreen(
                onBack = { navController.popBackStack() },
                onPaymentSuccess = { paymentUrl ->
                    navController.navigate(AppRoutes.paymentWebView(paymentUrl))
                },
                onCashReservation = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                }
            )
        }

        composable(AppRoutes.PERFIL) { backStackEntry ->

            val profileViewModel: ProfileViewModel = hiltViewModel(backStackEntry)

            val refreshKey = backStackEntry.savedStateHandle
                .get<Boolean>("profile_needs_refresh")

            LaunchedEffect(refreshKey) {
                if (refreshKey == true) {
                    profileViewModel.loadProfileData()
                    backStackEntry.savedStateHandle
                        .remove<Boolean>("profile_needs_refresh")
                }
            }

            ProfileClientScreen(
                onHomeClick          = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onSearchClick        = { navController.navigate(AppRoutes.BUSQUEDA) },
                onAddVehicleClick    = { navController.navigate(AppRoutes.CREATE_VEHICLE) },
                onEditVehicleClick   = { vehicle ->
                    navController.navigate(
                        AppRoutes.editVehicle(
                            vehicleId    = vehicle.id,
                            licensePlate = vehicle.licensePlate,
                            brand        = vehicle.brand ?: "",
                            model        = vehicle.model ?: "",
                            color        = vehicle.color ?: "",
                            isDefault    = vehicle.isDefault
                        )
                    )
                },
                onEditProfileClick   = { name, phone, imageUrl ->
                    navController.navigate(AppRoutes.updateProfile(name, phone, imageUrl))
                },
                onNotificationsClick = { navController.navigate(AppRoutes.NOTIFICATIONS) },
                // ✅ Configuración para PROPIETARIO desde su perfil
                onDashboardClick     = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick  = { navController.navigate("owner_reservations") },
                onAvailabilityClick  = { navController.navigate("owner_availability") },
                onMySpacesClick      = { navController.navigate(AppRoutes.MY_SPACES) },
                onTransferClick      = { navController.navigate(AppRoutes.REQUEST_PAYOUT) },
                onPayoutHistoryClick = { navController.navigate(AppRoutes.PAYOUT_HISTORY) },
                viewModel            = profileViewModel
            )
        }

        composable(AppRoutes.CREATE_VEHICLE) {
            CreateVehicleScreen(
                onBack    = { navController.popBackStack() },
                onSuccess = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("profile_needs_refresh", true)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EDIT_VEHICLE,
            arguments = listOf(
                navArgument("vehicleId")    { type = NavType.StringType },
                navArgument("licensePlate") { type = NavType.StringType },
                navArgument("brand")        { type = NavType.StringType },
                navArgument("model")        { type = NavType.StringType },
                navArgument("color")        { type = NavType.StringType },
                navArgument("isDefault")    { type = NavType.BoolType  }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            EditVehicleScreen(
                vehicleId    = args.getString("vehicleId")!!,
                licensePlate = args.getString("licensePlate")!!,
                brand        = args.getString("brand")!!,
                model        = args.getString("model")!!,
                color        = args.getString("color")!!,
                isDefault    = args.getBoolean("isDefault"),
                onBack       = { navController.popBackStack() },
                onSuccess    = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("profile_needs_refresh", true)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.UPDATE_PROFILE,
            arguments = listOf(
                navArgument("name")     { type = NavType.StringType },
                navArgument("phone")    { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            UpdateProfileScreen(
                currentName  = Uri.decode(args.getString("name")!!),
                currentPhone = Uri.decode(args.getString("phone")!!).ifBlank { null },
                currentImage = Uri.decode(args.getString("imageUrl")!!).ifBlank { null },
                onBack       = { navController.popBackStack() },
                onSuccess    = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("profile_needs_refresh", true)
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("owner_reservations") {
            ReservationOwnerScreen(
                onDashboardClick = { navController.navigate(AppRoutes.HOME_OWNER) },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onMySpacesClick = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) }
            )
        }
        
        composable("owner_availability") {
            AvailabilityScreen(
                onDashboardClick = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onMySpacesClick = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable(AppRoutes.MY_SPACES) {
            MySpacesScreen(
                onDashboardClick = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onSpotClick = { /* TODO: Navegar al detalle del espacio */ }
            )
        }
        
        composable(AppRoutes.PAYOUT_HISTORY) {
            // TODO: Implementar pantalla de historial de pagos
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Historial de Ganancias (Próximamente)")
            }
        }
        
        composable(AppRoutes.REQUEST_PAYOUT) {
            // TODO: Implementar pantalla de solicitud de retiro
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Transferir a mi Cuenta (Próximamente)")
            }
        }

        composable(
            route = AppRoutes.REVIEW,
            arguments = listOf(
                navArgument("parkingLotId")  { type = NavType.StringType },
                navArgument("reservationId") { type = NavType.StringType },
                navArgument("parkingName")   { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            ReviewScreen(
                parkingLotId  = args.getString("parkingLotId")!!,
                reservationId = args.getString("reservationId")!!,
                parkingName   = Uri.decode(args.getString("parkingName")!!),
                onSuccess     = {
                    // Vuelve al home después de calificar
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                },
                onSkip = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = AppRoutes.PAYMENT_WEBVIEW,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
            PaymentWebViewScreen(
                paymentUrl       = url,
                onPaymentSuccess = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                },
                onPaymentFailure = {
                    navController.popBackStack()
                    navController.popBackStack() // sale también del ReservationScreen
                },
                onPaymentPending = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("subscription/{status}") { backStackEntry ->
            val status = backStackEntry.arguments?.getString("status") ?: "success"
            LaunchedEffect(Unit) {
                if (status == "approved" || status == "success") {
                    navController.navigate(AppRoutes.HOME_OWNER) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppRoutes.SUBSCRIPTION) {
                        popUpTo(AppRoutes.SUBSCRIPTION) { inclusive = false }
                    }
                }
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
