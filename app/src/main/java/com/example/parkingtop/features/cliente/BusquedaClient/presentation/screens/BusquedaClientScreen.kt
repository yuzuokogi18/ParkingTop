package com.example.parkingtop.features.cliente.BusquedaClient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parkingtop.R
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.components.ParkingMap
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.components.SearchParkingCard
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.components.SearchParkingItem
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels.ParkingMapViewModel
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels.ParkingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaClientScreen(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onParkingClick: (String) -> Unit = {},
    listViewModel: ParkingViewModel = hiltViewModel(),
    mapViewModel: ParkingMapViewModel = hiltViewModel()
) {
    val selectedTab = 1

    // Estado de lista (ya existente)
    val parkingLots by listViewModel.parkingLots.collectAsStateWithLifecycle()
    val parkings = parkingLots.map {
        SearchParkingItem(
            id            = it.id,
            name          = it.name,
            address       = it.address,
            price         = it.pricePerHour.toString(),
            spaces        = it.availableSpots,
            rating        = it.rating.toFloat(),
            status        = when {
                it.availableSpots == 0 -> "Lleno"
                it.availableSpots < 5  -> "Pocos"
                else                   -> "Disponible"
            },
            imageUrl      = it.images.firstOrNull()
        )
    }

    // Estado del mapa
    val mapState by mapViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Resultados de Búsqueda",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 18.sp
                            )
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.onSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter           = painterResource(R.drawable.logo_parking),
                                contentDescription = "Logo",
                                modifier          = Modifier.size(22.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        onClick = { },
                        color   = Color(0xFFF1F3F4),
                        shape   = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier          = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Sort, contentDescription = null,
                                tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Distancia", color = Color.Black, fontSize = 12.sp)
                        }
                    }
                    Surface(
                        onClick = { },
                        color   = Color(0xFFF1F3F4),
                        shape   = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier          = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null,
                                tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Filtros", color = Color.Black, fontSize = 12.sp)
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },

        bottomBar = {
            Surface(color = Color.White, shadowElevation = 16.dp) {
                NavigationBar(
                    modifier        = Modifier.height(64.dp),
                    containerColor  = Color.White,
                    tonalElevation  = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0, onClick = onHomeClick,
                        icon  = { Icon(Icons.Default.Home, contentDescription = "Home",
                            modifier = Modifier.size(20.dp)) },
                        label = { Text("Home", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1, onClick = { },
                        icon  = { Icon(Icons.Default.Search, contentDescription = "Buscar",
                            modifier = Modifier.size(20.dp)) },
                        label = { Text("Buscar", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2, onClick = onProfileClick,
                        icon  = { Icon(Icons.Default.Person, contentDescription = "Perfil",
                            modifier = Modifier.size(20.dp)) },
                        label = { Text("Perfil", fontSize = 10.sp) }
                    )
                }
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ── MAPA REAL (reemplaza el placeholder gris) ─────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)                          // un poco más alto ahora que es real
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (mapState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE9EEF1)),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                } else {
                    ParkingMap(
                        markers        = mapState.markers,
                        selectedMarker = mapState.selectedMarker,
                        onMarkerClick  = { mapViewModel.onMarkerSelected(it) },
                        onCardClick    = { id -> onParkingClick(id) },
                        onDismiss      = { mapViewModel.onMarkerDismissed() },
                        modifier       = Modifier.fillMaxSize()
                    )
                }
            }
            // ─────────────────────────────────────────────────────────────────

            Text(
                "Estacionamientos disponibles",
                fontSize     = 18.sp,
                fontWeight   = FontWeight.Bold,
                modifier     = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (parkings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier        = Modifier.fillMaxSize(),
                    contentPadding  = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(parkings) { parking ->
                        SearchParkingCard(
                            item          = parking,
                            onParkingClick = { id -> onParkingClick(id) }
                        )
                    }
                }
            }
        }
    }
}