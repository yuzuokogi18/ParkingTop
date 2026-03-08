package com.example.parkingtop.features.cliente.Perfil.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.cliente.Perfil.presentation.components.ProfileHeaderCard
import com.example.parkingtop.features.cliente.Perfil.presentation.components.ReservationProfileCard
import com.example.parkingtop.features.cliente.Perfil.presentation.components.VehicleCard
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileClientScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val selectedTab = 2 // Profile is selected

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Mi Perfil",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* Settings */ }) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                NavigationBar(
                    modifier = Modifier.height(64.dp),
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onHomeClick,
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        label = { Text("Home", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onSearchClick,
                        icon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        label = { Text("Buscar", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(20.dp)) },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Perfil Info
            ProfileHeaderCard(
                name = "Juan Pérez",
                email = "juan.perez@example.com"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mis Vehículos
            Text("Mis Vehículos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            VehicleCard(name = "Renault Clio", plate = "ABC-123", color = "Gris")
            VehicleCard(name = "Ford Focus", plate = "XYZ-789", color = "Azul")
            
            OutlinedButton(
                onClick = { /* Add Vehicle */ },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BlueSecondary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = BlueSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Vehículo", color = BlueSecondary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mis Reservas
            Text("Mis Reservas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("Reservas Activas", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            ReservationProfileCard(
                parkingName = "Parking Central",
                status = "Activa",
                date = "10/08/2024",
                time = "14:00 - 16:00",
                price = "12.50 €",
                onCancel = { /* Cancel */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Historial de Reservas", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            ReservationProfileCard(
                parkingName = "Parking Estación",
                status = "Completada",
                date = "01/08/2024",
                time = "09:00 - 11:00",
                price = "8.00 €"
            )
            ReservationProfileCard(
                parkingName = "Parking Centro Comercial",
                status = "Completada",
                date = "25/07/2024",
                time = "18:00 - 20:00",
                price = "10.00 €"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
