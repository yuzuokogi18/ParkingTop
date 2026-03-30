package com.example.parkingtop.features.reservations.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.ConfirmReservationButton
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.DateSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PaymentMethodSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PriceSummaryCard
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.TimeSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.VehicleSelector
import com.example.parkingtop.features.cliente.ReservaClient.presentation.viewmodels.ReservationViewModel
import com.example.parkingtop.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    onBack: () -> Unit = {},
    onPaymentSuccess: (String) -> Unit = {},
    viewModel: ReservationViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    // Diálogos de fecha y hora
    var showEntryDatePicker by remember { mutableStateOf(false) }
    var showEntryTimePicker by remember { mutableStateOf(false) }
    var showExitDatePicker by remember { mutableStateOf(false) }
    var showExitTimePicker by remember { mutableStateOf(false) }

    // Manejar éxito de reserva
    LaunchedEffect(state.reservationSuccess) {
        if (state.reservationSuccess) {
            state.paymentUrl?.let { onPaymentSuccess(it) }
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Reservar Estacionamiento",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFEEEEEE)
                )
            }
        }
    ) { innerPadding ->

        if (state.isLoading && state.parkingLot == null) {
            // Loading inicial
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Selector de vehículo
                state.selectedVehicle?.let { vehicle ->
                    VehicleSelector(
                        selectedVehicle = "${vehicle.brand} ${vehicle.model} (${vehicle.licensePlate})",
                        vehicleModel = vehicle.model,
                        vehiclePlate = vehicle.licensePlate,
                        onVehicleClick = { /* TODO: Mostrar bottom sheet de vehículos */ }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Fecha y hora de entrada/salida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Entrada
                    Column(modifier = Modifier.weight(1f)) {
                        DateSelector(
                            label = "Fecha y Hora de Entrada",
                            date = viewModel.getFormattedEntryDate(),
                            onDateClick = { showEntryDatePicker = true }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TimeSelector(
                            time = viewModel.getFormattedEntryTime(),
                            onTimeClick = { showEntryTimePicker = true }
                        )
                    }

                    // Salida
                    Column(modifier = Modifier.weight(1f)) {
                        DateSelector(
                            label = "Fecha y Hora de Salida",
                            date = viewModel.getFormattedExitDate(),
                            onDateClick = { showExitDatePicker = true }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TimeSelector(
                            time = viewModel.getFormattedExitTime(),
                            onTimeClick = { showExitTimePicker = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Resumen de precios
                PriceSummaryCard(
                    hours = state.hours,
                    baseCost = state.baseCost,
                    additionalTime = state.additionalTime,
                    discounts = state.discounts,
                    total = state.total
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Método de pago
                PaymentMethodSelector(
                    selectedMethod = state.selectedPaymentMethod,
                    onMethodClick = { /* TODO: Mostrar opciones de pago */ }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de confirmar
                ConfirmReservationButton(
                    isLoading = state.isLoading,
                    enabled = state.total > 0 && state.selectedVehicle != null,
                    onClick = { viewModel.confirmReservation() }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Snackbar de error
        state.error?.let { error ->
            LaunchedEffect(error) {
                // Mostrar snackbar o dialog
                viewModel.resetError()
            }
        }
    }

    // Date Pickers
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
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
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
        ).apply {
            datePicker.minDate = state.entryDate.toEpochDay() * 24 * 60 * 60 * 1000
            show()
        }
    }

    // Time Pickers
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