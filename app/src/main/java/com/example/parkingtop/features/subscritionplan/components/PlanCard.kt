package com.example.parkingtop.features.subscritionplan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun PlanCard(
    title: String,
    description: String,
    price: String,
    lotes: String,
    sitios: String,
    comision: String,
    isCurrent: Boolean = false,
    setupFee: String? = null,
    features: List<String>,
    trialPeriod: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(if (isCurrent) 2.dp else 1.dp, if (isCurrent) BlueSecondary else Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Title and Active status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (isCurrent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF43A047),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Activo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price and Setup Fee
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 6.dp))
                    Text(price, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Text(" / mes", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 6.dp))
                }
                
                if (setupFee != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "SETUP: $$setupFee",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatBox(modifier = Modifier.weight(1f), label = "LOTES", value = lotes, icon = Icons.Default.Inventory2)
                StatBox(modifier = Modifier.weight(1f), label = "SITIOS", value = sitios, icon = Icons.Default.LocalParking)
                StatBox(modifier = Modifier.weight(1f), label = "COMISIÓN", value = comision, icon = Icons.Default.Percent)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Features Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CARACTERÍSTICAS INCLUIDAS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(BlueSecondary, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = feature, fontSize = 14.sp, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trial period info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Periodo de prueba: ",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = trialPeriod,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button
            if (isCurrent) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8F9FA)),
                    enabled = false
                ) {
                    Text("Plan Actual", fontWeight = FontWeight.Bold, color = Color.LightGray)
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
}

@Composable
fun StatBox(modifier: Modifier = Modifier, label: String, value: String, icon: ImageVector) {
    Surface(
        modifier = modifier.height(70.dp),
        color = Color(0xFFF1F8FE),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}
