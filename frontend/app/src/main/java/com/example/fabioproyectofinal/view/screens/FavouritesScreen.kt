package com.example.fabioproyectofinal.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.FavouriteClinicsViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.fabioproyectofinal.R

@Composable
fun FavouritesScreen(navController: NavHostController, userId: Int?) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var searchText by remember { mutableStateOf("") }
    val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
    val clinics by favouritesViewModel.favoritas.collectAsState()
    val clinicsConMarca = clinics.map { it.copy(inFavourites = true) }

    LaunchedEffect(searchText, userId) {
        Log.d("FavouritesScreen", "Cargando favoritos para usuario: $userId")
        if (searchText.length >= 3) {
            favouritesViewModel.buscarPorEspecialidadEnFavoritos(searchText)
        } else {
            userId?.let { id ->
                Log.d("FavouritesScreen", "Cargando favoritos para usuario: $id")
                favouritesViewModel.fetchFavoritas(id)
            }
        }
    }


    // Filtrar por búsqueda
    val clinicasFiltradas = clinicsConMarca.filter {
        Log.d("FiltroEspecialidad", "especialidad de ${it.nombre}: ${it.especialidad}")
        it.nombre.contains(searchText, ignoreCase = true) ||
                it.direccion.contains(searchText, ignoreCase = true) ||
                it.especialidad?.contains(searchText, ignoreCase = true) == true
    }

    Scaffold(
        topBar = { TopBar(navController = navController) {} },
        bottomBar = { BottomBar(navController = navController, userId = userId ?: -1) },
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .padding(innerPadding)
        ) {
            Text(
                text = "Favoritos",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                fontFamily = afacadFont,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar", modifier = Modifier.size(18.dp))
                },
                placeholder = {
                    Text("Clínica, especialidad o dirección", fontSize = 18.sp, fontFamily = afacadFont, color = Color(0xFFB2C2A4))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
            )

            ClinicList(clinicasFiltradas, navController, userId = userId ?: -1, true)
        }
    }
}
