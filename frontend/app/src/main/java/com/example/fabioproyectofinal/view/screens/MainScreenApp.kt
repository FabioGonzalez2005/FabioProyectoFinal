package com.example.fabioproyectofinal.view.screens

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
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.example.fabioproyectofinal.view.components.AnimatedDialogButton
import com.example.fabioproyectofinal.view.components.GoogleMapWithClinics

@Composable
fun MainScreenApp(navController: NavHostController, userId: Int?) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val clinicViewModel: ClinicViewModel = viewModel()
    val clinics by clinicViewModel.clinics.collectAsState()

    var searchText by remember { mutableStateOf("") }

    val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
    val favoritas by favouritesViewModel.favoritas.collectAsState()

// Cargar favoritos al entrar
    LaunchedEffect(userId, searchText) {
        val query = searchText.trim()
        if (query.isNotEmpty()) {
            clinicViewModel.buscarClinicas(query)
        } else {
            clinicViewModel.fetchClinics(usuario_id = userId ?: -1)
        }
        userId?.let { id ->
            favouritesViewModel.fetchFavoritas(id)
        }
    }

// Marcar favoritas
    val clinicsConMarca = remember(clinics, favoritas) {
        val idsFavoritos = favoritas.map { it.id_clinica }.toSet()
        clinics.map { clinica ->
            clinica.copy(inFavourites = idsFavoritos.contains(clinica.id_clinica))
        }
    }

// Filtrar por búsqueda
    val clinicasFiltradas = clinicsConMarca.sortedByDescending { it.inFavourites }
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
            var showMapDialog by remember { mutableStateOf(false) }
            // "Buscador"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
            Text(
                text = "Buscador",
                color = Color(0xFFB2C2A4),
                fontSize = 40.sp,
                fontFamily = afacadFont,
            )
                Button(
                    onClick = {
                        showMapDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Mapa",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }



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
                            fontSize = 18.sp,
                            color = Color(0xFF7C8B6B),
                            fontWeight = FontWeight.Bold,
                            fontFamily = afacadFont
                        )
                    },
                    text = {
                        Box(modifier = Modifier
                            .height(450.dp)
                            .fillMaxWidth()
                        ) {
                            GoogleMapWithClinics(
                                clinics = clinics,
                                navController = navController,
                                userId = userId ?: -1,
                                onDismiss = { showMapDialog = false }
                            )
                        }
                    }
                    ,
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
                inFavourites = clinic.inFavourites,
                isClickable = true,
                mostrarIconoVacio = false,
                botonFavoritoActivo = false,
                mostrarBotonSeguros = false
            )
        }
    }
}
