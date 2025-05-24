package com.example.fabioproyectofinal.view.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.navigation.AppScreens
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.viewmodel.LoginViewModel
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import com.example.fabioproyectofinal.model.session.SessionManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LoginScreen(navController: NavHostController) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // Estados del formulario
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Contexto para mostrar toasts
    val context = LocalContext.current

    // ViewModel de login
    val loginViewModel: LoginViewModel = viewModel()

    // Estado del intento de login (éxito o error)
    val estadoLogin by loginViewModel.loginEstado.collectAsState()

    // Valida si ambos campos están completos
    val formularioValido = username.isNotBlank() && password.isNotBlank()

    Scaffold(
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Logo superior
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/logo_ozj4ng.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                ),
                contentDescription = "Logo de CanariaSS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 16.dp)
                    .height(150.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            // Título principal
            Text("Iniciar sesión", color = Color(0xFFB2C2A4), fontFamily = afacadFont, fontSize = 40.sp)

            // Botones de selección: login y registro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón "Iniciar sesión" (ya activo)
                Button(
                    onClick = { /* Ya está activo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text("Iniciar sesión", color = Color.White, fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }

                // Botón "Crea una cuenta" redirige al registro
                Button(
                    onClick = {
                        navController.navigate(route = AppScreens.RegisterScreen.route) {
                            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text("Crea una cuenta", color = Color(0xFFB2C2A4), fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }
            }

            // Campo de texto para nombre de usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Introduce tu usuario", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C8B6B),
                    unfocusedBorderColor = Color(0xFF7C8B6B)
                )
            )

            // Campo de texto para contraseña (oculta)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Introduce tu contraseña", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C8B6B),
                    unfocusedBorderColor = Color(0xFF7C8B6B)
                )
            )

            // Enlace para "He olvidado mi contraseña"
            Text(
                text = "He olvidado mi contraseña",
                color = Color.Gray,
                fontFamily = afacadFont,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable { /* Acción futura */ }
            )

            // Botón para enviar el formulario de login
            Button(
                onClick = {
                    if (formularioValido) {
                        loginViewModel.login(UsuarioLoginRequest(usuario = username, contraseña = password))
                    } else {
                        val errorMsg = when {
                            username.isBlank() -> "Por favor, escribe tu nombre de usuario"
                            password.isBlank() -> "Por favor, escribe tu contraseña"
                            else -> "Completa todos los campos correctamente"
                        }
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (formularioValido) Color(0xFFB2C2A4) else Color.Gray
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 36.dp)
                    .width(160.dp)
            ) {
                Text("Continuar", fontFamily = afacadFont, color = Color.White)
            }

            // Reacción al estado del login
            LaunchedEffect(estadoLogin) {
                estadoLogin?.let { estado ->
                    if (estado.msg != null) {
                        // Login exitoso: guardar sesión y navegar
                        SessionManager.idUsuario = estado.id_usuario
                        SessionManager.nombre = estado.nombre
                        SessionManager.email = estado.email
                        SessionManager.username = estado.usuario

                        SessionManager.rol = estado.rol
                        Log.e("LoginRol", "${estado.rol}")
                        val destino = when (estado.rol) {
                            "Usuario" -> AppScreens.MainScreenApp.route
                            "Medico" -> AppScreens.AppointmentsScreen.route
                            else -> AppScreens.MainScreenApp.route
                        }

                        navController.navigate(
                            route = destino.replace("{id_usuario}", estado.id_usuario.toString())
                        ) {
                            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                        }


                        Toast.makeText(context, "✅ Login exitoso", Toast.LENGTH_SHORT).show()
                    } else if (estado.error != null) {
                        // Error de login
                        Toast.makeText(context, estado.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}