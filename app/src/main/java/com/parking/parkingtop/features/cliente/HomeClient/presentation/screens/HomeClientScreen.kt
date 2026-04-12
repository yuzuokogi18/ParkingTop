package com.parking.parkingtop.features.cliente.HomeClient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.parking.parkingtop.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.parking.parkingtop.features.cliente.HomeClient.presentation.components.HomeSearchBar
import com.parking.parkingtop.features.cliente.HomeClient.presentation.components.LocationPermissionCard
import com.parking.parkingtop.features.cliente.HomeClient.presentation.components.ParkingCard
import com.parking.parkingtop.features.cliente.HomeClient.presentation.components.ParkingItem
import com.parking.parkingtop.features.cliente.HomeClient.presentation.components.SearchRadiusSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeClientScreen(
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onParkingClick: (ParkingItem) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableStateOf(5f) }
    val selectedTab = 0 // Home is selected

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Parking Top",
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
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 16.dp
            ) {
                NavigationBar(
                    modifier = Modifier.height(64.dp),
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { /* Already here */ },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text("Home", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = onSearchClick,
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
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            LocationPermissionCard()

            Spacer(modifier = Modifier.height(24.dp))

            HomeSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchRadiusSlider(
                position = sliderPosition,
                onPositionChange = { sliderPosition = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Estacionamientos Destacados",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val sampleParkings = listOf(
                    ParkingItem("1", "Parking Central", "0.5 km", "2.50", "Libre", 4.8f, 1),
                    ParkingItem(
                        "2",
                        "Estacionamiento Rápido",
                        "1.2 km",
                        "2.00",
                        "Ocupado",
                        4.2f,
                        2
                    ),
                    ParkingItem("3", "Parking Seguro", "0.8 km", "3.00", "Libre", 4.5f, 3),
                    ParkingItem("4", "Gran Parking Express", "1.5 km", "2.75", "Libre", 4.7f, 4)
                )
                items(sampleParkings) { parking ->
                    Box(modifier = Modifier.clickable { onParkingClick(parking) }) {
                        ParkingCard(parking)
                    }
                }
            }
        }
    }
}
