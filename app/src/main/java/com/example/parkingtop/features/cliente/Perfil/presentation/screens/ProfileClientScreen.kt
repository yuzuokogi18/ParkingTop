package com.example.parkingtop.features.cliente.Perfil.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    // Parámetros para cuando el perfil lo ve un PROPIETARIO
    onDashboardClick: () -> Unit = {},
    onReservationsClick: () -> Unit = {},
    onAvailabilityClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val isOwner = state.user?.role == "owner"

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = TextPrimary) },
                    actions = {
                        IconButton(onClick = onNotificationsClick) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    if (isOwner) {
                        // ✅ BARRA PARA PROPIETARIO
                        NavigationBarItem(
                            selected = false, onClick = onDashboardClick,
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Panel", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = false, onClick = onReservationsClick,
                            icon = { Icon(Icons.Default.List, null) },
                            label = { Text("Reservas", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = false, onClick = onAvailabilityClick,
                            icon = { Icon(Icons.Default.DateRange, null) },
                            label = { Text("Disponibilidad", fontSize = 11.sp, maxLines = 1, softWrap = false, overflow = TextOverflow.Visible) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = true, onClick = { },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Perfil", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BlueSecondary,
                                selectedTextColor = BlueSecondary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    } else {
                        // ── BARRA PARA CLIENTE ──
                        NavigationBarItem(
                            selected = false, onClick = onHomeClick,
                            icon = { Icon(Icons.Default.Home, null, Modifier.size(20.dp)) },
                            label = { Text("Home", fontSize = 10.sp) }
                        )
                        NavigationBarItem(
                            selected = false, onClick = onSearchClick,
                            icon = { Icon(Icons.Default.Search, null, Modifier.size(20.dp)) },
                            label = { Text("Buscar", fontSize = 10.sp) }
                        )
                        NavigationBarItem(
                            selected = true, onClick = { },
                            icon = { Icon(Icons.Default.Person, null, Modifier.size(20.dp)) },
                            label = { Text("Perfil", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BlueSecondary,
                                selectedTextColor = BlueSecondary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BlueSecondary)
            }
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
                    name = state.user?.fullName ?: "",
                    email = state.user?.email ?: "",
                    profileImageUrl = state.user?.profileImageUrl,
                    onEditClick = {
                        onEditProfileClick(
                            state.user?.fullName ?: "",
                            state.user?.phone ?: "",
                            state.user?.profileImageUrl ?: ""
                        )
                    }
                )

                // Solo mostrar secciones de cliente si NO es propietario
                if (!isOwner) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Mis Vehículos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    state.vehicles.forEach { vehicle ->
                        VehicleCard(
                            name = "${vehicle.brand} ${vehicle.model}",
                            plate = vehicle.licensePlate,
                            color = vehicle.color ?: "",
                            onEditClick = { onEditVehicleClick(vehicle) },
                            onDeleteClick = { viewModel.deleteVehicle(vehicle.id) }
                        )
                    }
                    OutlinedButton(
                        onClick = onAddVehicleClick,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BlueSecondary)
                    ) {
                        Icon(Icons.Default.Add, null, tint = BlueSecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar Vehículo", color = BlueSecondary)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Mis Reservas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Reservas Activas", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                    state.activeReservations.forEach { reservation ->
                        ReservationProfileCard(
                            parkingName = reservation.parkingLotName,
                            status = reservation.status,
                            date = reservation.startTime,
                            time = "${reservation.startTime} - ${reservation.endTime}",
                            price = "$${reservation.totalCost}",
                            onCancel = { }
                        )
                    }
                } else {
                    // Contenido específico para PROPIETARIO en su perfil
                    Spacer(modifier = Modifier.height(32.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Cuenta de Propietario", fontWeight = FontWeight.Bold, color = BlueSecondary)
                            Text("Estás gestionando tus estacionamientos. Usa el panel inferior para acceder a las herramientas de administración.", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
