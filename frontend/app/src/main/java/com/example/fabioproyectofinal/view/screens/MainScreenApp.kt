package com.example.fabioproyectofinal.view.screens

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import com.example.fabioproyectofinal.viewmodel.FavouriteClinicsViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MainScreenApp(navController: NavHostController, userId: Int?) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val clinicViewModel: ClinicViewModel = viewModel()
    val clinics by clinicViewModel.clinics.collectAsState()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        if (searchText.length >= 3) {
            clinicViewModel.buscarPorEspecialidad(searchText)
        } else {
            clinicViewModel.fetchClinics()
        }
    }

    val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
    val favoritas by favouritesViewModel.favoritas.collectAsState()

// Cargar favoritos al entrar
    LaunchedEffect(userId) {
        userId?.let { id ->
            favouritesViewModel.fetchFavoritas(id)
        }
    }

// Marcar favoritas
    val idsFavoritos = favoritas.map { it.id_clinica }.toSet()
    val clinicsConMarca = clinics.map { clinica ->
        clinica.copy(inFavourites = clinica.id_clinica in idsFavoritos)
    }

// Filtrar por búsqueda
    val clinicasFiltradas = clinicsConMarca.filter {
        Log.d("FiltroEspecialidad", "especialidad de ${it.nombre}: ${it.especialidad}")
        it.nombre.contains(searchText, ignoreCase = true) ||
                it.direccion.contains(searchText, ignoreCase = true) ||
                it.especialidad?.contains(searchText, ignoreCase = true) == true
    }
    Scaffold(
        topBar = {
            TopBar(navController = navController) { /* Acción */ }
        },
        bottomBar = {
            BottomBar(navController = navController, userId = userId ?: -1)
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
                fontFamily = afacadFont,
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
                        "Clínica, especialidad o dirección",
                        fontFamily = afacadFont,
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
            ClinicList(clinicasFiltradas, navController, userId = userId ?: -1, false)
        }
    }
}


@Composable
fun ClinicList(
    clinicasFiltradas: List<Clinic>,
    navController: NavHostController,
    userId: Int?,
    showFavouritesOnly: Boolean
) {
    val clinicasAMostrar = if (showFavouritesOnly) {
        clinicasFiltradas.filter { it.inFavourites }
    } else {
        clinicasFiltradas
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(clinicasAMostrar) { clinic ->
            ClinicaCard(
                clinic = clinic,
                navController = navController,
                userId = userId ?: -1,
                inFavourites = clinic.inFavourites
            )
        }
    }
}
