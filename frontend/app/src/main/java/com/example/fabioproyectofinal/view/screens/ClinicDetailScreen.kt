package com.example.fabioproyectofinal.view.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.ClinicActionButton
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.ProfessionalCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.fabioproyectofinal.viewmodel.DoctorViewModel
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.example.fabioproyectofinal.view.components.GoogleMapView
import com.example.fabioproyectofinal.viewmodel.FavouriteClinicsViewModel

@Composable
fun ClinicDetailScreen(
    navController: NavHostController,
    userId: Int?,
    idClinica: Int?,
    viewModel: DoctorViewModel = viewModel()
)
 {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val clinicViewModel: ClinicViewModel = viewModel()
    val clinics by clinicViewModel.clinics.collectAsState()
    val doctorList by viewModel.doctors.collectAsState()
    val context = LocalContext.current
     LaunchedEffect(userId) {
         if (userId != null) {
             clinicViewModel.fetchClinics(userId)
         }
     }
     val favouritesViewModel: FavouriteClinicsViewModel = viewModel()
     val favoritas by favouritesViewModel.favoritas.collectAsState()
     val showMapDialog = remember { mutableStateOf(false) }
     val clinic = clinics.firstOrNull { it.id_clinica == idClinica }

     val estaEnFavoritos by remember(clinic, favoritas) {
         derivedStateOf {
             clinic != null && favoritas.any { it.id_clinica == clinic.id_clinica }
         }
     }
     val filteredDoctors = clinic?.let { currentClinic ->
        doctorList.filter { it.id_clinica == currentClinic.id_clinica }
    } ?: emptyList()



     LaunchedEffect(userId) {
         userId?.let { id ->
             favouritesViewModel.fetchFavoritas(id)
         }
     }

    Scaffold(
        topBar = {
            TopBar(navController = navController) { /* Acción */ }
        },
        bottomBar = {
            BottomBar(navController = navController, userId = userId ?: -1)
        },
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                clinic?.let {
                    ClinicaCard(
                        clinic = it,
                        navController = navController,
                        userId = userId ?: -1,
                        inFavourites = estaEnFavoritos,
                        isClickable = false,
                        mostrarIconoVacio = true,
                        botonFavoritoActivo = true,
                        mostrarCompatibilidad = false,
                    )
                }
            }
            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClinicActionButton("Ubicación", "https://res.cloudinary.com/dr8es2ate/image/upload/icon_map_fc9rco.webp") {
                    showMapDialog.value = true
                }
                ClinicActionButton("Llamar", "https://res.cloudinary.com/dr8es2ate/image/upload/icon_call_qbnahd.webp") {
                    clinic?.let {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = "tel:+34${it.telefono}".toUri()
                        context.startActivity(intent)
                    }
                }

                ClinicActionButton("Web", "https://res.cloudinary.com/dr8es2ate/image/upload/icon_web_yygyly.webp") {
                    clinic?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.web))
                        context.startActivity(intent)
                    }
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título sección profesionales
                        Text(
                            "Escoge profesional:",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = afacadFont,
                            fontSize = 16.sp,
                            color = Color(0xFFB2C2A4)
                        )
                        Spacer(modifier = Modifier.size(8.dp))

                        LazyColumn {
                            items(filteredDoctors.chunked(2)) { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    pair.forEach { doctor ->
                                        Log.i("DoctorId", "IdDoctor ${doctor.id_doctor}")
                                        ProfessionalCard(
                                            name = doctor.nombre,
                                            specialty = doctor.especialidad,
                                            navController = navController,
                                            userId = userId ?: -1

                                        ) {

                                            navController.navigate(
                                                AppScreens.SelectProfessionalScreen.createRoute(
                                                    idUsuario = userId ?: -1,
                                                    idDoctor = doctor.id_doctor
                                                )
                                            )
                                        }
                                        Log.d("DEBUG", "ID clínica actual: ${clinic?.id_clinica}")
                                        doctorList.forEach {
                                            Log.d(
                                                "DEBUG",
                                                "Doctor ${it.nombre}, id_clinica: ${it.id_clinica}"
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
    if (showMapDialog.value) {
        clinic?.let { c ->
            Log.d("MAPA", "Latitud: ${c.lat}, Longitud: ${c.lng}")
            AlertDialog(
                onDismissRequest = { showMapDialog.value = false },
                confirmButton = {
                    Button(
                        onClick = { showMapDialog.value = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB2C2A4)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .width(160.dp)
                    ) {
                        Text("Cerrar", color = Color.White, fontFamily = afacadFont,)
                    }
                },
                title = {
                    Text(
                        text = "Ubicación",
                        color = Color(0xFFB2C2A4),
                        fontSize = 20.sp,
                        fontFamily = afacadFont,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFF9F2))
                            .padding(top = 8.dp)
                    ) {
                        GoogleMapView(lat = c.lat, lng = c.lng)
                    }
                },
                containerColor = Color(0xFFFFF9F2)
            )
        }
    }
}

