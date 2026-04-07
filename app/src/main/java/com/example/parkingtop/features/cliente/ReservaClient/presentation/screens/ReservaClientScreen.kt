package com.example.parkingtop.features.reservations.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.ConfirmReservationButton
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.DateSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PaymentMethodBottomSheet
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PaymentMethodSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PriceSummaryCard
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.SpotBottomSheet
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.TimeSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.VehicleBottomSheet
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.VehicleSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.viewmodels.ReservationViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    onBack: () -> Unit = {},
    onPaymentSuccess: (String) -> Unit = {},
    onCashReservation: () -> Unit = {},
    viewModel: ReservationViewModel = hiltViewModel()
) {
    val state   = viewModel.state.value
    val context = LocalContext.current

    var showEntryDatePicker    by remember { mutableStateOf(false) }
    var showEntryTimePicker    by remember { mutableStateOf(false) }
    var showExitDatePicker     by remember { mutableStateOf(false) }
    var showExitTimePicker     by remember { mutableStateOf(false) }
    var showVehicleSheet       by remember { mutableStateOf(false) }
    var showSpotSheet          by remember { mutableStateOf(false) }
    var showPaymentMethodSheet by remember { mutableStateOf(false) }

    LaunchedEffect(state.reservationSuccess) {
        if (state.reservationSuccess) {
            val result = state.reservationResult
            if (result != null) {
                if (result.isCash) onCashReservation()
                else result.paymentUrl?.let { onPaymentSuccess(it) }
            }
            viewModel.resetSuccess()
        }
    }

    if (showVehicleSheet) {
        VehicleBottomSheet(
            vehicles          = state.vehicles,
            selectedVehicle   = state.selectedVehicle,
            onVehicleSelected = { viewModel.selectVehicle(it) },
            onDismiss         = { showVehicleSheet = false }
        )
    }
    if (showSpotSheet) {
        SpotBottomSheet(
            spots          = state.spots,
            selectedSpot   = state.selectedSpot,
            onSpotSelected = { viewModel.selectSpot(it) },
            onDismiss      = { showSpotSheet = false }
        )
    }
    if (showPaymentMethodSheet) {
        PaymentMethodBottomSheet(
            selectedMethod   = state.selectedPaymentMethod,
            onMethodSelected = { viewModel.selectPaymentMethod(it) },
            onDismiss        = { showPaymentMethodSheet = false }
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Reservar Estacionamiento",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->

        if (state.isLoading && state.parkingLot == null) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // ── Vehículo ──────────────────────────────────────────────────
                if (state.isLoadingVehicles) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cargando vehículos...", color = Color.Gray)
                    }
                } else {
                    VehicleSelector(
                        selectedVehicle = state.selectedVehicle?.let {
                            "${it.brand} ${it.model} (${it.licensePlate})"
                        } ?: "Selecciona un vehículo",
                        vehicleModel   = state.selectedVehicle?.model ?: "",
                        vehiclePlate   = state.selectedVehicle?.licensePlate ?: "",
                        onVehicleClick = { showVehicleSheet = true }
                    )
                }

                // ── Espacio (si el estacionamiento tiene spots) ───────────────
                if (state.spots.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        onClick  = { showSpotSheet = true },
                        color    = Color(0xFFF5F5F5),
                        shape    = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier              = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Espacio",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text  = state.selectedSpot?.let { "Espacio ${it.spotNumber}" }
                                        ?: "Sin preferencia (automático)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = TextPrimary
                                )
                            }
                            Text(
                                "${state.spots.count { it.isAvailable }} disponibles",
                                style = MaterialTheme.typography.labelSmall,
                                color = BlueSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Fechas y horas — named params para evitar ambigüedad ───────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        DateSelector(
                            label       = "Entrada",
                            date        = viewModel.getFormattedEntryDate(),
                            onDateClick = { showEntryDatePicker = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TimeSelector(
                            time        = viewModel.getFormattedEntryTime(),
                            onTimeClick = { showEntryTimePicker = true }
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        DateSelector(
                            label       = "Salida",
                            date        = viewModel.getFormattedExitDate(),
                            onDateClick = { showExitDatePicker = true }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TimeSelector(
                            time        = viewModel.getFormattedExitTime(),
                            onTimeClick = { showExitTimePicker = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PriceSummaryCard(
                    hours          = state.hours,
                    baseCost       = state.baseCost,
                    additionalTime = state.additionalTime,
                    discounts      = state.discounts,
                    total          = state.total
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Método de pago ────────────────────────────────────────────
                PaymentMethodSelector(
                    selectedMethod = if (state.selectedPaymentMethod == "cash") "Efectivo"
                    else "MercadoPago",
                    onMethodClick  = { showPaymentMethodSheet = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Error ─────────────────────────────────────────────────────
                state.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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

                ConfirmReservationButton(
                    isLoading = state.isLoading,
                    enabled   = state.total > 0 && state.selectedVehicle != null,
                    onClick   = { viewModel.confirmReservation() }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // ── Date / Time pickers ───────────────────────────────────────────────────
    if (showEntryDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                viewModel.updateEntryDate(LocalDate.of(year, month + 1, day))
                showEntryDatePicker = false
            },
            state.entryDate.year,
            state.entryDate.monthValue - 1,
            state.entryDate.dayOfMonth
        ).apply { datePicker.minDate = System.currentTimeMillis(); show() }
    }

    if (showExitDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                viewModel.updateExitDate(LocalDate.of(year, month + 1, day))
                showExitDatePicker = false
            },
            state.exitDate.year,
            state.exitDate.monthValue - 1,
            state.exitDate.dayOfMonth
        ).apply { datePicker.minDate = state.entryDate.toEpochDay() * 86400000L; show() }
    }

    if (showEntryTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                viewModel.updateEntryTime(LocalTime.of(hour, minute))
                showEntryTimePicker = false
            },
            state.entryTime.hour,
            state.entryTime.minute,
            true
        ).show()
    }

    if (showExitTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                viewModel.updateExitTime(LocalTime.of(hour, minute))
                showExitTimePicker = false
            },
            state.exitTime.hour,
            state.exitTime.minute,
            true
        ).show()
    }
}