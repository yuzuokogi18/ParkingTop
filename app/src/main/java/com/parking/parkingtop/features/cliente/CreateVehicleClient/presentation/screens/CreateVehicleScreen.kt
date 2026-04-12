package com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.components.VehicleField
import com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.components.VehicleTypeChip
import com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.viewmodels.CreateVehicleViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

// Tipos de vehículo disponibles en el backend
private val VEHICLE_TYPES = listOf(
    "car"   to "Auto",
    "moto"  to "Moto",
    "suv"   to "SUV",
    "van"   to "Van",
    "truck" to "Camión"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVehicleScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: CreateVehicleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var licensePlate  by remember { mutableStateOf("") }
    var brand         by remember { mutableStateOf("") }
    var model         by remember { mutableStateOf("") }
    var color         by remember { mutableStateOf("") }
    var selectedType  by remember { mutableStateOf("car") }
    var isDefault     by remember { mutableStateOf(false) }

    // Navegar de vuelta al perfil cuando el vehículo se creó exitosamente
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Agregar Vehículo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = "Volver",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ── Ícono decorativo ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF0F4FF))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint     = BlueSecondary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Completa los datos de tu vehículo",
                style    = MaterialTheme.typography.bodyMedium,
                color    = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Error ─────────────────────────────────────────────────────────
            state.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text     = error,
                        color    = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ── Placa (obligatoria) ───────────────────────────────────────────
            VehicleField(
                label = "Placa *",
                value = licensePlate,
                onChange = { licensePlate = it.uppercase() },
                placeholder = "Ej. ABC-123"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Marca ─────────────────────────────────────────────────────────
            VehicleField(
                label = "Marca",
                value = brand,
                onChange = { brand = it },
                placeholder = "Ej. Toyota"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Modelo ────────────────────────────────────────────────────────
            VehicleField(
                label = "Modelo",
                value = model,
                onChange = { model = it },
                placeholder = "Ej. Corolla"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Color ─────────────────────────────────────────────────────────
            VehicleField(
                label = "Color",
                value = color,
                onChange = { color = it },
                placeholder = "Ej. Blanco"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Tipo de vehículo ──────────────────────────────────────────────
            Text(
                "Tipo de vehículo",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VEHICLE_TYPES.forEach { (key, label) ->
                    VehicleTypeChip(
                        label = label,
                        isSelected = selectedType == key,
                        onClick = { selectedType = key },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Vehículo por defecto ──────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Vehículo por defecto",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = TextPrimary
                        )
                        Text(
                            "Se usará automáticamente al reservar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked         = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor  = Color.White,
                            checkedTrackColor  = BlueSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Botón guardar ─────────────────────────────────────────────────
            Button(
                onClick = {
                    viewModel.createVehicle(
                        licensePlate = licensePlate,
                        brand        = brand.ifBlank { null },
                        model        = model.ifBlank { null },
                        color        = color.ifBlank { null },
                        isDefault    = isDefault
                    )
                },
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape     = RoundedCornerShape(14.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                enabled   = licensePlate.isNotBlank() && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Guardar Vehículo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}