package com.example.fabioproyectofinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.Clinic
import com.example.fabioproyectofinal.model.data.clinics
import com.example.fabioproyectofinal.model.navigation.AppScreens

@Composable
fun MainScreenApp(navController: NavHostController) {
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
        TopBar()
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

@Composable
fun ClinicList(clinicasFiltradas: List<Clinic>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(clinicasFiltradas) { clinic ->
            ClinicaItem(clinic = clinic, navController = navController)
        }
    }
}

// Barra de navegación superior
@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFB2C2A4),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ESP ▼", color = Color.White)
                Text("(logo)", color = Color.White)
                Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
            }
        }
    }
}

// Tarjeta de clínica
@Composable
fun ClinicaItem(clinic: Clinic, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = AppScreens.ClinicDetailScreen.route)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = clinic.src),
                contentDescription = clinic.name,
                modifier = Modifier
                    .size(110.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(clinic.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(clinic.address, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}


// Barra de navegación inferior
@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F2))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFB2C2A4),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favoritos", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario", modifier = Modifier.size(32.dp), tint = Color.White)
                }
            }
        }
    }
}