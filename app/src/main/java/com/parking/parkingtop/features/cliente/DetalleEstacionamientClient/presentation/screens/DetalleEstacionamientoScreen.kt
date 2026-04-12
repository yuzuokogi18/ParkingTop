package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.ParkingDetalleViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components.DetailImageCard
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components.DetailSection
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components.FeatureChip
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components.ReviewItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetalleEstacionamientoScreen(
    parkingId: String,
    isOwner: Boolean = false, // ✅ Nuevo parámetro para distinguir roles
    onBackClick: () -> Unit = {},
    onReserveClick: () -> Unit = {},
    viewModel: ParkingDetalleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(parkingId) {
        viewModel.loadDetail(parkingId)
    }

    val parking = state.parking

    if (state.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BlueSecondary)
        }
        return
    }

    parking?.let { lot ->
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(lot.name, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // IMÁGENES
                LazyRow(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lot.images.size) { index ->
                        DetailImageCard(lot.images[index])
                    }
                }

                // DESCRIPCIÓN
                DetailSection(title = "Descripción") {
                    Text(
                        text = lot.description ?: "Sin descripción",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(lot.address, fontSize = 12.sp, color = Color.Gray)
                }

                // PRECIOS
                DetailSection(title = "Precios y Disponibilidad") {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Precio Base", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                "$${lot.pricing.basePricePerHour}/hora",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Hora Extra", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                "$${lot.pricing.overtimeRatePerHour}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Espacios Disponibles", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Outlined.DirectionsCar, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${lot.availability.available} de ${lot.availability.total}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // FEATURES
                DetailSection(title = "Características") {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        lot.features.forEach { feature ->
                            FeatureChip(feature, Icons.Outlined.Info)
                        }
                    }
                }

                // RATING
                DetailSection(title = "Reseñas (${lot.reviews.size})") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(lot.ratingAverage.toInt()) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB74D)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(lot.ratingAverage.toString(), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    lot.reviews.forEach { ReviewItem(it) }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ SOLO MOSTRAR BOTÓN SI NO ES EL PROPIETARIO
                if (!isOwner) {
                    Button(
                        onClick = onReserveClick,
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
                    ) {
                        Text("Reservar", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    state.error?.let {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(it, color = Color.Red)
        }
    }
}
