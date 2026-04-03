package com.example.parkingtop.features.subscritionplan.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
                            "Parking Top",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
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
                text = "Elige tu Plan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Text(
                text = "Optimiza la gestión de tus lotes con herramientas avanzadas.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info, 
                    contentDescription = null, 
                    tint = BlueSecondary, 
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Frecuencia de Facturación",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FrequencySelector(
                selectedFrequency = selectedFrequency,
                onFrequencySelected = { selectedFrequency = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Plan Básico
            PlanCard(
                title = "Plan Básico",
                description = "Ideal para empezar",
                price = "300",
                lotes = "1",
                sitios = "50",
                comision = "15%",
                isCurrent = true,
                features = listOf("basic analytics", "email support"),
                trialPeriod = "7 días",
                buttonText = "Plan Actual",
                onButtonClick = onUpgradeClick // Corregido: Ahora navega al home
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plan Premium
            PlanCard(
                title = "Plan Premium",
                description = "Para múltiples estacionamientos",
                price = "600",
                lotes = "5",
                sitios = "∞",
                comision = "12%",
                features = listOf("advanced analytics", "priority support"),
                trialPeriod = "14 días",
                buttonText = "Seleccionar Plan",
                onButtonClick = onUpgradeClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plan Empresarial
            PlanCard(
                title = "Plan Empresarial",
                description = "Solución completa",
                price = "900",
                setupFee = "300",
                lotes = "∞",
                sitios = "∞",
                comision = "10%",
                features = listOf("advanced analytics", "24/7 support", "api access"),
                trialPeriod = "30 días",
                buttonText = "Seleccionar Plan",
                onButtonClick = onUpgradeClick // Corregido: Ahora navega al home
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Plan Banner
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                color = Color(0xFFFFF9F2),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(40.dp).background(Color(0xFFFFECB3), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = Color(0xFFFFB300))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "¿Necesitas algo a medida?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "Contacta con nuestro equipo para planes corporativos personalizados.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
