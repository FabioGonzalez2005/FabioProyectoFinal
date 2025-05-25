package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    val clinicViewModel: ClinicViewModel = viewModel()
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

    var selectedOption by remember { mutableStateOf("Más recientes") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userId?.let {
            appointmentViewModel.fetchCitas(it)
            clinicViewModel.fetchClinics(it)
        }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Historial",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    fontFamily = afacadFont,
                )

                Box {
                    Card(
                        modifier = Modifier
                            .height(28.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 8.dp)
                                .clickable { expanded = true }
                        ) {
                            Text(
                                text = selectedOption,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = afacadFont,
                                color = Color(0xFFB2C2A4)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .width(IntrinsicSize.Min)
                            .padding(horizontal = 8.dp)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Más recientes",
                                    fontFamily = afacadFont,
                                    color = Color(0xFFB2C2A4),
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                selectedOption = "Más recientes"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Más antiguas",
                                    fontFamily = afacadFont,
                                    color = Color(0xFFB2C2A4),
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                selectedOption = "Más antiguas"
                                expanded = false
                            }
                        )
                    }
                }

            }

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
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = afacadFont,
                        color = Color(0xFFB2C2A4),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            HistoryCardList(selectedOption = selectedOption, navController = navController)
        }
    }
}

@Composable
fun HistoryCardList(selectedOption: String, navController: NavHostController? = null)  {
    // ViewModels necesarios
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val doctorViewModel: DoctorViewModel = viewModel()
    val clinicViewModel: ClinicViewModel = viewModel()

    // Observa los datos de citas, doctores y clínicas
    val citas by appointmentViewModel.citas.collectAsState()
    val doctorList by doctorViewModel.doctors.collectAsState()
    val clinicas by clinicViewModel.clinics.collectAsState()

    val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH)
    val now = java.util.Date()

    val citasFiltradas = citas.mapNotNull {
        try {
            val date = sdf.parse(it.fecha_cita)
            if (date.before(now)) it to date else null
        } catch (e: Exception) {
            null
        }
    }

    // Filtra citas pasadas
    val citasPasadas = if (selectedOption == "Más recientes") {
        citasFiltradas.sortedByDescending { it.second }.map { it.first }
    } else {
        citasFiltradas.sortedBy { it.second }.map { it.first }
    }

    // Lista de tarjetas con citas anteriores
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        items(citasPasadas) { cita ->
            // Busca doctor y clínica correspondientes
            val doctor = doctorList.find { it.id_doctor == cita.id_doctor }
            val clinica = doctor?.let { d -> clinicas.find { it.id_clinica == d.id_clinica } }

            // Componente de tarjeta individual para cada cita
            HistoryCard(
                appointment = cita,
                doctor = doctor,
                clinic = clinica,
                navController = navController
            )
        }
    }
}