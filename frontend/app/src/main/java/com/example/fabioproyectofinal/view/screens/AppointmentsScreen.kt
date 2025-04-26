package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.Clinic
import com.example.fabioproyectofinal.model.data.clinics
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun AppointmentsScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val clinicasFiltradas = clinics.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.address.contains(searchText, ignoreCase = true)
    }

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
            Text(
                text = "Citas",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
            )
            Text(
                text = "Historial",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
            )
        }
        Text(
            text = "Confirmadas",
            color = Color(0xFFB2C2A4),
            fontSize = 40.sp,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
        )
    }
}