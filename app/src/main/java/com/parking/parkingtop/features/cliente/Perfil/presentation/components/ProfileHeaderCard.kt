package com.parking.parkingtop.features.cliente.Perfil.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun ProfileHeaderCard(
    name: String,
    email: String,
    profileImageUrl: String? = null,
    onEditClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}   // ✅ tap en el avatar → abre cámara/galería
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier            = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Avatar clickable con badge de cámara ──────────────────────────
            Box(
                modifier = Modifier.clickable { onAvatarClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUrl != null) {
                        AsyncImage(
                            model              = profileImageUrl,
                            contentDescription = "Foto de perfil",
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Person, null,
                            tint     = BlueSecondary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Punto verde online
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )

                // ✅ Badge de cámara — indica que se puede tocar para cambiar foto
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(BlueSecondary)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint     = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onEditClick,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                    border   = BorderStroke(
                        1.dp, BlueSecondary.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(Icons.Outlined.Edit, null, Modifier.size(16.dp), tint = BlueSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar Perfil", color = BlueSecondary, fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick  = onSettingsClick,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                    border   = BorderStroke(
                        1.dp, BlueSecondary.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(Icons.Outlined.Settings, null, Modifier.size(16.dp), tint = BlueSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajustes", color = BlueSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}