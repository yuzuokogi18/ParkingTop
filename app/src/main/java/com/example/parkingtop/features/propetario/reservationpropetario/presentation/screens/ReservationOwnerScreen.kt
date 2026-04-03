package com.example.parkingtop.features.propetario.reservationpropetario.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.example.parkingtop.features.propetario.reservationpropetario.presentation.components.ReservationOwnerCard
import com.example.parkingtop.features.propetario.reservationpropetario.presentation.viewmodels.ReservationOwnerViewModel
import com.example.parkingtop.ui.theme.BlueSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationOwnerScreen(
    onDashboardClick: () -> Unit,
    onAvailabilityClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: ReservationOwnerViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val filters = listOf("Todas", "Pendientes", "Confirmadas", "Canceladas")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestionar Reservas", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    NavigationBarItem(
                        selected = false, onClick = onDashboardClick,
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Panel", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = true, onClick = { },
                        icon = { Icon(Icons.Default.List, null) },
                        label = { Text("Reservas", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlueSecondary,
                            selectedTextColor = BlueSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = false, onClick = onAvailabilityClick,
                        icon = { Icon(Icons.Default.DateRange, null) },
                        label = { Text("Disponibilidad", fontSize = 11.sp, maxLines = 1, softWrap = false, overflow = TextOverflow.Visible) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = false, onClick = onProfileClick,
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Perfil", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFD))
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = state.selectedFilter == filter,
                        onClick = { viewModel.loadReservations(filter) },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlueSecondary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF1F3F4)
                        ),
                        border = null,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlueSecondary)
                }
            } else if (state.reservations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFF0F7FF)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.EventBusy, null, modifier = Modifier.size(40.dp), tint = BlueSecondary)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Aún no tienes ninguna reservación de tus estacionamientos", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, color = Color.Gray))
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(state.reservations) { reservation ->
                        ReservationOwnerCard(
                            reservation = reservation,
                            onAccept = { viewModel.updateStatus(reservation.id, ReservationStatus.CONFIRMED) },
                            onDecline = { viewModel.updateStatus(reservation.id, ReservationStatus.CANCELLED) },
                            onComplete = { viewModel.updateStatus(reservation.id, ReservationStatus.COMPLETED) },
                            onViewDetails = { /* TODO */ }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}
