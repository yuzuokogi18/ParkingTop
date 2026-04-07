package com.example.parkingtop.core.di.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.screens.BusquedaClientScreen
import com.example.parkingtop.features.cliente.CreateVehicleClient.presentation.screens.CreateVehicleScreen
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens.DetalleEstacionamientoScreen
import com.example.parkingtop.features.cliente.EditVehicleClient.presentation.screens.EditVehicleScreen
import com.example.parkingtop.features.cliente.HomeClient.presentation.screens.HomeClientScreen
import com.example.parkingtop.features.cliente.Notifications.presentation.screens.NotificationsScreen
import com.example.parkingtop.features.cliente.Perfil.presentation.screens.ProfileClientScreen
import com.example.parkingtop.features.cliente.Perfil.presentation.screens.ProfileViewModel
import com.example.parkingtop.features.cliente.updateProfile.presentation.screens.UpdateProfileScreen
import com.example.parkingtop.features.login.presentation.screens.LoginScreen
import com.example.parkingtop.features.login.presentation.screens.WelcomeScreen
import com.example.parkingtop.features.register.presentation.screens.RegisterScreen
import com.example.parkingtop.features.reservations.presentation.screens.ReservationScreen
import com.example.parkingtop.features.subscritionplan.screens.SubscriptionPlanScreen
import com.example.parkingtop.features.propetario.homepropetario.presentation.screens.HomePropetarioScreen
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.screens.CreateParkingScreen
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.screens.UpdateParkingScreen
import com.example.parkingtop.features.propetario.reservationpropetario.presentation.screens.ReservationOwnerScreen
import com.example.parkingtop.features.propetario.horariopropetario.presentation.screens.AvailabilityScreen
import com.example.parkingtop.features.propetario.misespaciospropetario.presentation.screens.MySpacesScreen

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
                    val route = if (role == "owner") AppRoutes.HOME_OWNER else AppRoutes.HOME_CLIENT
                    navController.navigate(route) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.LOGIN) {
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

        composable(AppRoutes.HOME_CLIENT) {
            HomeClientScreen(
                onSearchClick  = { navController.navigate(AppRoutes.BUSQUEDA) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { parking ->
                    navController.navigate(AppRoutes.parkingDetail(parking.name, isOwner = false))
                }
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
                onHomeClick    = { navController.navigate(AppRoutes.HOME_CLIENT) },
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
                onReserveClick = { navController.navigate(AppRoutes.reservaClient(parkingId)) }
            )
        }


        composable(
            route = AppRoutes.RESERVA_CLIENT,
            arguments = listOf(navArgument("parkingIdClient") { type = NavType.StringType })
        ) { backStackEntry ->
            ReservationScreen(
                onBack            = { navController.popBackStack() },
                onPaymentSuccess  = { url -> /* navegar a WebView */ },
                onCashReservation = { navController.popBackStack() }
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
    }
}
