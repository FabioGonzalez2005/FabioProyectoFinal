package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun AppointmentsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F2))
            .height(64.dp)
    ) {
        // Navegación superior
        TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }
        // "Buscador"
        Row {
            // "Citas"
            Text(
                text = "Citas",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
            )
            // "Historia"
            Card(
                modifier = Modifier
                    .size(width = 180.dp, height = 45.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Historial:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        // "Confirmadas"
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 45.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Confirmadas:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB2C2A4),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        // "Rechazadas"
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 45.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Rechazadas:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB2C2A4),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}