package com.example.parkingtop.features.propetario.horariopropetario.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.CalendarDay
import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.DayStatus
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun AvailabilityCalendar(
    days: List<CalendarDay>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                }
                Text(
                    text = "febrero de 2026",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                val weekDays = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val emptyCellsBefore = 6
            
            Column {
                var currentDayIndex = 0
                for (row in 0..4) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0..6) {
                            val absoluteIndex = row * 7 + col
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                                if (absoluteIndex >= emptyCellsBefore && currentDayIndex < days.size) {
                                    DayCell(day = days[currentDayIndex])
                                    currentDayIndex++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayCell(day: CalendarDay) {
    val isSelected = day.day == 11
    val borderColor = if (isSelected) BlueSecondary else Color(0xFFEEEEEE)
    
    Column(
        modifier = Modifier
            .fillMaxSize(0.9f)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.day.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = TextPrimary
        )

        when (day.status) {
            DayStatus.PARTIAL -> {
                Text(
                    text = "${day.occupiedSpaces}/${day.totalSpaces}",
                    fontSize = 8.sp,
                    color = BlueSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            DayStatus.FREE -> {
                Text(
                    text = "Libre",
                    fontSize = 8.sp,
                    color = Color.Gray
                )
            }
            DayStatus.BLOCKED -> {
                Text(
                    text = "Bloq.",
                    fontSize = 8.sp,
                    color = Color(0xFFFFA000),
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {}
        }
    }
}
