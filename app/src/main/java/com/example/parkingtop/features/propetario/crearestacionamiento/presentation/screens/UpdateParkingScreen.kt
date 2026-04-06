package com.example.parkingtop.features.propetario.crearestacionamiento.presentation.screens

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.DayHours
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.OperatingHours
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormSection
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormSwitch
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.components.FormTextField
import com.example.parkingtop.features.propetario.crearestacionamiento.presentation.viewmodels.UpdateParkingViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateParkingScreen(
    parkingId: String,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: UpdateParkingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var stateStr by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    
    var basePrice by remember { mutableStateOf("") }
    var overtimeRate by remember { mutableStateOf("") }
    var totalSpots by remember { mutableStateOf("") }
    
    var isCovered by remember { mutableStateOf(false) }
    var hasCctv by remember { mutableStateOf(false) }
    var hasSecurity by remember { mutableStateOf(false) }

    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val dayChecks = remember { mutableStateListOf(true, true, true, true, true, false, false) }
    val openTimes = remember { mutableStateListOf("08:00", "08:00", "08:00", "08:00", "08:00", "09:00", "10:00") }
    val closeTimes = remember { mutableStateListOf("20:00", "20:00", "20:00", "20:00", "22:00", "23:00", "18:00") }

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    LaunchedEffect(parkingId) {
        viewModel.loadParking(parkingId)
    }

    LaunchedEffect(state.parkingData) {
        state.parkingData?.let { data ->
            name = data.name
            description = data.description
            address = data.address
            city = data.city
            stateStr = data.state
            postalCode = data.postalCode
            latitude = data.latitude.toString()
            longitude = data.longitude.toString()
            basePrice = data.basePricePerHour.toString()
            overtimeRate = data.overtimeRatePerHour.toString()
            totalSpots = data.totalSpots.toString()
            
            isCovered = data.features.contains("techado")
            hasCctv = data.features.contains("cctv")
            hasSecurity = data.features.contains("vigilancia")

            val hours = data.operatingHours
            val hList = listOf(hours.monday, hours.tuesday, hours.wednesday, hours.thursday, hours.friday, hours.saturday, hours.sunday)
            hList.forEachIndexed { i, h ->
                openTimes[i] = h.open
                closeTimes[i] = h.close
            }
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5),
        onResult = { uris -> selectedImageUris = uris }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Actualizar Estacionamiento", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (state.isLoading && state.parkingData == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BlueSecondary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFFDFDFD))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                FormSection(title = "Información Básica") {
                    FormTextField(label = "Nombre", value = name, onValueChange = { name = it }, placeholder = "Ej. Estacionamiento Centro")
                    FormTextField(label = "Descripción", value = description, onValueChange = { description = it }, placeholder = "Breve descripción...")
                    FormTextField(label = "Dirección", value = address, onValueChange = { address = it }, placeholder = "Avenida Central Norte 123")
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FormTextField(modifier = Modifier.weight(1f), label = "Ciudad", value = city, onValueChange = { city = it }, placeholder = "Tuxtla Gtz")
                        FormTextField(modifier = Modifier.weight(1f), label = "Estado", value = stateStr, onValueChange = { stateStr = it }, placeholder = "Chiapas")
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FormTextField(modifier = Modifier.weight(1f), label = "C.P.", value = postalCode, onValueChange = { postalCode = it }, placeholder = "29000")
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FormTextField(modifier = Modifier.weight(1f), label = "Latitud", value = latitude, onValueChange = { latitude = it }, placeholder = "16.7516")
                        FormTextField(modifier = Modifier.weight(1f), label = "Longitud", value = longitude, onValueChange = { longitude = it }, placeholder = "-93.1029")
                    }
                }

                FormSection(title = "Fotos del Estacionamiento") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5))
                            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable { 
                                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Toca para cambiar fotos", color = Color.Gray, fontSize = 12.sp)
                            Text("Subir Fotos", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    if (selectedImageUris.isNotEmpty()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            selectedImageUris.take(3).forEach { uri ->
                                AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                            }
                        }
                    }
                }

                FormSection(title = "Horarios de Operación") {
                    days.forEachIndexed { index, day ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(checked = dayChecks[index], onCheckedChange = { dayChecks[index] = it }, colors = CheckboxDefaults.colors(checkedColor = BlueSecondary))
                            Text(text = day, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Medium)
                            OutlinedTextField(value = openTimes[index], onValueChange = { openTimes[index] = it }, modifier = Modifier.weight(1f), textStyle = MaterialTheme.typography.bodySmall, enabled = dayChecks[index], shape = RoundedCornerShape(8.dp))
                            Text("-")
                            OutlinedTextField(value = closeTimes[index], onValueChange = { closeTimes[index] = it }, modifier = Modifier.weight(1f), textStyle = MaterialTheme.typography.bodySmall, enabled = dayChecks[index], shape = RoundedCornerShape(8.dp))
                        }
                    }
                }

                FormSection(title = "Tarifas y Capacidad") {
                    FormTextField(label = "Precio Base por Hora", value = basePrice, onValueChange = { basePrice = it }, placeholder = "15.00", leadingIcon = { Text("$", color = Color.Gray, modifier = Modifier.padding(start = 12.dp)) })
                    FormTextField(label = "Precio por Hora Extra", value = overtimeRate, onValueChange = { overtimeRate = it }, placeholder = "20.00", leadingIcon = { Text("$", color = Color.Gray, modifier = Modifier.padding(start = 12.dp)) })
                    FormTextField(label = "Total de Espacios", value = totalSpots, onValueChange = { totalSpots = it }, placeholder = "25", leadingIcon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null, tint = Color.Gray) })
                }

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
                        if (isCovered) features.add("techado")
                        if (hasCctv) features.add("cctv")
                        if (hasSecurity) features.add("vigilancia")
                        features.add("iluminado")

                        val operatingHours = OperatingHours(
                            monday = DayHours(openTimes[0], closeTimes[0]),
                            tuesday = DayHours(openTimes[1], closeTimes[1]),
                            wednesday = DayHours(openTimes[2], closeTimes[2]),
                            thursday = DayHours(openTimes[3], closeTimes[3]),
                            friday = DayHours(openTimes[4], closeTimes[4]),
                            saturday = DayHours(openTimes[5], closeTimes[5]),
                            sunday = DayHours(openTimes[6], closeTimes[6])
                        )

                        val imageFiles = selectedImageUris.mapNotNull { uri ->
                            try {
                                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val file = File(context.cacheDir, "parking_upd_${System.currentTimeMillis()}.$extension")
                                val outputStream = FileOutputStream(file)
                                inputStream?.use { input -> outputStream.use { output -> input.copyTo(output) } }
                                file
                            } catch (e: Exception) { null }
                        }

                        viewModel.updateParking(
                            id = parkingId,
                            name = name,
                            description = description,
                            address = address,
                            city = city,
                            state = stateStr,
                            postalCode = postalCode,
                            latitude = latitude.toDoubleOrNull() ?: 0.0,
                            longitude = longitude.toDoubleOrNull() ?: 0.0,
                            totalSpots = totalSpots.toIntOrNull() ?: 0,
                            basePricePerHour = basePrice.toDoubleOrNull() ?: 0.0,
                            overtimeRatePerHour = overtimeRate.toDoubleOrNull() ?: 0.0,
                            features = features,
                            operatingHours = operatingHours,
                            images = imageFiles
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
}
