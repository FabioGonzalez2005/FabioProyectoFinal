package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fabioproyectofinal.model.session.SessionManager

@Composable
fun MedicalDataDialog(onDismiss: () -> Unit) {
    var fechaNacimiento by remember { mutableStateOf(SessionManager.fecha_nacimiento ?: "") }
    var telefono by remember { mutableStateOf(SessionManager.telefono ?: "") }
    var contactoEmergencia by remember { mutableStateOf(SessionManager.contacto_emergencia ?: "") }
    var alergias by remember { mutableStateOf(SessionManager.alergias ?: "") }
    var antecedentesFamiliares by remember { mutableStateOf(SessionManager.antecedentes_familiares ?: "") }
    var condicionesPasadas by remember { mutableStateOf(SessionManager.condiciones_pasadas ?: "") }
    var procedimientosQuirurgicos by remember { mutableStateOf(SessionManager.procedimientos_quirurgicos ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Datos de interés",
                color = Color(0xFFB2C2A4),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                @Composable
                fun input(label: String, value: String, onValueChange: (String) -> Unit) {
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

                input("Fecha de nacimiento", fechaNacimiento) { fechaNacimiento = it }
                input("Teléfono", telefono) { telefono = it }
                input("Contacto de emergencia", contactoEmergencia) { contactoEmergencia = it }
                input("Alergias", alergias) { alergias = it }
                input("Antecedentes familiares", antecedentesFamiliares) { antecedentesFamiliares = it }
                input("Condiciones pasadas", condicionesPasadas) { condicionesPasadas = it }
                input("Procedimientos quirúrgicos", procedimientosQuirurgicos) { procedimientosQuirurgicos = it }
            }
        },
        confirmButton = {
            AnimatedDialogButton(
                text = "Guardar",
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.padding(horizontal = 0.dp)
            )

        },
        dismissButton = {
            AnimatedDialogButton(
                text = "Cerrar",
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        },
        containerColor = Color(0xFFFFF9F2),
        shape = RoundedCornerShape(12.dp)
    )
}
