package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.view.components.BottomBarDoctor
import com.example.fabioproyectofinal.view.components.CalendarComponent
import com.example.fabioproyectofinal.view.components.DoctorCitaCard
import com.example.fabioproyectofinal.view.components.DoctorPastCitaCard
import com.example.fabioproyectofinal.view.components.TopBar
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun DoctorPastAppointmentsScreen(
    navController: NavHostController,
    userId: Int?
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont))
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var citasFiltradas by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId, selectedDate) {
        if (userId != null) {
            val fechaStr = selectedDate.toString()
            try {
                val idDoctorResponse = ApiServer.apiService.getIdDoctorPorUsuario(userId)
                val idDoctor = idDoctorResponse.firstOrNull()?.get("id_doctor")
                if (idDoctor != null) {
                    citasFiltradas = ApiServer.apiService.getCitasDelDoctorPorDia(idDoctor, fechaStr)
                        .filter { it.estado == "Confirmado" || it.estado == "Cancelado" }
                } else {
                    println("No se encontró el id_doctor para el usuario $userId")
                }
            } catch (e: Exception) {
                println("Error cargando citas: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController) {} },
        bottomBar = { BottomBarDoctor(navController = navController, userId = userId ?: -1) },
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFF9F2))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Citas pasadas",
                    color = Color(0xFFB2C2A4),
                    fontSize = 40.sp,
                    fontFamily = afacadFont,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CalendarComponent(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    validaFecha = { it.isBefore(LocalDate.now()) },
                    allowFuture = false,
                    allowPast = true,
                    colorProvider = { isSelected, isWorkingDay, isPastDate, isToday ->
                        when {
                            isSelected -> Color(0xFF859A72)
                            !isWorkingDay -> Color(0xFFC47E7E)
                            isPastDate -> Color(0xFFB2C2A4)
                            isToday -> Color(0xFF9EC8D5)
                            else -> Color(0xFFD5D5D5)
                        }
                    }
                )


                Spacer(modifier = Modifier.height(12.dp))

                if (citasFiltradas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No hay citas para este día.",
                            fontFamily = afacadFont,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(citasFiltradas) { cita ->
                            DoctorPastCitaCard(
                                cita = cita,
                                afacadFont = afacadFont,
                                onCitaActualizada = {
                                    val fechaStr = selectedDate.toString()
                                    scope.launch {
                                        try {
                                            val idDoctorResponse = ApiServer.apiService.getIdDoctorPorUsuario(userId ?: -1)
                                            val idDoctor = idDoctorResponse.firstOrNull()?.get("id_doctor")
                                            if (idDoctor != null) {
                                                citasFiltradas = ApiServer.apiService.getCitasDelDoctorPorDia(idDoctor, fechaStr)
                                                    .filter { it.estado == "Confirmado" }
                                                    .sortedBy { it.fecha_cita }
                                            }
                                        } catch (e: Exception) {
                                            println("Error actualizando citas: ${e.message}")
                                        }
                                    }
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}