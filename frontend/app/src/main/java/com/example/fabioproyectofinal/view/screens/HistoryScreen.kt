package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.HistoryCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.AppointmentViewModel
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import com.example.fabioproyectofinal.viewmodel.DoctorViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R

@Composable
fun HistoryScreen(navController: NavHostController, userId: Int?) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val appointments by appointmentViewModel.citas.collectAsState()

    val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH)
    val hoy = java.util.Date()

    val appointmentsCount = appointments.count {
        try {
            val citaDate = sdf.parse(it.fecha_cita)
            citaDate?.before(hoy) == true
        } catch (e: Exception) {
            false
        }
    }

    LaunchedEffect(userId) {
        userId?.let { id ->
            appointmentViewModel.fetchCitas(id)
        }
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // "Citas"
                Text(
                    text = "Historial",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    fontFamily = afacadFont,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                )
            }
            // Texto
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Últimas citas: $appointmentsCount",
                        fontSize = 18.sp,
                        fontFamily = afacadFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            HistoryCardList()
        }
    }
}

@Composable
fun HistoryCardList(navController: NavHostController? = null) {
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val doctorViewModel: DoctorViewModel = viewModel()
    val clinicViewModel: ClinicViewModel = viewModel()

    val citas by appointmentViewModel.citas.collectAsState()
    val doctorList by doctorViewModel.doctors.collectAsState()
    val clinicas by clinicViewModel.clinics.collectAsState()

    val citasPasadas = citas.filter {
        try {
            val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH)
            val citaDate = sdf.parse(it.fecha_cita)
            val hoy = java.util.Date()
            citaDate?.before(hoy) == true
        } catch (e: Exception) {
            false
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(citasPasadas) { cita ->
            val doctor = doctorList.find { it.id_doctor == cita.id_doctor }
            val clinica = doctor?.let { d -> clinicas.find { it.id_clinica == d.id_clinica } }

            HistoryCard(
                appointment = cita,
                doctor = doctor,
                clinic = clinica,
                navController = navController
            )
        }
    }
}