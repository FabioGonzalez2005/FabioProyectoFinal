package com.example.fabioproyectofinal.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.view.components.AppointmentCard
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.viewmodel.AppointmentViewModel
import com.example.fabioproyectofinal.viewmodel.ClinicViewModel
import com.example.fabioproyectofinal.viewmodel.DoctorViewModel
import com.example.fabioproyectofinal.viewmodel.LoginViewModel

@Composable
fun AppointmentsScreen(navController: NavHostController, loginViewModel: LoginViewModel = viewModel()) {
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val appointments by appointmentViewModel.citas.collectAsState()

    val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH)
    val hoy = java.util.Date()

    val citasFuturas = appointments.filter {
        try {
            val citaDate = sdf.parse(it.fecha_cita)
            citaDate?.after(hoy) == true
        } catch (e: Exception) {
            false
        }
    }

    val confirmedCount = citasFuturas.count { it.estado == "Confirmado" }
    val pendingCount = citasFuturas.count { it.estado == "Pendiente" }
    val cancelledCount = citasFuturas.count { it.estado == "Cancelado" }

    val loginEstado by loginViewModel.loginEstado.collectAsState()

    LaunchedEffect(loginEstado) {
        SessionManager.idUsuario?.let { idUsuario ->
            Log.d("AppointmentsScreen", "Llamando a fetchCitas con id: $idUsuario")
            appointmentViewModel.fetchCitas(idUsuario)
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController) { /* Acción */ }
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
                    text = "Citas",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                )
                // "Historial"
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(width = 120.dp, height = 45.dp)
                        .padding(end = 16.dp)
                        .clickable { navController.navigate(route = AppScreens.HistoryScreen.route) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Historial",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB2C2A4),
                        )
                    }
                }
            }

            // Confirmadas
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
                        text = "Confirmadas: $confirmedCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Pendientes
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
                        text = "Pendientes: $pendingCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Canceladas
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
                        text = "Canceladas: $cancelledCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            AppointmentList(navController = navController)
        }
    }
}

@Composable
fun AppointmentList(
    navController: NavHostController? = null,
    viewModel: DoctorViewModel = viewModel()
) {
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val clinicViewModel: ClinicViewModel = viewModel()

    val allCitas by appointmentViewModel.citas.collectAsState()

    val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH)
    val hoy = java.util.Date()

    val citas = allCitas.filter {
        try {
            val citaDate = sdf.parse(it.fecha_cita)
            citaDate?.after(hoy) == true
        } catch (e: Exception) {
            false
        }
    }

    val doctorList by viewModel.doctors.collectAsState()
    val clinicas by clinicViewModel.clinics.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(citas) { cita: Appointment ->
            val doctor: Doctor? = doctorList.find { it.id_doctor == cita.id_doctor }
            val clinica: Clinic? = doctor?.let { doc ->
                clinicas.find { it.id_clinica == doc.id_clinica }
            }

            AppointmentCard(
                appointment = cita,
                doctor = doctor,
                clinic = clinica,
                navController = navController
            )
        }
    }
}
