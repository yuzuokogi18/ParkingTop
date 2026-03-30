package com.example.parkingtop.features.cliente.CreateVehicleClient.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary


@Composable
fun VehicleField(
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
            focusedTextColor    = TextPrimary,
            unfocusedTextColor  = TextPrimary,
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedBorderColor  = BlueSecondary
        )
    )
}