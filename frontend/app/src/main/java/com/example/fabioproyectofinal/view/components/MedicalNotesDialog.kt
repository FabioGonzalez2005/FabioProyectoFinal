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
fun MedicalNotesDialog(
    cita: Appointment,
    onDismiss: () -> Unit,
    onUpdated: () -> Unit = {}
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current
    val api = ApiServer.apiService

    var medicamento by remember { mutableStateOf(cita.medicamento_y_dosis ?: "") }
    var nota by remember { mutableStateOf(cita.nota ?: "") }

    fun guardarCambios() {
        val datos = mutableMapOf<String, String>()
        if (medicamento.isNotBlank()) datos["medicamento_y_dosis"] = medicamento
        if (nota.isNotBlank()) datos["nota"] = nota

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.actualizarDatosCita(cita.id_cita, datos)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Notas médicas actualizadas correctamente", Toast.LENGTH_SHORT).show()
                    onUpdated()
                    onDismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al actualizar las notas: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Notas Médicas",
                color = Color(0xFFB2C2A4),
                fontFamily = afacadFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                buildTextField("Medicamento y dosis", medicamento) { medicamento = it }
                buildTextField("Nota", nota) { nota = it }
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
        singleLine = false
    )
}