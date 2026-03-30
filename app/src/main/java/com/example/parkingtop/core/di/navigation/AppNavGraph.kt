package com.example.parkingtop.core.di.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
                onHome          = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onSubscription = { navController.navigate(AppRoutes.SUBSCRIPTION) },
                onHome         = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onLoginClick   = { navController.navigate(AppRoutes.LOGIN) }
            )
        }

        composable(AppRoutes.SUBSCRIPTION) {
            SubscriptionPlanScreen(
                onBackClick    = { navController.popBackStack() },
                onUpgradeClick = { navController.navigate(AppRoutes.HOME_CLIENT) }
            )
        }

        composable(AppRoutes.HOME_CLIENT) {
            HomeClientScreen(
                onSearchClick  = { navController.navigate(AppRoutes.BUSQUEDA) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { navController.navigate(AppRoutes.DETALLE_ESTACIONAMIENTO) }
            )
        }

        composable(AppRoutes.BUSQUEDA) {
            BusquedaClientScreen(
                onHomeClick    = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { parkingId ->
                    navController.navigate(AppRoutes.parkingDetail(parkingId))
                }
            )
        }

        composable(route = AppRoutes.DETALLE_ESTACIONAMIENTO) { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""
            DetalleEstacionamientoScreen(
                parkingId      = parkingId,
                onBackClick    = { navController.popBackStack() },
                onReserveClick = { navController.navigate(AppRoutes.RESERVA) }
            )
        }

        composable(AppRoutes.RESERVA) {
            ReservationScreen(onBack = { navController.popBackStack() })
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
    }
}