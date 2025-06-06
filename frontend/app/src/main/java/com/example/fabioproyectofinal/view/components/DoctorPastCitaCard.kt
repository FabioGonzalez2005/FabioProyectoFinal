package com.example.fabioproyectofinal.view.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.utils.formatFechaCompleta
import com.example.fabioproyectofinal.model.utils.formatHora

@Composable
fun DoctorPastCitaCard(
    cita: Appointment,
    afacadFont: FontFamily,
    onCitaActualizada: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    val fecha = formatFechaCompleta(cita.fecha_cita)
    val hora = formatHora(cita.fecha_cita)

    val estadoColor = when (cita.estado) {
        "Confirmado" -> Color(0xFFBD8F45)
        "Cancelado" -> Color(0xFFE57373)
        else -> Color.Gray
    }

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
                    text = "Pasado",
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
                Column {
                    Text(
                        text = fecha,
                        fontSize = 14.sp,
                        fontFamily = afacadFont,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Hora: $hora",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = afacadFont,
                        color = Color(0xFFB2C2A4)
                    )
                }

                Button(
                    onClick = { showEditDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Expediente", fontFamily = afacadFont, color = Color.White)
                }
            }

            if (showEditDialog) {
                EditRecordDialog(cita = cita, onDismiss = { showEditDialog = false })
            }
        }
    }
}
