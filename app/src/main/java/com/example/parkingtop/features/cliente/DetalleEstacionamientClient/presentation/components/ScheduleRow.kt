package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun ScheduleRow(day: String, time: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = day, 
            fontSize = 12.sp, 
            color = TextPrimary, 
            fontWeight = FontWeight.Medium, 
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = time, 
            fontSize = 12.sp, 
            color = Color.Gray
        )
    }
}
