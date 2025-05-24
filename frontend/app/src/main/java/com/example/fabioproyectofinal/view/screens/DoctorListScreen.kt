package com.example.fabioproyectofinal.view.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.ProfessionalCard
import com.example.fabioproyectofinal.viewmodel.DoctorViewModel

@Composable
fun DoctorListScreen(
    navController: NavHostController,
    userId: Int?,
    viewModel: DoctorViewModel = viewModel()
) {
    // Observa la lista de doctores desde el ViewModel
    val doctorList by viewModel.doctors.collectAsState()

    Log.d("DoctorListScreen", "Pantalla cargada")

    // Contenedor principal en columna
    Column(modifier = Modifier.padding(top = 16.dp)) {

        // Itera sobre cada doctor disponible
        doctorList.forEach { doctor ->
            Log.d("DoctorListScreen", (doctor.id_doctor).toString())

            // Tarjeta visual del profesional (nombre y clínica a la que pertenece)
            ProfessionalCard(
                name = "Doctor ${doctor.id_doctor}",
                specialty = "Clinica ID: ${doctor.id_clinica}",
                navController = navController,
                userId = userId ?: -1,
                onClick = {
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Separación entre tarjetas
        }
    }
}