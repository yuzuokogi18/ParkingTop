package com.parking.parkingtop.features.cliente.Perfil.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun OwnerBalanceCard(
    balance: String,
    onTransferClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$$balance MXN",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Saldo Disponible",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text("💵", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onTransferClick,
                    modifier = Modifier.weight(1.1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, BlueSecondary)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward, 
                        contentDescription = null, 
                        modifier = Modifier.size(18.dp),
                        tint = BlueSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Transferir a mi Cuenta", 
                        fontSize = 11.sp, 
                        fontWeight = FontWeight.Bold,
                        color = BlueSecondary,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.weight(0.9f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, BlueSecondary)
                ) {
                    Icon(
                        Icons.Default.ListAlt, 
                        contentDescription = null, 
                        modifier = Modifier.size(18.dp),
                        tint = BlueSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Historial de Ganancias", 
                        fontSize = 11.sp, 
                        fontWeight = FontWeight.Bold,
                        color = BlueSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
