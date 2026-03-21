package com.example.parkingtop.features.subscritionplan.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.R
import com.example.parkingtop.features.subscritionplan.components.FrequencySelector
import com.example.parkingtop.features.subscritionplan.components.PlanCard
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPlanScreen(
    onBackClick: () -> Unit = {},
    onUpgradeClick: () -> Unit = {}
) {
    var selectedFrequency by remember { mutableStateOf("Mensual") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Planes de Suscripción",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TextPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo_parking),
                                contentDescription = "Logo",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Help */ }) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color.Gray)
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "FRECUENCIA DE FACTURACIÓN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Frequency Selector
            FrequencySelector(
                selectedFrequency = selectedFrequency,
                onFrequencySelected = { selectedFrequency = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Plan Básico (Actual)
            PlanCard(
                title = "Básico",
                price = "9.99",
                isCurrent = true,
                features = listOf(
                    "Hasta 2 listados activos" to true,
                    "Visibilidad estándar" to true,
                    "Soporte por email" to true,
                    "Analíticas básicas" to false,
                    "Destacados premium" to false
                ),
                buttonText = "Suscripción Activa",
                onButtonClick = {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plan Pro (Recomendado)
            PlanCard(
                title = "Pro",
                price = "24.99",
                isCurrent = false,
                isRecommended = true,
                icon = Icons.Outlined.Bolt,
                iconColor = Color(0xFFFBC02D),
                features = listOf(
                    "Listados ilimitados" to true,
                    "Visibilidad prioritaria (Boost)" to true,
                    "Soporte 24/7 dedicado" to true,
                    "Analíticas avanzadas" to true,
                    "Insignia de verificado" to true
                ),
                buttonText = "Mejorar a Pro",
                onButtonClick = onUpgradeClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Comparar todos los beneficios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Compare benefits */ },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Comparar todos los beneficios",
                    color = BlueSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = BlueSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Los pagos se procesarán de forma segura. Puedes cancelar tu suscripción en cualquier momento desde los ajustes de tu cuenta.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
