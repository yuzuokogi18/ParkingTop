package com.example.parkingtop.features.cliente.ReservaClient.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.DateTimePickerField
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.PriceRow
import com.example.parkingtop.features.cliente.ReservaClient.presentation.components.ReservationSection
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaClientScreen(
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reservar Estacionamiento",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ReservationSection(title = "Seleccionar Vehículo") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE9EEF1),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Toyota Corolla (ABC-123)", modifier = Modifier.weight(1f), color = TextPrimary)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Modelo: Corolla", fontSize = 13.sp, color = Color.Gray)
                Text("Matrícula: ABC-123", fontSize = 13.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha y Hora
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    ReservationSection(title = "Entrada") {
                        DateTimePickerField(value = "2024-08-15", icon = Icons.Default.CalendarToday)
                        Spacer(modifier = Modifier.height(8.dp))
                        DateTimePickerField(value = "10:00", icon = Icons.Default.Schedule)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ReservationSection(title = "Salida") {
                        DateTimePickerField(value = "2024-08-15", icon = Icons.Default.CalendarToday)
                        Spacer(modifier = Modifier.height(8.dp))
                        DateTimePickerField(value = "14:00", icon = Icons.Default.Schedule)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de Precios
            ReservationSection(title = "Resumen de Precios") {
                PriceRow(label = "Precio Base (4 horas)", value = "$8.00")
                PriceRow(label = "Tiempo Adicional", value = "$0.00")
                PriceRow(label = "Descuentos", value = "-$0.00")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BlueSecondary)
                    Text("$8.00", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BlueSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            ReservationSection(title = "Método de Pago") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE9EEF1),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Tarjeta de Crédito", modifier = Modifier.weight(1f), color = TextPrimary)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
            ) {
                Text("Confirmar reserva", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
