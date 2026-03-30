package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ReviewDTO
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun ReviewItem(
    review: ReviewDTO,
    reply: String? = null
) {

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

                Text(
                    text = review.user.fullName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    repeat(review.rating) {

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(14.dp)
                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.comment ?: "Sin comentario",
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