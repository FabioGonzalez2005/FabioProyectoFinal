package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fabioproyectofinal.R

// Tarjeta de profesional
@Composable
fun ProfessionalCard(name: String, specialty: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.icon_user),
            contentDescription = name,
            modifier = Modifier.size(60.dp)
        )
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(specialty, fontSize = 12.sp, color = Color.Gray)
    }
}
