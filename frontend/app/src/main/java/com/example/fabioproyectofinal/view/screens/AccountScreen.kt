package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun AccountScreen(navController: NavHostController) {
    var isCreatingAccount by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F2))
    ) {
        // Navegación superior
        TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .height(64.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Cuenta",
                fontSize = 32.sp,
                color = Color(0xFF7C8B6B),
            )

            // Campos comunes y según la pantalla
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Introduce tu usuario", color = Color(0xFF7C8B6B)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C8B6B),
                    unfocusedBorderColor = Color(0xFF7C8B6B)
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            if (isCreatingAccount) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Introduce tu correo electrónico", color = Color(0xFF7C8B6B)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Introduce tu contraseña", color = Color(0xFF7C8B6B)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C8B6B),
                    unfocusedBorderColor = Color(0xFF7C8B6B)
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            if (isCreatingAccount) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirma tu contraseña", color = Color(0xFF7C8B6B)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C8B6B),
                        unfocusedBorderColor = Color(0xFF7C8B6B)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
            } else {
                Text(
                    text = "He olvidado mi contraseña",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .clickable {}
                )
            }

            Button(
                onClick = { /* Acción al continuar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp)
            ) {
                Text(text = "Continuar", color = Color.White)
            }

            // Botones para cambiar entre pantallas
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Button(
                    onClick = { isCreatingAccount = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isCreatingAccount) Color(0xFFB2C2A4) else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = if (!isCreatingAccount) Color.White else Color(0xFFB2C2A4)
                    )
                }

                Button(
                    onClick = { isCreatingAccount = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCreatingAccount) Color(0xFFB2C2A4) else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "Crea una cuenta",
                        color = if (isCreatingAccount) Color.White else Color(0xFFB2C2A4)
                    )
                }
            }
        }
    }
}
