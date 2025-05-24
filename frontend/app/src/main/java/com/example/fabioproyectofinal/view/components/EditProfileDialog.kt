package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.fabioproyectofinal.model.ApiServer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun EditProfileDialog(onDismiss: () -> Unit) {
    // Fuente personalizada para los textos dentro del diálogo
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current

    // Estados para almacenar los campos del perfil, inicializados con datos actuales del usuario
    var name by remember { mutableStateOf(SessionManager.nombre ?: "") }
    var username by remember { mutableStateOf(SessionManager.username ?: "") }
    var email by remember { mutableStateOf(SessionManager.email ?: "") }

    // Variables para mostrar diálogos adicionales
    var showMedicalDialog by remember { mutableStateOf(false) }
    var showInsuranceDialog by remember { mutableStateOf(false) }

    // Función para guardar los cambios en el perfil mediante llamada API
    fun guardarCambios() {
        val api = ApiServer.apiService
        val idUsuario = SessionManager.idUsuario ?: return

        val datos = mutableMapOf<String, String>()
        if (name.isNotBlank()) datos["nombre"] = name
        if (username.isNotBlank()) datos["usuario"] = username
        if (email.isNotBlank()) datos["email"] = email

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llama al servicio API para actualizar datos
                api.actualizarPerfil(idUsuario, datos)

                // Actualiza datos localmente en SessionManager
                SessionManager.nombre = name
                SessionManager.username = username
                SessionManager.email = email

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace() // Imprime error en consola para debug
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al actualizar perfil: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Diálogo principal para editar el perfil
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Perfil",
                color = Color(0xFFB2C2A4),
                fontFamily = afacadFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                // Campo para editar el nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )

                // Campo para editar el usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )

                // Campo para editar el correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )

                // Botón para abrir diálogo de datos médicos
                AnimatedDialogButton(
                    text = "Datos de interés",
                    onClick = {
                        showMedicalDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                // Botón para abrir diálogo de seguros
                AnimatedDialogButton(
                    text = "Seguros",
                    onClick = {
                        showInsuranceDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        },
        confirmButton = {
            // Botón para guardar cambios
            AnimatedDialogButton(
                text = "Guardar",
                onClick = {
                    guardarCambios()
                },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        },
        dismissButton = {
            // Botón para cerrar el diálogo sin guardar
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

    // Mostrar diálogo para datos médicos si se activa
    if (showMedicalDialog) {
        MedicalDataDialog(onDismiss = { showMedicalDialog = false })
    }

    // Mostrar diálogo para seguros si se activa
    if (showInsuranceDialog) {
        InsuranceDialog(onDismiss = { showInsuranceDialog = false })
    }
}