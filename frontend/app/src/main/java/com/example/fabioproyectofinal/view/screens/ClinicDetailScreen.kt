package com.example.fabioproyectofinal.view.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.ClinicActionButton
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.ProfessionalCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.fabioproyectofinal.viewmodel.DoctorViewModel

@Composable
fun ClinicDetailScreen(navController: NavHostController, viewModel: DoctorViewModel = viewModel()) {
    val clinicViewModel: ClinicViewModel = viewModel()
    val clinics by clinicViewModel.clinics.collectAsState()
    val doctorList by viewModel.doctors.collectAsState()
    val context = LocalContext.current
    val clinic = clinics.firstOrNull()
    val filteredDoctors = clinic?.let { currentClinic ->
        doctorList.filter { it.id_clinica == currentClinic.id_clinica }
    } ?: emptyList()

    Scaffold(
        topBar = {
            TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }
        },
        bottomBar = {
            BottomBar(navController = navController)
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
                    ClinicaCard(clinic = it, navController = navController, inFavourites = true)
                }
            }
            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClinicActionButton("Dirección", R.drawable.icon_map) { /* Acción */ }
                ClinicActionButton("Llamar", R.drawable.icon_call) {
                    clinic?.let {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = "tel:+34${it.telefono}".toUri()
                        context.startActivity(intent)
                    }
                }

                ClinicActionButton("Web", R.drawable.webpage) {
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
                                        ProfessionalCard(
                                            name = doctor.nombre,
                                            specialty = doctor.especialidad,
                                            navController = navController
                                        ) { /* Acción */ }
                                        Log.d("DEBUG", "ID clínica actual: ${clinic?.id_clinica}")
                                        doctorList.forEach {
                                            Log.d("DEBUG", "Doctor ${it.nombre}, id_clinica: ${it.id_clinica}")
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
}