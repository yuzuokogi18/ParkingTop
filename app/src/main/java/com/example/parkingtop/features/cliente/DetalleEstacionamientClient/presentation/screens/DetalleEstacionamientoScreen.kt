package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.R
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components.*
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetalleEstacionamientoScreen(
    onBackClick: () -> Unit = {},
    onReserveClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Estacionamiento Central Park",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextPrimary)
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(TextPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo_parking),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Galería de Imágenes
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    DetailImageCard("Entrada Principal")
                }
                item {
                    DetailImageCard("Interior")
                }
            }

            // Sección Descripción
            DetailSection(title = "Descripción") {
                Text(
                    text = "Un espacio de estacionamiento moderno y seguro en el corazón de la ciudad. Ideal para visitas al centro comercial y oficinas cercanas. Contamos con amplia vigilancia y fácil acceso.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F3F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Mapa de Ubicación", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Calle Falsa 123, Centro, Ciudad Inventada",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Precios y Disponibilidad
            DetailSection(title = "Precios y Disponibilidad") {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Precio Base:", fontSize = 12.sp, color = Color.Gray)
                        Text("$2.50/hora", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BlueSecondary)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hora Extra:", fontSize = 12.sp, color = Color.Gray)
                        Text("$1.50", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BlueSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Espacios Disponibles:", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Outlined.DirectionsCar, contentDescription = null, modifier = Modifier.size(18.dp), tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("15 de 50", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }

            // Horarios y Características
            DetailSection(title = "Horarios") {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        ScheduleRow("Lunes:", "08:00 - 22:00")
                        ScheduleRow("Miércoles:", "08:00 - 22:00")
                        ScheduleRow("Viernes:", "08:00 - 00:00")
                        ScheduleRow("Domingo:", "10:00 - 20:00")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        ScheduleRow("Martes:", "08:00 - 22:00")
                        ScheduleRow("Jueves:", "08:00 - 22:00")
                        ScheduleRow("Sábado:", "10:00 - 00:00")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Características", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureChip("Techado", Icons.Outlined.Info)
                    FeatureChip("CCTV 24/7", Icons.Outlined.CheckCircle)
                    FeatureChip("Abierto 24/7", Icons.Outlined.Schedule)
                    FeatureChip("Wi-Fi", Icons.Outlined.Wifi)
                }
            }

            // Reseñas
            DetailSection(title = "Reseñas (45)") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(4) { Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(18.dp)) }
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("4.7", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(" (45 opiniones)", color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                
                ReviewItem("Ana García", "Excelente ubicación y muy seguro. Siempre encuentro espacio. ¡Recomendado!", "Hace 2 días")
                ReviewItem("Carlos Gómez", "Buen servicio, aunque un poco caro si te quedas muchas horas.", "Hace 1 semana", reply = "Agradecemos su comentario, Carlos. Buscamos ofrecer el mejor servicio y seguridad.")
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN RESERVAR
            Button(
                onClick = onReserveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
            ) {
                Text(
                    text = "Reservar",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
