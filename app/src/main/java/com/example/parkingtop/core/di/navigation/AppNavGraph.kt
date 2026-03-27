package com.example.parkingtop.core.di.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import com.example.parkingtop.features.login.presentation.screens.LoginScreen
import com.example.parkingtop.features.register.presentation.screens.RegisterScreen
import com.example.parkingtop.features.login.presentation.screens.WelcomeScreen
import com.example.parkingtop.features.subscritionplan.screens.SubscriptionPlanScreen
import com.example.parkingtop.features.cliente.HomeClient.presentation.screens.HomeClientScreen
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.screens.BusquedaClientScreen
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens.DetalleEstacionamientoScreen
import com.example.parkingtop.features.cliente.ReservaClient.presentation.screens.ReservaClientScreen
import com.example.parkingtop.features.cliente.Perfil.presentation.screens.ProfileClientScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.WELCOME
    ) {
        composable(AppRoutes.WELCOME) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(AppRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    // Si es propietario (owner), va a suscripciones. Si es cliente (customer), va directo a Home.
                    if (role == "owner") {
                        navController.navigate(AppRoutes.SUBSCRIPTION) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(AppRoutes.HOME_CLIENT) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
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
                onBackClick = { navController.popBackStack() },
                onUpgradeClick = { navController.navigate(AppRoutes.HOME_CLIENT) }
            )
        }

        composable(AppRoutes.HOME_CLIENT) {
            HomeClientScreen(
                onSearchClick = { navController.navigate(AppRoutes.BUSQUEDA) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) },
                onParkingClick = { navController.navigate(AppRoutes.DETALLE_ESTACIONAMIENTO) }
            )
        }

        composable(AppRoutes.BUSQUEDA) {
            BusquedaClientScreen(
                onHomeClick = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onProfileClick = { navController.navigate(AppRoutes.PERFIL) }
            )
        }

        composable(AppRoutes.DETALLE_ESTACIONAMIENTO) {
            DetalleEstacionamientoScreen(
                onBackClick = { navController.popBackStack() },
                onReserveClick = { navController.navigate(AppRoutes.RESERVA) }
            )
        }

        composable(AppRoutes.RESERVA) {
            ReservaClientScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.PERFIL) {
            ProfileClientScreen(
                onHomeClick = { navController.navigate(AppRoutes.HOME_CLIENT) },
                onSearchClick = { navController.navigate(AppRoutes.BUSQUEDA) }
            )
        }
    }
}
