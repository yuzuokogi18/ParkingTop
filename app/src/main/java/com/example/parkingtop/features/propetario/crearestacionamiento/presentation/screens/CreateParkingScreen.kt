package com.example.parkingtop.features.propetario.crearestacionamiento.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormSection
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormSwitch
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormTextField
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.viewmodels.CreateParkingViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateParkingScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: CreateParkingViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var stateStr by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("-34.6037") }
    var longitude by remember { mutableStateOf("-58.3816") }
    
    var basePrice by remember { mutableStateOf("150.00") }
    var overtimeRate by remember { mutableStateOf("100.00") }
    var totalSpots by remember { mutableStateOf("50") }
    
    var isCovered by remember { mutableStateOf(false) }
    var hasCctv by remember { mutableStateOf(true) }
    var hasSecurity by remember { mutableStateOf(false) }

    val state by viewModel.state

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Estacionamiento", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFD))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Información Básica
            FormSection(title = "Información Básica") {
                FormTextField(label = "Nombre del Estacionamiento", value = name, onValueChange = { name = it }, placeholder = "Ej. Estacionamiento Central")
                FormTextField(label = "Dirección", value = address, onValueChange = { address = it }, placeholder = "Calle Principal 123")
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormTextField(modifier = Modifier.weight(1f), label = "Ciudad", value = city, onValueChange = { city = it }, placeholder = "Ej. CDMX")
                    FormTextField(modifier = Modifier.weight(1f), label = "Estado", value = stateStr, onValueChange = { stateStr = it }, placeholder = "Ej. CDMX")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormTextField(modifier = Modifier.weight(1f), label = "Latitud", value = latitude, onValueChange = { latitude = it }, placeholder = "-34.6037")
                    FormTextField(modifier = Modifier.weight(1f), label = "Longitud", value = longitude, onValueChange = { longitude = it }, placeholder = "-58.3816")
                }
            }

            // Fotos
            FormSection(title = "Fotos del Estacionamiento") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9F9F9))
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { /* TODO: Pick images */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = Color.Gray)
                        Text("Toca para subir fotos", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Subir Fotos", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                             AsyncImage(
                                model = "https://via.placeholder.com/150",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                             )
                        }
                    }
                }
            }

            // Tarifas y Capacidad
            FormSection(title = "Tarifas y Capacidad") {
                FormTextField(label = "Precio Base por Hora", value = basePrice, onValueChange = { basePrice = it }, placeholder = "150.00", leadingIcon = { Text("$", color = Color.Gray, modifier = Modifier.padding(start = 12.dp)) })
                FormTextField(label = "Precio por Hora Extra", value = overtimeRate, onValueChange = { overtimeRate = it }, placeholder = "100.00", leadingIcon = { Text("$", color = Color.Gray, modifier = Modifier.padding(start = 12.dp)) })
                FormTextField(label = "Total de Espacios", value = totalSpots, onValueChange = { totalSpots = it }, placeholder = "50", leadingIcon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null, tint = Color.Gray) })
            }

            // Características
            FormSection(title = "Características") {
                FormSwitch(label = "Techado", checked = isCovered, onCheckedChange = { isCovered = it })
                FormSwitch(label = "Cámaras de Seguridad (CCTV)", checked = hasCctv, onCheckedChange = { hasCctv = it })
                FormSwitch(label = "Vigilancia 24/7", checked = hasSecurity, onCheckedChange = { hasSecurity = it })
            }

            if (state.error != null) {
                Text(text = state.error!!, color = Color.Red, fontSize = 12.sp)
            }

            Button(
                onClick = { 
                    val features = mutableListOf<String>()
                    if (isCovered) features.add("covered")
                    if (hasCctv) features.add("cctv")
                    if (hasSecurity) features.add("security")

                    viewModel.createParking(
                        name = name,
                        address = address,
                        city = city,
                        state = stateStr,
                        latitude = latitude.toDoubleOrNull() ?: 0.0,
                        longitude = longitude.toDoubleOrNull() ?: 0.0,
                        totalSpots = totalSpots.toIntOrNull() ?: 0,
                        basePricePerHour = basePrice.toDoubleOrNull() ?: 0.0,
                        overtimeRatePerHour = overtimeRate.toDoubleOrNull() ?: 0.0,
                        features = features,
                        images = emptyList()
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
