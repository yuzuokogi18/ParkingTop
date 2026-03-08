package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun ReviewItem(name: String, comment: String, date: String, reply: String? = null) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(text = date, fontSize = 11.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = comment, 
            fontSize = 13.sp, 
            color = TextPrimary, 
            lineHeight = 18.sp
        )
        
        if (reply != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = Color(0xFFF8F9FA),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Respuesta del propietario:", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 11.sp, 
                        color = Color.Gray
                    )
                    Text(
                        text = reply, 
                        fontSize = 12.sp, 
                        color = Color.Gray, 
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
