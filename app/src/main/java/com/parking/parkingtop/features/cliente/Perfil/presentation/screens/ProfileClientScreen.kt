package com.parking.parkingtop.features.cliente.Perfil.presentation.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.parking.parkingtop.features.cliente.Perfil.presentation.components.OwnerBalanceCard
import com.parking.parkingtop.features.cliente.Perfil.presentation.components.ProfileHeaderCard
import com.parking.parkingtop.features.cliente.Perfil.presentation.components.ReservationProfileCard
import com.parking.parkingtop.features.cliente.Perfil.presentation.components.VehicleCard
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary
import java.io.File
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileClientScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAddVehicleClick: () -> Unit = {},
    onEditVehicleClick: (Vehicle) -> Unit = {},
    onEditProfileClick: (name: String, phone: String, imageUrl: String) -> Unit = { _, _, _ -> },
    onNotificationsClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onReservationsClick: () -> Unit = {},
    onAvailabilityClick: () -> Unit = {},
    onMySpacesClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onPayoutHistoryClick: () -> Unit = {},
    onProfilePhotoTaken: (Uri) -> Unit = {},   // ✅ devuelve la URI de la foto al NavGraph
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state   = viewModel.state.value
    val context = LocalContext.current
    val isOwner = state.user?.role == "owner"

    // ── Estado local de foto ──────────────────────────────────────────────────
    var cameraImageUri       by remember { mutableStateOf<Uri?>(null) }
    var showPhotoSourceSheet by remember { mutableStateOf(false) }

    // ── Launchers ─────────────────────────────────────────────────────────────

    // 1. Cámara nativa — recibe la URI temporal donde guardar la foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { onProfilePhotoTaken(it) }
    }

    // 2. Galería
    val pickFromGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { onProfilePhotoTaken(it) } }

    // 3. Permiso de cámara — si se concede abre la cámara, si no muestra el rationale
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = File.createTempFile("profile_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraImageUri = uri
            takePictureLauncher.launch(uri)
        } else {
            viewModel.onCameraPermissionDenied()
        }
    }

    // ── Bottom sheet: elegir fuente ───────────────────────────────────────────
    if (showPhotoSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSourceSheet = false },
            containerColor   = Color.White,
            shape            = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    "Cambiar foto de perfil",
                    style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color    = TextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // ── Cámara — solo si el dispositivo tiene cámara ──────────────
                if (state.cameraAvailable) {
                    ListItem(
                        headlineContent = { Text("Tomar foto") },
                        supportingContent = { Text("Usa tu cámara", color = Color.Gray, fontSize = 12.sp) },
                        leadingContent  = {
                            Icon(Icons.Default.CameraAlt, null, tint = BlueSecondary)
                        },
                        modifier = Modifier.clickable {
                            showPhotoSourceSheet = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                }

                // ── Galería — siempre disponible ──────────────────────────────
                ListItem(
                    headlineContent   = { Text("Elegir de galería") },
                    supportingContent = { Text("Selecciona una imagen existente", color = Color.Gray, fontSize = 12.sp) },
                    leadingContent    = {
                        Icon(Icons.Default.PhotoLibrary, null, tint = BlueSecondary)
                    },
                    modifier = Modifier.clickable {
                        showPhotoSourceSheet = false
                        pickFromGalleryLauncher.launch("image/*")
                    }
                )
            }
        }
    }

    // ── Dialog rationale de cámara ────────────────────────────────────────────
    if (state.showCameraPermissionRationale) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCameraRationale() },
            title   = { Text("Permiso de cámara necesario") },
            text    = { Text("Para tomar tu foto de perfil necesitamos acceso a la cámara. Puedes concederlo desde Configuración > Aplicaciones > ParkingTop.") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissCameraRationale() }) {
                    Text("Entendido", color = BlueSecondary)
                }
            }
        )
    }

    // ── Scaffold ──────────────────────────────────────────────────────────────
    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title   = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = TextPrimary) },
                    actions = {
                        IconButton(onClick = onNotificationsClick) {
                            Icon(Icons.Default.Notifications, null, tint = TextPrimary)
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
                        NavigationBarItem(selected = false, onClick = onDashboardClick,
                            icon = { Icon(Icons.Default.Home, null) }, label = { Text("Panel", fontSize = 9.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
                        NavigationBarItem(selected = false, onClick = onReservationsClick,
                            icon = { Icon(Icons.Default.List, null) }, label = { Text("Reservas", fontSize = 9.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
                        NavigationBarItem(selected = false, onClick = onAvailabilityClick,
                            icon = { Icon(Icons.Default.DateRange, null) }, label = { Text("Horario", fontSize = 9.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
                        NavigationBarItem(selected = false, onClick = onMySpacesClick,
                            icon = { Icon(Icons.Default.DirectionsCar, null) }, label = { Text("Espacios", fontSize = 9.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
                        NavigationBarItem(selected = true, onClick = { },
                            icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil", fontSize = 9.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BlueSecondary, selectedTextColor = BlueSecondary,
                                indicatorColor = Color.Transparent))
                    } else {
                        NavigationBarItem(selected = false, onClick = onSearchClick,
                            icon = { Icon(Icons.Default.Search, null, Modifier.size(20.dp)) }, label = { Text("Buscar", fontSize = 10.sp) })
                        NavigationBarItem(selected = true, onClick = { },
                            icon = { Icon(Icons.Default.Person, null, Modifier.size(20.dp)) }, label = { Text("Perfil", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BlueSecondary, selectedTextColor = BlueSecondary,
                                indicatorColor = Color.Transparent))
                    }
                }
            }
        }
    ) { innerPadding ->

        if (state.isLoading && state.user == null) {
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
                    },
                    // ✅ tap en el avatar abre el selector cámara/galería
                    onAvatarClick = { showPhotoSourceSheet = true }
                )

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
                        onClick  = onAddVehicleClick,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape    = RoundedCornerShape(12.dp),
                        border   = BorderStroke(1.dp, BlueSecondary)
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
                            reservation  = reservation,
                            isCancelling = state.cancellingReservationId == reservation.id,
                            onCancel     = { viewModel.cancelReservation(reservation.id) }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Historial de Reservas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    state.reservationHistory.forEach { reservation ->
                        ReservationProfileCard(
                            reservation = reservation,
                            onCancel    = null   // historial no se puede cancelar
                        )
                    }

                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    OwnerBalanceCard(
                        balance = state.balance?.availableBalance ?: "0.00",
                        onTransferClick = onTransferClick,
                        onHistoryClick = onPayoutHistoryClick
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Cuenta de Propietario", fontWeight = FontWeight.Bold, color = BlueSecondary)
                            Text("Estás gestionando tus estacionamientos. Usa el panel inferior para acceder a las herramientas de administración.",
                                fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}