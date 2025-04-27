package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.Appointment
import com.example.fabioproyectofinal.model.data.appointments
import com.example.fabioproyectofinal.model.data.AppointmentStatus
import com.example.fabioproyectofinal.model.navigation.AppScreens

@Composable
fun AppointmentCard(appointment: Appointment, navController: NavHostController? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                navController?.navigate(route = AppScreens.ClinicDetailScreen.route)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = appointment.src),
                contentDescription = appointment.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = appointment.address, fontSize = 14.sp, color = Color.Gray)
                Text(text = "Fecha: ${appointment.date}", fontSize = 14.sp)
                Text(text = "Hora: ${appointment.time}", fontSize = 14.sp)

                val statusText = when (appointment.status) {
                    AppointmentStatus.CONFIRMED -> "Confirmada"
                    AppointmentStatus.REJECTED -> "Rechazada"
                }

                val statusColor = when (appointment.status) {
                    AppointmentStatus.CONFIRMED -> Color(0xFF4CAF50) // Verde
                    AppointmentStatus.REJECTED -> Color(0xFFF44336) // Rojo
                }

                Text(
                    text = "Estado: $statusText",
                    fontSize = 14.sp,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AppointmentList(navController: NavHostController? = null) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(appointments) { appointment ->
            AppointmentCard(appointment = appointment, navController = navController)
        }
    }
}

