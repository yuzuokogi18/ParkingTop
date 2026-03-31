package com.example.parkingtop.features.propetario.reservationpropetario.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.example.parkingtop.features.propetario.reservationpropetario.presentation.components.ReservationOwnerCard
import com.example.parkingtop.features.propetario.reservationpropetario.presentation.viewmodels.ReservationOwnerViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationOwnerScreen(
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFD))
        ) {
            // Filtros
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = state.selectedFilter == filter,
                        onClick = { viewModel.loadReservations(filter) },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlueSecondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlueSecondary)
                }
            } else if (state.reservations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay reservas con este estado.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.reservations) { reservation ->
                        ReservationOwnerCard(
                            reservation = reservation,
                            onAccept = { viewModel.updateStatus(reservation.id, ReservationStatus.CONFIRMED) },
                            onDecline = { viewModel.updateStatus(reservation.id, ReservationStatus.CANCELLED) },
                            onComplete = { viewModel.updateStatus(reservation.id, ReservationStatus.COMPLETED) },
                            onViewDetails = { /* TODO: Navegar a detalles */ }
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
