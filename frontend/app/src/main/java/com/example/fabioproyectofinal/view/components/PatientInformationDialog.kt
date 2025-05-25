package com.example.fabioproyectofinal.view.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Appointment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PatientInformationDialog(
    cita: Appointment,
    onDismiss: () -> Unit,
    onUpdated: () -> Unit = {}
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current
    val api = ApiServer.apiService

    var nombre by remember { mutableStateOf(cita.nombre ?: "") }
    var fechaNacimiento by remember { mutableStateOf(cita.fecha_nacimiento ?: "") }
    var telefono by remember { mutableStateOf(cita.telefono ?: "") }
    var telefonoEmergencia by remember { mutableStateOf(cita.telefono_emergencia ?: "") }

    fun guardarCambios() {
        val datos = mutableMapOf<String, String>()
        if (nombre.isNotBlank()) datos["nombre"] = nombre
        if (fechaNacimiento.isNotBlank()) datos["fecha_nacimiento"] = fechaNacimiento
        if (telefono.isNotBlank()) datos["telefono"] = telefono
        if (telefonoEmergencia.isNotBlank()) datos["telefono_emergencia"] = telefonoEmergencia

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.actualizarDatosCita(cita.id_cita, datos)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Información del paciente actualizada correctamente", Toast.LENGTH_SHORT).show()
                    onUpdated()
                    onDismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al actualizar los datos: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Información del Paciente",
                color = Color(0xFFB2C2A4),
                fontFamily = afacadFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                buildTextField("Nombre completo", nombre) { nombre = it }
                buildTextField("Fecha de nacimiento", fechaNacimiento) { fechaNacimiento = it }
                buildTextField("Teléfono", telefono) { telefono = it }
                buildTextField("Teléfono de emergencia", telefonoEmergencia) { telefonoEmergencia = it }
            }
        },
        confirmButton = {
            AnimatedDialogButton(
                text = "Guardar",
                onClick = { guardarCambios() },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        },
        dismissButton = {
            AnimatedDialogButton(
                text = "Cerrar",
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        },
        containerColor = Color(0xFFFFF9F2),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun buildTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, fontFamily = afacadFont, color = Color(0xFF7C8B6B))
        },
        textStyle = TextStyle(color = Color(0xFF7C8B6B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7C8B6B),
            unfocusedBorderColor = Color(0xFF7C8B6B)
        ),
        singleLine = true
    )
}