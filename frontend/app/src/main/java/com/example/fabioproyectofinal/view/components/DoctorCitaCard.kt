package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.utils.formatFechaCompleta
import kotlinx.coroutines.launch

@Composable
fun DoctorCitaCard(cita: Appointment, afacadFont: FontFamily) {
    var showDialog by remember { mutableStateOf(false) }
    var motivo by remember { mutableStateOf("") }

    val fecha = formatFechaCompleta(cita.fecha_cita)

    val estadoColor = when (cita.estado) {
        "Confirmado" -> Color(0xFFB2C2A4)
        "Cancelado" -> Color(0xFFE57373)
        else -> Color.Gray
    }

    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Paciente: ${cita.nombre_paciente ?: "Desconocido"}",
                    fontSize = 16.sp,
                    fontFamily = afacadFont,
                    color = Color.Black
                )

                Text(
                    text = cita.estado,
                    fontSize = 14.sp,
                    fontFamily = afacadFont,
                    color = estadoColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fecha,
                    fontSize = 14.sp,
                    fontFamily = afacadFont,
                    color = Color.DarkGray
                )

                if (cita.estado == "Confirmado") {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC47E7E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar", fontFamily = afacadFont, color = Color.White)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        scope.launch {
                            try {
                                ApiServer.apiService.cancelarCitaConMotivo(
                                    idCita = cita.id_cita,
                                    data = mapOf(
                                        "estado" to "Cancelado",
                                        "motivo_cancelacion" to motivo
                                    )
                                )
                                println("✅ Cita cancelada con motivo: $motivo")
                            } catch (e: Exception) {
                                println("❌ Error cancelando cita: ${e.message}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC47E7E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirmar cancelación", fontFamily = afacadFont, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar", fontFamily = afacadFont)
                }
            },
            title = {
                Text("¿Cancelar cita?", fontFamily = afacadFont, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text("Indica el motivo de la cancelación:", fontFamily = afacadFont)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        placeholder = { Text("Motivo", fontFamily = afacadFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }
}