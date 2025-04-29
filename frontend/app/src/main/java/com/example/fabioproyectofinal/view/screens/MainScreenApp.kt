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
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun MainScreenApp(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val clinicasFiltradas = clinics.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.address.contains(searchText, ignoreCase = true)
    }
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
            Text(
                text = "Buscador",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
            )

            // Buscador de clínicas
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Buscar",
                        modifier = Modifier.size(18.dp)
                    )
                },
                placeholder = {
                    Text(
                        "Nombre clínica o dirección",
                        fontSize = 18.sp,
                        color = Color(0xFFB2C2A4),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
            ClinicList(clinicasFiltradas, navController)
        }
    }
}


@Composable
fun ClinicList(clinicasFiltradas: List<Clinic>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(clinicasFiltradas) { clinic ->
            ClinicaCard(clinic = clinic, navController = navController)
        }
    }
}