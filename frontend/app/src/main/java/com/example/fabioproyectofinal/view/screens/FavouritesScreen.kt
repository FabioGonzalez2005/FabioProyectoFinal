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
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.FavouriteClinicsViewModel

@Composable
fun FavouritesScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
    val clinics by favouritesViewModel.favoritas.collectAsState()
    val clinicsConMarca = clinics.map { it.copy(inFavourites = true) }

    LaunchedEffect(Unit) {
        SessionManager.idUsuario?.let { id ->
            Log.d("FavouritesScreen", "Cargando favoritos para usuario: $id")
            favouritesViewModel.fetchFavoritas(id)
        }
    }

    val clinicasFiltradas = clinicsConMarca.filter {
        it.nombre.contains(searchText, ignoreCase = true) ||
                it.direccion.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopBar(navController = navController) {} },
        bottomBar = { BottomBar(navController = navController) },
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
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar", modifier = Modifier.size(18.dp))
                },
                placeholder = {
                    Text("Nombre clínica o dirección", fontSize = 18.sp, color = Color(0xFFB2C2A4))
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

            ClinicList(clinicasFiltradas, navController, true)
        }
    }
}
