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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.R
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.components.SearchParkingCard
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.components.SearchParkingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaClientScreen(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val selectedTab = 1 // Buscar isi selected

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
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
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
                                painter = painterResource(R.drawable.logo_parking),
                                contentDescription = "Logo",
                                modifier = Modifier.size(22.dp)
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
                        color = Color(0xFFF1F3F4),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Sort, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Distancia", color = Color.Black, fontSize = 12.sp)
                        }
                    }

                    Surface(
                        onClick = { },
                        color = Color(0xFFF1F3F4),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Filtros", color = Color.Black, fontSize = 12.sp)
                        }
                    }
                }
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
                        selected = selectedTab == 0,
                        onClick = onHomeClick,
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Home, 
                                contentDescription = "Home",
                                modifier = Modifier.size(20.dp)
                            ) 
                        },
                        label = { Text("Home", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { },
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Search, 
                                contentDescription = "Buscar",
                                modifier = Modifier.size(20.dp)
                            ) 
                        },
                        label = { Text("Buscar", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = onProfileClick,
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Person, 
                                contentDescription = "Perfil",
                                modifier = Modifier.size(20.dp)
                            ) 
                        },
                        label = { Text("Perfil", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
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
        ) {
            // Mapa placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE9EEF1)),
                contentAlignment = Alignment.Center
            ) {
                // Background visual detail for map
                Icon(
                    painter = painterResource(id = R.drawable.logo_parking),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp).alpha(0.05f)
                )
                Text("Vista de Mapa", color = Color.Gray, fontWeight = FontWeight.Medium)
            }

            Text(
                "Estacionamientos disponibles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val parkings = listOf(
                    SearchParkingItem("Parking Central", "0.5 km", "2.50", 15, 4.5f, "Disponible"),
                    SearchParkingItem("Estacionamiento Plaza", "1.2 km", "1.80", 3, 3.9f, "Pocos"),
                    SearchParkingItem("Garaje 24h Av.", "0.8 km", "3.00", 0, 4.8f, "Lleno"),
                    SearchParkingItem("Parking del Centro", "2.1 km", "2.00", 20, 4.2f, "Disponible"),
                    SearchParkingItem("Parking de la Est.", "0.3 km", "2.75", 7, 4.1f, "Pocos")
                )
                items(parkings) { parking ->
                    SearchParkingCard(parking)
                }
            }
        }
    }
}
