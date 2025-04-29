package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.fabioproyectofinal.model.data.appointments
import com.example.fabioproyectofinal.model.data.pastAppointments
import com.example.fabioproyectofinal.view.components.AppointmentCard
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.HistoryCard
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun HistoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = Color(0xFFFFF9F2) // Fondo para toda la pantalla
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .height(64.dp)
                .padding(innerPadding)
        ) {
            // "Buscador"
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // "Citas"
                Text(
                    text = "Historial",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                )
            }
            // Texto
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Últimas citas:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            HistoryCardList()
        }
    }
}

@Composable
fun HistoryCardList(navController: NavHostController? = null) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(pastAppointments) { history ->
            HistoryCard(history = history, navController = navController)
        }
    }
}
