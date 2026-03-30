package com.example.parkingtop.features.cliente.Perfil.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingtop.features.cliente.Perfil.presentation.components.ProfileHeaderCard
import com.example.parkingtop.features.cliente.Perfil.presentation.components.ReservationProfileCard
import com.example.parkingtop.features.cliente.Perfil.presentation.components.VehicleCard
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileClientScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAddVehicleClick: () -> Unit = {},
    onEditVehicleClick: (Vehicle) -> Unit = {},
    onEditProfileClick: (name: String, phone: String, imageUrl: String) -> Unit = { _, _, _ -> },
    onNotificationsClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Mi Perfil",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    },
                    actions = {
                        BadgedBox(badge = {}) {
                            IconButton(onClick = onNotificationsClick) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = TextPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },
        bottomBar = {
            Surface(color = Color.White, shadowElevation = 16.dp) {
                NavigationBar(
                    modifier       = Modifier.height(64.dp),
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = false, onClick = onHomeClick,
                        icon  = { Icon(Icons.Default.Home, null, Modifier.size(20.dp)) },
                        label = { Text("Home", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = false, onClick = onSearchClick,
                        icon  = { Icon(Icons.Default.Search, null, Modifier.size(20.dp)) },
                        label = { Text("Buscar", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = true, onClick = { },
                        icon   = { Icon(Icons.Default.Person, null, Modifier.size(20.dp)) },
                        label  = { Text("Perfil", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlueSecondary,
                            selectedTextColor = BlueSecondary,
                            indicatorColor    = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                ProfileHeaderCard(
                    name            = state.user?.fullName ?: "",
                    email           = state.user?.email ?: "",
                    profileImageUrl = state.user?.profileImageUrl,
                    onEditClick     = {
                        onEditProfileClick(
                            state.user?.fullName ?: "",
                            state.user?.phone    ?: "",
                            state.user?.profileImageUrl ?: ""
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Mis Vehículos",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                state.vehicles.forEach { vehicle ->
                    VehicleCard(
                        name          = "${vehicle.brand} ${vehicle.model}",
                        plate         = vehicle.licensePlate,
                        color         = vehicle.color ?: "",
                        onEditClick   = { onEditVehicleClick(vehicle) },
                        // ✅ delete se llama directo al ViewModel — no necesita navegar
                        onDeleteClick = { viewModel.deleteVehicle(vehicle.id) }
                    )
                }

                OutlinedButton(
                    onClick  = onAddVehicleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape  = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BlueSecondary)
                ) {
                    Icon(Icons.Default.Add, null, tint = BlueSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Vehículo", color = BlueSecondary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Mis Reservas",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Reservas Activas",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Color.Gray
                )

                state.activeReservations.forEach { reservation ->
                    ReservationProfileCard(
                        parkingName = reservation.parkingLotName,
                        status      = reservation.status,
                        date        = reservation.startTime,
                        time        = "${reservation.startTime} - ${reservation.endTime}",
                        price       = "$${reservation.totalCost}",
                        onCancel    = { }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Historial de Reservas",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Color.Gray
                )

                state.reservationHistory.forEach { reservation ->
                    ReservationProfileCard(
                        parkingName = reservation.parkingLotName,
                        status      = reservation.status,
                        date        = reservation.startTime,
                        time        = "${reservation.startTime} - ${reservation.endTime}",
                        price       = "$${reservation.totalCost}"
                    )
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}