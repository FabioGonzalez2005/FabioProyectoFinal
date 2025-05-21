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

@Composable
fun AppointmentCard(
    appointment: Appointment,
    doctor: Doctor?,
    clinic: Clinic?,
    navController: NavHostController? = null,
    userId: Int,
    appointmentViewModel: AppointmentViewModel
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                        fontFamily = afacadFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Estás seguro de que deseas cancelar la cita?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = afacadFont,
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
                                println("Eliminando cita...")

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val response = ApiServer.apiService.eliminarCita(
                                            mapOf(
                                                "id_usuario" to userId,
                                                "id_cita" to appointment.id_cita
                                            )
                                        )
                                        withContext(Dispatchers.Main) {
                                            println("Cita eliminada: ${response.msg}")

                                            // Refrescar lista de citas para ese usuario
                                            appointmentViewModel.fetchCitas(userId)

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

    val statusColor = when (appointment.estado) {
        "Confirmado" -> Color(0xFFB2C2A4)
        "Cancelado" -> Color(0xFFA64646)
        "Pendiente" -> Color(0xFFBD8F45)
        else -> Color.Gray
    }

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
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(clinic?.src)
                                .diskCachePolicy(CachePolicy.ENABLED)    // cache en disco
                                .memoryCachePolicy(CachePolicy.ENABLED)  // cache en memoria
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado:",
                                fontSize = 14.sp,
                                fontFamily = afacadFont,
                                fontWeight = FontWeight.Bold,
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
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Fecha
                        Text(
                            text = formatFecha(appointment.fecha_cita),
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    //Hora
                    Text(
                        text = formatHora(appointment.fecha_cita),
                        fontSize = 18.sp,
                        fontFamily = afacadFont,
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
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp")
                                    .diskCachePolicy(CachePolicy.ENABLED)    // cache en disco
                                    .memoryCachePolicy(CachePolicy.ENABLED)  // cache en memoria
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
