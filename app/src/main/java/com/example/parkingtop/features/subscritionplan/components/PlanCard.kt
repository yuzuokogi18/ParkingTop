package com.example.parkingtop.features.subscritionplan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
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
