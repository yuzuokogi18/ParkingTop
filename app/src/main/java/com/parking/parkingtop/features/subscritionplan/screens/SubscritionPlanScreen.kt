package com.parking.parkingtop.features.subscritionplan.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.parking.parkingtop.R
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parking.parkingtop.features.subscritionplan.components.FrequencySelector
import com.parking.parkingtop.features.subscritionplan.components.PlanCard
import com.parking.parkingtop.features.subscritionplan.viewmodels.SubscritionPlanViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPlanScreen(
    onBackClick: () -> Unit = {},
    onUpgradeClick: () -> Unit = {},
    viewModel: SubscritionPlanViewModel = hiltViewModel()
) {
    var selectedFrequency by remember { mutableStateOf("Mensual") }
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val plans by viewModel.plans.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.paymentUrl.collectLatest { url ->
            if (url == "navigate_home") {
                onUpgradeClick()
            } else if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

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
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                if (plans.isEmpty() && !isLoading) {
                    // Fallback a planes estáticos si no hay planes en la API o error
                    StaticPlans(onUpgradeClick = onUpgradeClick, onPlanSelect = { viewModel.selectPlan(it) })
                } else {
                    plans.forEach { plan ->
                        PlanCard(
                            title = plan.name,
                            description = plan.description,
                            price = plan.monthlyPrice,
                            lotes = if (plan.name.contains("Básico", true)) "1" else if (plan.name.contains("Premium", true)) "5" else "∞",
                            sitios = if (plan.name.contains("Básico", true)) "50" else "∞",
                            comision = if (plan.name.contains("Básico", true)) "15%" else if (plan.name.contains("Premium", true)) "12%" else "10%",
                            features = plan.features,
                            trialPeriod = "7 días",
                            buttonText = "Seleccionar Plan",
                            onButtonClick = { viewModel.selectPlan(plan.id) }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

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

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BlueSecondary)
            }

            errorMessage?.let { error ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Error") },
                    text = { Text(error) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StaticPlans(onUpgradeClick: () -> Unit, onPlanSelect: (String) -> Unit) {
    // Plan Básico
    PlanCard(
        title = "Plan Básico",
        description = "Ideal para empezar",
        price = "300",
        lotes = "1",
        sitios = "50",
        comision = "15%",
        features = listOf("Gestión de 1 lote", "Hasta 50 sitios", "Soporte por email"),
        trialPeriod = "7 días",
        buttonText = "Seleccionar Plan",
        onButtonClick = { onPlanSelect("basic_id") }
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
        features = listOf("Gestión de 5 lotes", "Sitios ilimitados", "Soporte prioritario"),
        trialPeriod = "14 días",
        buttonText = "Seleccionar Plan",
        onButtonClick = { onPlanSelect("premium_id") }
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
        features = listOf("Lotes ilimitados", "Soporte 24/7", "Acceso a API"),
        trialPeriod = "30 días",
        buttonText = "Seleccionar Plan",
        onButtonClick = { onPlanSelect("enterprise_id") }
    )
}
