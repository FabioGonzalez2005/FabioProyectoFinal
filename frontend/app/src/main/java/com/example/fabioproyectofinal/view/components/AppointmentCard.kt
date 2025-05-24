package com.example.fabioproyectofinal.view.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.utils.formatFecha
import com.example.fabioproyectofinal.model.utils.formatHora
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.viewmodel.AppointmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.content.Context

// Tarjeta de citas
@Composable
fun AppointmentCard(
    appointment: Appointment,
    doctor: Doctor?,
    clinic: Clinic?,
    navController: NavHostController? = null,
    userId: Int,
    appointmentViewModel: AppointmentViewModel
) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Diálogo de confirmación de cancelación de cita
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título del diálogo
                    Text(
                        text = "Confirmar cancelación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB2C2A4),
                        fontFamily = afacadFont,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Texto descriptivo del diálogo
                    Text(
                        text = "¿Estás seguro de que deseas cancelar la cita?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = afacadFont,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de acción: "Sí" y "No"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnimatedDialogButton(
                            text = "Sí",
                            onClick = {
                                showDialog = false

                                // Lógica para eliminar la cita desde la API
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val response = ApiServer.apiService.eliminarCita(
                                            mapOf(
                                                "id_usuario" to userId,
                                                "id_cita" to appointment.id_cita
                                            )
                                        )
                                        withContext(Dispatchers.Main) {
                                            // Refresca citas y muestra notificación
                                            appointmentViewModel.fetchCitas(userId)

                                            val fechaTexto = formatFecha(appointment.fecha_cita)
                                            val horaTexto = formatHora(appointment.fecha_cita)
                                            val nombreDoctor = doctor?.nombre ?: "Profesional"
                                            val nombreClinica = clinic?.nombre ?: "Clínica"

                                            // Construcción y envío de la notificación
                                            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                            val notification = NotificationCompat.Builder(context, "appointment_channel")
                                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                .setContentTitle("Cita cancelada")
                                                .setStyle(
                                                    NotificationCompat.BigTextStyle().bigText(
                                                        "Has cancelado tu cita con $nombreDoctor en $nombreClinica a las $horaTexto el $fechaTexto"
                                                    )
                                                )
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .build()

                                            notificationManager.notify(System.currentTimeMillis().toInt(), notification)

                                            Toast.makeText(context, "Cita cancelada con éxito", Toast.LENGTH_SHORT).show()
                                        }

                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            println("Error eliminando cita: ${e.message}")
                                        }
                                    }
                                }
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

    // Define el color del estado de la cita
    val statusColor = when (appointment.estado) {
        "Confirmado" -> Color(0xFFB2C2A4)
        "Cancelado" -> Color(0xFFA64646)
        "Pendiente" -> Color(0xFFBD8F45)
        else -> Color.Gray
    }

    // Tarjeta principal de la cita
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
            Column(modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )) {
                // Imagen de la clínica y datos básicos
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(clinic?.src)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()
                        ),
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
                            fontFamily = afacadFont,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = clinic?.direccion ?: "Dirección no disponible",
                            fontSize = 14.sp,
                            fontFamily = afacadFont,
                            color = Color.Gray
                        )

                        // Estado de la cita
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                fontFamily = afacadFont,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = appointment.estado,
                                fontSize = 14.sp,
                                fontFamily = afacadFont,
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 2.dp, color = Color(0xFFCAD2C5))
                Spacer(modifier = Modifier.height(8.dp))

                // Fecha y hora de la cita
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Cita:",
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatFecha(appointment.fecha_cita),
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = formatHora(appointment.fecha_cita),
                        fontSize = 18.sp,
                        fontFamily = afacadFont,
                        color = Color(0xFFB2C2A4),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Información del profesional
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp")
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .build()
                            ),
                            contentDescription = doctor?.nombre,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = doctor?.nombre ?: "Profesional no disponible",
                                fontSize = 14.sp,
                                fontFamily = afacadFont,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB2C2A4)
                            )
                            Text(
                                text = doctor?.especialidad ?: "Especialidad desconocida",
                                fontSize = 12.sp,
                                fontFamily = afacadFont,
                                color = Color.Gray
                            )
                        }
                    }

                    // Botón para cancelar cita o ver motivos
                    if (appointment.estado == "Cancelado") {
                        Button(
                            onClick = { println("Ver motivos de la cancelación") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ver motivos", fontFamily = afacadFont, color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancelar", fontFamily = afacadFont, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}