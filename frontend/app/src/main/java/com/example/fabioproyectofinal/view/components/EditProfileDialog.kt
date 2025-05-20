package com.example.fabioproyectofinal.view.components

import android.util.Log
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

@Composable
fun EditProfileDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(SessionManager.nombre ?: "") }
    var username by remember { mutableStateOf(SessionManager.username ?: "") }
    var email by remember { mutableStateOf(SessionManager.email ?: "") }
    Log.i("Prueba", "$username $email")
    var showMedicalDialog by remember { mutableStateOf(false) }

    fun guardarCambios() {
        val api = ApiServer.apiService
        val idUsuario = SessionManager.idUsuario ?: return

        val datos = mutableMapOf<String, String>()
        if (name.isNotBlank()) datos["nombre"] = name
        if (username.isNotBlank()) datos["usuario"] = username
        if (email.isNotBlank()) datos["email"] = email

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.actualizarPerfil(idUsuario, datos)

                // Actualizar SessionManager local
                SessionManager.nombre = name
                SessionManager.username = username
                SessionManager.email = email

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace() // Esto imprimirá el error en Logcat
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al actualizar perfil: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Editar Perfil",
                color = Color(0xFFB2C2A4),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre", color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario", color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", color = Color(0xFF7C8B6B)) },
                    textStyle = TextStyle(color = Color(0xFF7C8B6B)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    singleLine = true
                )
                AnimatedDialogButton(
                    text = "Datos de interés",
                    onClick = {
                        showMedicalDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        },
        confirmButton = {
            AnimatedDialogButton(
                text = "Guardar",
                onClick = {
                    guardarCambios()
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
    if (showMedicalDialog) {
        MedicalDataDialog(onDismiss = { showMedicalDialog = false })
    }
}