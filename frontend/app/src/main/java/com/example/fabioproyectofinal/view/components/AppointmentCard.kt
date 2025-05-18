package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.utils.formatFecha
import com.example.fabioproyectofinal.model.utils.formatHora

@Composable
fun AppointmentCard(
    appointment: Appointment,
    doctor: Doctor?,
    clinic: Clinic?,
    navController: NavHostController? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {},
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Confirmar cancelación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB2C2A4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Estás seguro de que deseas cancelar la cita?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnimatedDialogButton(
                            text = "Sí",
                            onClick = {
                                showDialog = false
                                println("Cita cancelada")
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AnimatedDialogButton(
                            text = "No",
                            onClick = {
                                showDialog = false
                            }
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            containerColor = Color.White
        )
    }

    val statusColor = when (appointment.estado) {
        "Confirmado" -> Color(0xFFB2C2A4)
        "Cancelado" -> Color(0xFFA64646)
        "Pendiente" -> Color(0xFFBD8F45)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(clinic?.src),
                        contentDescription = clinic?.nombre,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(end = 16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = clinic?.nombre ?: "Clínica no disponible",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = clinic?.direccion ?: "Dirección no disponible",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = appointment.estado,
                                fontSize = 14.sp,
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 2.dp, color = Color(0xFFCAD2C5))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Cita:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Fecha
                        Text(
                            text = formatFecha(appointment.fecha_cita),
                            fontSize = 18.sp,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    //Hora
                    Text(
                        text = formatHora(appointment.fecha_cita),
                        fontSize = 18.sp,
                        color = Color(0xFFB2C2A4),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp"),
                            contentDescription = doctor?.nombre,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = doctor?.nombre ?: "Profesional no disponible",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB2C2A4)
                            )
                            Text(
                                text = doctor?.especialidad ?: "Especialidad desconocida",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    if (appointment.estado == "Cancelado") {
                        Button(
                            onClick = { println("Ver motivos de la cancelación") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ver motivos", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
