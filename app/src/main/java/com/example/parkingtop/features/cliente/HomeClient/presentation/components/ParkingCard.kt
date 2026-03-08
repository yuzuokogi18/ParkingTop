package com.example.parkingtop.features.cliente.HomeClient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.ui.theme.TextPrimary

data class ParkingItem(
    val name: String,
    val distance: String,
    val price: String,
    val status: String,
    val rating: Float,
    val reviews: Int
)

@Composable
fun ParkingCard(parking: ParkingItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                // Placeholder image
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                ) {
                    Text(parking.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(parking.distance, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("€${parking.price}/h", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                    Surface(
                        color = if (parking.status == "Libre") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            parking.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (parking.status == "Libre") Color(0xFF43A047) else Color(0xFFE53935)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${parking.rating} (${parking.reviews} opiniones)", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}
