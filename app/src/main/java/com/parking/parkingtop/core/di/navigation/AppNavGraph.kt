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
import com.parking.parkingtop.core.di.navigation.viewmodels.DeepLinkHandlerViewModel
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

    // Obtenemos el handler a través del ViewModel delgado (Hilt-safe en Compose)
    val deepLinkVm: DeepLinkHandlerViewModel = hiltViewModel()

    // Colectamos eventos de deep-link.
    // LaunchedEffect(navController) se re-lanza si el NavController cambia,
    // pero en la práctica es estable durante toda la sesión.
    LaunchedEffect(navController) {
        deepLinkVm.handler.events.collect { event ->
            when (event) {
                is DeepLinkEvent.OpenReview -> {
                    navController.navigate(
                        AppRoutes.review(
                            parkingLotId  = event.parkingLotId,
                            reservationId = event.reservationId,
                            parkingName   = event.parkingName
                        )
                    )
                    deepLinkVm.handler.clearLastEvent() // ← nuevo método
                }
            }
        }
    }

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
                    navController.navigate(AppRoutes.SUBSCRIPTION) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                    }
                },
                onHome = {
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
                onAddParkingClick   = { navController.navigate(AppRoutes.CREATE_PARKING) },
                onParkingClick      = { id -> navController.navigate(AppRoutes.parkingDetail(id, true)) },
                onEditParkingClick  = { id -> navController.navigate(AppRoutes.editParking(id)) },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onMySpacesClick     = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick      = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable(AppRoutes.CREATE_PARKING) {
            CreateParkingScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess   = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.EDIT_PARKING,
            arguments = listOf(navArgument("parkingId") { type = NavType.StringType })
        ) {
            UpdateParkingScreen(
                parkingId   = it.arguments?.getString("parkingId") ?: "",
                onBackClick = { navController.popBackStack() },
                onSuccess   = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.BUSQUEDA) {
            BusquedaClientScreen(
                onHomeClick    = { },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { id -> navController.navigate(AppRoutes.parkingDetail(id, false)) }
            )
        }

        composable(
            route = AppRoutes.DETALLE_ESTACIONAMIENTO,
            arguments = listOf(
                navArgument("parkingId") { type = NavType.StringType },
                navArgument("isOwner")   { type = NavType.BoolType; defaultValue = false }
            )
        ) { back ->
            val parkingId = back.arguments?.getString("parkingId") ?: ""
            DetalleEstacionamientoScreen(
                parkingId      = parkingId,
                isOwner        = back.arguments?.getBoolean("isOwner") ?: false,
                onBackClick    = { navController.popBackStack() },
                onReserveClick = { navController.navigate(AppRoutes.reserva(parkingId)) }
            )
        }

        composable(
            route = AppRoutes.RESERVA,
            arguments = listOf(navArgument("parkingIdClient") { type = NavType.StringType })
        ) {
            ReservationScreen(
                onBack            = { navController.popBackStack() },
                onPaymentSuccess  = { url -> navController.navigate(AppRoutes.paymentWebView(url)) },
                onCashReservation = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                }
            )
        }

        composable(AppRoutes.PERFIL) { back ->
            val vm: ProfileViewModel = hiltViewModel(back)
            val shouldRefresh = back.savedStateHandle.get<Boolean>("profile_needs_refresh")

            LaunchedEffect(shouldRefresh) {
                if (shouldRefresh == true) {
                    vm.loadProfileData()
                    back.savedStateHandle.remove<Boolean>("profile_needs_refresh")
                }
            }
            ProfileClientScreen(
                onHomeClick          = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onSearchClick        = { navController.navigate(AppRoutes.BUSQUEDA) },
                onAddVehicleClick    = { navController.navigate(AppRoutes.CREATE_VEHICLE) },
                onEditVehicleClick   = { v ->
                    navController.navigate(
                        AppRoutes.editVehicle(v.id, v.licensePlate, v.brand ?: "", v.model ?: "", v.color ?: "", v.isDefault)
                    )
                },
                onEditProfileClick   = { n, p, i -> navController.navigate(AppRoutes.updateProfile(n, p, i)) },
                onNotificationsClick = { navController.navigate(AppRoutes.NOTIFICATIONS) },
                onDashboardClick     = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick  = { navController.navigate("owner_reservations") },
                onAvailabilityClick  = { navController.navigate("owner_availability") },
                onMySpacesClick      = { navController.navigate(AppRoutes.MY_SPACES) },
                onTransferClick      = { navController.navigate(AppRoutes.REQUEST_PAYOUT) },
                onPayoutHistoryClick = { navController.navigate(AppRoutes.PAYOUT_HISTORY) },
                viewModel            = vm
            )
        }

        composable(AppRoutes.CREATE_VEHICLE) {
            CreateVehicleScreen(
                onBack    = { navController.popBackStack() },
                onSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("profile_needs_refresh", true)
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
        ) { back ->
            val a = back.arguments!!
            EditVehicleScreen(
                vehicleId    = a.getString("vehicleId")!!,
                licensePlate = a.getString("licensePlate")!!,
                brand        = a.getString("brand")!!,
                model        = a.getString("model")!!,
                color        = a.getString("color")!!,
                isDefault    = a.getBoolean("isDefault"),
                onBack       = { navController.popBackStack() },
                onSuccess    = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("profile_needs_refresh", true)
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
        ) { back ->
            val a = back.arguments!!
            UpdateProfileScreen(
                currentName  = Uri.decode(a.getString("name")!!),
                currentPhone = Uri.decode(a.getString("phone")!!).ifBlank { null },
                currentImage = Uri.decode(a.getString("imageUrl")!!).ifBlank { null },
                onBack       = { navController.popBackStack() },
                onSuccess    = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("profile_needs_refresh", true)
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable("owner_reservations") {
            ReservationOwnerScreen(
                onDashboardClick    = { navController.navigate(AppRoutes.HOME_OWNER) },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onMySpacesClick     = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick      = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable("owner_availability") {
            AvailabilityScreen(
                onDashboardClick    = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onMySpacesClick     = { navController.navigate(AppRoutes.MY_SPACES) },
                onProfileClick      = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable(AppRoutes.MY_SPACES) {
            MySpacesScreen(
                onDashboardClick    = { navController.navigate(AppRoutes.HOME_OWNER) },
                onReservationsClick = { navController.navigate("owner_reservations") },
                onAvailabilityClick = { navController.navigate("owner_availability") },
                onProfileClick      = { navController.navigate(AppRoutes.PERFIL) },
                onSpotClick         = { }
            )
        }

        composable(AppRoutes.PAYOUT_HISTORY) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Historial de Ganancias (Próximamente)")
            }
        }

        composable(AppRoutes.REQUEST_PAYOUT) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Transferir a mi Cuenta (Próximamente)")
            }
        }

        // ── Review ────────────────────────────────────────────────────────────
        composable(
            route = AppRoutes.REVIEW,
            arguments = listOf(
                navArgument("parkingLotId")  { type = NavType.StringType },
                navArgument("reservationId") { type = NavType.StringType },
                navArgument("parkingName")   { type = NavType.StringType }
            )
        ) { back ->
            val parkingLotId  = back.arguments?.getString("parkingLotId").orEmpty()
            val reservationId = back.arguments?.getString("reservationId").orEmpty()
            val parkingName   = Uri.decode(back.arguments?.getString("parkingName").orEmpty())

            if (parkingLotId.isBlank() || reservationId.isBlank()) {
                LaunchedEffect(Unit) {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                }
                return@composable
            }

            ReviewScreen(
                parkingLotId  = parkingLotId,
                reservationId = reservationId,
                parkingName   = parkingName,
                onSuccess     = {
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
        ) { back ->
            val url = Uri.decode(back.arguments?.getString("url") ?: "")
            PaymentWebViewScreen(
                paymentUrl       = url,
                onPaymentSuccess = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                },
                onPaymentFailure = { navController.popBackStack(); navController.popBackStack() },
                onPaymentPending = {
                    navController.navigate(AppRoutes.HOME_CLIENT) {
                        popUpTo(AppRoutes.HOME_CLIENT) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("subscription/{status}") { back ->
            val status = back.arguments?.getString("status") ?: "success"
            LaunchedEffect(Unit) {
                if (status == "approved" || status == "success") {
                    navController.navigate(AppRoutes.HOME_OWNER) { popUpTo(0) { inclusive = true } }
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