package com.example.parkingtop.features.subscritionplan.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Bolt
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
import com.example.parkingtop.R
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

@Composable
fun FrequencySelector(
    selectedFrequency: String,
    onFrequencySelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = Color(0xFFF8F9FA),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Semanal", "Mensual", "Anual").forEach { freq ->
                val isSelected = selectedFrequency == freq
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { onFrequencySelected(freq) }
                        .then(if (isSelected) Modifier.border(0.5.dp, Color(0xFFEEEEEE), RoundedCornerShape(6.dp)) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = freq,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextPrimary else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    isCurrent: Boolean = false,
    isRecommended: Boolean = false,
    icon: ImageVector? = null,
    iconColor: Color = Color.Unspecified,
    features: List<Pair<String, Boolean>>,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(if (isRecommended) 2.dp else 1.dp, if (isRecommended) BlueSecondary else Color(0xFFEEEEEE))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                    } else if (isCurrent) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = BlueSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                if (isCurrent) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "Plan Actual",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43A047)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                    Text(price, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Text("/mes", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF5F5F5))

                features.forEach { (feature, isIncluded) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isIncluded) BlueSecondary.copy(alpha = 0.3f) else Color(0xFFEEEEEE),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            color = if (isIncluded) TextPrimary else Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isCurrent) {
                    OutlinedButton(
                        onClick = onButtonClick,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                    ) {
                        Text(buttonText, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Button(
                        onClick = onButtonClick,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
                    ) {
                        Text(buttonText, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        if (isRecommended) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                color = BlueSecondary,
                shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RECOMENDADO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}