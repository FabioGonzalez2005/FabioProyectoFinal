package com.example.fabioproyectofinal.view.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MedicalDataDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val api = ApiServer.apiService

    var fecha_nacimiento by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var telefono_emergencia by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var antecedentes_familiares by remember { mutableStateOf("") }
    var condiciones_pasadas by remember { mutableStateOf("") }
    var procedimientos_quirurgicos by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        val idUsuario = SessionManager.idUsuario ?: return@LaunchedEffect
        try {
            val datos = api.obtenerDatosDeInteres(idUsuario).firstOrNull() ?: return@LaunchedEffect

            fecha_nacimiento = datos["fecha_nacimiento"] ?: ""
            telefono = datos["telefono"] ?: ""
            telefono_emergencia = datos["telefono_emergencia"] ?: ""
            alergias = datos["alergias"] ?: ""
            antecedentes_familiares = datos["antecedentes_familiares"] ?: ""
            condiciones_pasadas = datos["condiciones_pasadas"] ?: ""
            procedimientos_quirurgicos = datos["procedimientos_quirurgicos"] ?: ""

            // Opcional: actualiza SessionManager también
            SessionManager.fecha_nacimiento = fecha_nacimiento
            SessionManager.telefono = telefono
            SessionManager.telefono_emergencia = telefono_emergencia
            SessionManager.alergias = alergias
            SessionManager.antecedentes_familiares = antecedentes_familiares
            SessionManager.condiciones_pasadas = condiciones_pasadas
            SessionManager.procedimientos_quirurgicos = procedimientos_quirurgicos

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al cargar los datos del perfil", Toast.LENGTH_SHORT).show()
        }
    }

    fun guardarCambios() {
        val idUsuario = SessionManager.idUsuario ?: return
        val datos = mutableMapOf<String, String>()

        if (fecha_nacimiento.isNotBlank()) datos["fecha_nacimiento"] = fecha_nacimiento
        if (telefono.isNotBlank()) datos["telefono"] = telefono
        if (telefono_emergencia.isNotBlank()) datos["telefono_emergencia"] = telefono_emergencia
        if (alergias.isNotBlank()) datos["alergias"] = alergias
        if (antecedentes_familiares.isNotBlank()) datos["antecedentes_familiares"] = antecedentes_familiares
        if (condiciones_pasadas.isNotBlank()) datos["condiciones_pasadas"] = condiciones_pasadas
        if (procedimientos_quirurgicos.isNotBlank()) datos["procedimientos_quirurgicos"] = procedimientos_quirurgicos

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.actualizarDatosDeInteres(idUsuario, datos)

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Datos de interés actualizados correctamente", Toast.LENGTH_SHORT).show()
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
                "Editar Datos Médicos",
                color = Color(0xFFB2C2A4),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                buildTextField("Fecha de nacimiento", fecha_nacimiento) { fecha_nacimiento = it }
                buildTextField("Teléfono", telefono) { telefono = it }
                buildTextField("Teléfono de emergencia", telefono_emergencia) { telefono_emergencia = it }
                buildTextField("Alergias", alergias) { alergias = it }
                buildTextField("Antecedentes familiares", antecedentes_familiares) { antecedentes_familiares = it }
                buildTextField("Condiciones pasadas", condiciones_pasadas) { condiciones_pasadas = it }
                buildTextField("Procedimientos quirúrgicos", procedimientos_quirurgicos) { procedimientos_quirurgicos = it }
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF7C8B6B)) },
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
