package com.example.fabioproyectofinal.view.screens

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import com.example.fabioproyectofinal.viewmodel.FavouriteClinicsViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.fabioproyectofinal.view.components.AnimatedDialogButton
import com.example.fabioproyectofinal.view.components.GoogleMapWithClinics

@Composable
fun MainScreenApp(navController: NavHostController, userId: Int?) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // ViewModels y datos observables
    val clinicViewModel: ClinicViewModel = viewModel()
    val clinics by clinicViewModel.clinics.collectAsState()

    val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
    val favoritas by favouritesViewModel.favoritas.collectAsState()

    var searchText by remember { mutableStateOf("") }

    // Cargar clínicas y favoritos al entrar o cambiar el texto de búsqueda
    LaunchedEffect(userId, searchText) {
        val query = searchText.trim()
        if (query.isNotEmpty()) {
            clinicViewModel.buscarClinicas(query)
        } else {
            clinicViewModel.fetchClinics(userId ?: -1)
        }
        userId?.let { favouritesViewModel.fetchFavoritas(it) }
    }

    // Marca las clínicas favoritas en la lista principal
    val clinicsConMarca = remember(clinics, favoritas) {
        val idsFavoritos = favoritas.map { it.id_clinica }.toSet()
        clinics.map { clinica -> clinica.copy(inFavourites = idsFavoritos.contains(clinica.id_clinica)) }
    }

    // Ordenar clínicas mostrando primero las favoritas
    val clinicasFiltradas = clinicsConMarca.sortedByDescending { it.inFavourites }

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
            var showMapDialog by remember { mutableStateOf(false) }

            // Encabezado y botón para ver mapa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Buscador",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    fontFamily = afacadFont,
                )
                Button(
                    onClick = { showMapDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = "Mapa", tint = Color.White)
                }
            }

            // Buscador de clínicas
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar", modifier = Modifier.size(18.dp))
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
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )

            // Lista de clínicas (filtradas)
            ClinicList(clinicasFiltradas, navController, userId = userId ?: -1, showFavouritesOnly = false)

            // Diálogo con el mapa
            if (showMapDialog) {
                AlertDialog(
                    onDismissRequest = { showMapDialog = false },
                    confirmButton = {
                        AnimatedDialogButton(
                            text = "Cerrar",
                            onClick = { showMapDialog = false }
                        )
                    },
                    title = {
                        Text(
                            "Clínicas en el mapa",
                            fontFamily = afacadFont,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    text = {
                        Box(modifier = Modifier.height(450.dp).fillMaxWidth()) {
                            GoogleMapWithClinics(
                                clinics = clinics,
                                navController = navController,
                                userId = userId ?: -1,
                                onDismiss = { showMapDialog = false }
                            )
                        }
                    },
                    containerColor = Color(0xFFFFF9F2)
                )
            }
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
    // Filtra si solo se deben mostrar clínicas favoritas
    val clinicasAMostrar = if (showFavouritesOnly) {
        clinicasFiltradas.filter { it.inFavourites }
    } else {
        clinicasFiltradas
    }

    // Lista visual de clínicas
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
                inFavourites = clinic.inFavourites,
                isClickable = true,
                mostrarIconoVacio = false,
                botonFavoritoActivo = false,
                mostrarBotonSeguros = false
            )
        }
    }
}
