package com.example.parkingtop.features.cliente.EditVehicleClient.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary


@Composable
fun EditVehicleField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String
) {
    Text(
        label,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        color = TextPrimary
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = { Text(placeholder, color = Color.LightGray) },
        modifier      = Modifier.fillMaxWidth(),
        shape         = RoundedCornerShape(12.dp),
        singleLine    = true,
        colors        = OutlinedTextFieldDefaults.colors(
            focusedTextColor     = TextPrimary,
            unfocusedTextColor   = TextPrimary,
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedBorderColor   = BlueSecondary
        )
    )
}