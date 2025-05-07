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
fun DoctorListScreen(navController: NavHostController, viewModel: DoctorViewModel = viewModel()) {
    val doctorList by viewModel.doctors.collectAsState()
    Log.d("TESTBEBE", "Paantalla cargada")
    Column(modifier = Modifier.padding(top = 16.dp)) {
        doctorList.forEach { doctor ->
            Log.d("TESTBEBE", (doctor.id_doctor).toString())

            ProfessionalCard(

                name = "Doctor ${doctor.id_doctor}",
                specialty = "Clinica ID: ${doctor.id_clinica}",
                navController = navController,
                onClick = {
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
