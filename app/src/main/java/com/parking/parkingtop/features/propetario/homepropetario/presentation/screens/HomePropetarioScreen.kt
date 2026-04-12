package com.parking.parkingtop.features.propetario.homepropetario.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.parking.parkingtop.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.parking.parkingtop.features.propetario.homepropetario.presentation.components.OwnerParkingCard
import com.parking.parkingtop.features.propetario.homepropetario.presentation.viewmodels.HomePropetarioViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePropetarioScreen(
    onAddParkingClick: () -> Unit,
    onParkingClick: (String) -> Unit,
    onEditParkingClick: (String) -> Unit,
    onReservationsClick: () -> Unit,
    onMySpacesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAvailabilityClick: () -> Unit = {},
    viewModel: HomePropetarioViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val hasParkings = state.data?.parkings?.isNotEmpty() == true
    
    var parkingToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    if (parkingToDelete != null) {
        AlertDialog(
            onDismissRequest = { parkingToDelete = null },
            title = { Text("¿Eliminar estacionamiento?") },
            text = { Text("¿Estás seguro de que deseas eliminar este estacionamiento? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        parkingToDelete?.let { viewModel.deleteParking(it) }
                        parkingToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { parkingToDelete = null }) {
                    Text("Cancelar")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        bottomBar = {
            if (hasParkings) {
                Column {
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        NavigationBarItem(
                            selected = true,
                            onClick = { },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("Panel", fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BlueSecondary,
                                selectedTextColor = BlueSecondary,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = false,
                            onClick = onReservationsClick,
                            icon = { Icon(Icons.Default.List, contentDescription = null) },
                            label = { Text("Reservas", fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = false,
                            onClick = onAvailabilityClick,
                            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                            label = { Text("Horario", fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = false,
                            onClick = onMySpacesClick,
                            icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                            label = { Text("Espacios", fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                        NavigationBarItem(
                            selected = false,
                            onClick = onProfileClick,
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            label = { Text("Perfil", fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddParkingClick,
                containerColor = BlueSecondary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Estacionamiento")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TextPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_parking),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        tint = Color.Gray
                    )
                }
            }

            if (state.isLoading && state.data == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlueSecondary)
                }
            } else if (state.error != null && state.data == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error!!, color = Color.Red)
                }
            } else {
                state.data?.let { data ->
                    HomeOwnerContent(
                        data = data, 
                        onParkingClick = onParkingClick,
                        onUpdateClick = onEditParkingClick,
                        onDeleteClick = { id -> parkingToDelete = id }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeOwnerContent(
    data: HomePropetarioData,
    onParkingClick: (String) -> Unit,
    onUpdateClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(text = "Bienvenido de nuevo,", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(text = data.ownerName, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)

        Spacer(modifier = Modifier.height(24.dp))

        if (data.parkings.isEmpty()) {
            EmptyParkingsMessage()
        } else {
            Text(text = "Resumen General", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Ocupación Actual",
                    value = "${data.summary.currentOccupationPercentage}%",
                    icon = Icons.Default.DirectionsCar,
                    backgroundColor = Color(0xFFF0F7FF)
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Ingresos (Mes)",
                    value = "$${data.summary.monthlyEarnings}",
                    icon = Icons.Default.List,
                    backgroundColor = Color(0xFFFFF7F0)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Notificaciones Importantes", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            data.notifications.firstOrNull()?.let { notification ->
                ImportantNotificationItem(notification.message, notification.timeAgo)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Mis Estacionamientos", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            data.parkings.forEach { parking ->
                OwnerParkingCard(
                    parking = parking,
                    onClick = { onParkingClick(parking.id) },
                    onUpdateClick = { onUpdateClick(parking.id) },
                    onDeleteClick = { onDeleteClick(parking.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun EmptyParkingsMessage() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFFF0F7FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(60.dp), tint = BlueSecondary)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "¡Aún no tienes estacionamientos!", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Empieza a generar ingresos registrando tu primer espacio de estacionamiento.", style = MaterialTheme.typography.bodyLarge, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFF7F0)).padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFFFF9800))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Toca el botón azul de abajo para comenzar", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = TextPrimary)
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = backgroundColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        }
    }
}

@Composable
fun ImportantNotificationItem(message: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFF0F7FF)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Info, contentDescription = null, tint = BlueSecondary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, maxLines = 2)
            Text(text = time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
    }
}
