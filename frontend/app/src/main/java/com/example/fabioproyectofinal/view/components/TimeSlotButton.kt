package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Botón con horarios disponibles
@Composable
fun TimeSlotButton(
    time: String,
    isSelected: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !isAvailable -> Color(0xFFD32F2F) // Rojo cuando no está disponible
        isSelected -> Color(0xFF859A72)   // Verde oscuro cuando está seleccionado
        else -> Color(0xFFB2C2A4)         // Verde claro cuando está disponible pero no seleccionado
    }
    Box(
        modifier = Modifier
            .size(width = 188.dp, height = 42.dp)
            .padding(horizontal = 4.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = isAvailable) { onClick() }, // Solo clickeable si está disponible
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
