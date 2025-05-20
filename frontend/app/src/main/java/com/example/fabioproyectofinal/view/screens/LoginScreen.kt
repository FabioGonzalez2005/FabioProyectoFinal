package com.example.fabioproyectofinal.view.screens

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
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()
    val estadoLogin by loginViewModel.loginEstado.collectAsState()
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
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/logo_ozj4ng.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)    // cache en disco
                        .memoryCachePolicy(CachePolicy.ENABLED)  // cache en memoria
                        .build()
                ),
                contentDescription = "Logo de CanariaSS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 16.dp)
                    .height(150.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Text("Iniciar sesión", color = Color(0xFFB2C2A4), fontFamily = afacadFont, fontSize = 40.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Ya está en login */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text("Iniciar sesión", color = Color.White, fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }
                Button(
                    onClick = {
                        navController.navigate(route = AppScreens.RegisterScreen.route) {
                            popUpTo(AppScreens.LoginScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(6.dp),
                )
                {
                    Text("Crea una cuenta", color = Color(0xFFB2C2A4), fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }
            }

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

            Text(
                text = "He olvidado mi contraseña",
                color = Color.Gray,
                fontFamily = afacadFont,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable { /* Acción */ }
            )

            Button(
                onClick = {
                    if (formularioValido) {
                        loginViewModel.login(
                            UsuarioLoginRequest(usuario = username, contraseña = password)
                        )
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
            LaunchedEffect(estadoLogin) {
                estadoLogin?.let { estado ->
                    if (estado.msg != null) {
                        // Login correcto
                        SessionManager.idUsuario = estado.id_usuario
                        SessionManager.nombre = estado.nombre
                        SessionManager.email = estado.email
                        SessionManager.username = estado.usuario

                        navController.navigate(route = AppScreens.MainScreenApp.route.replace("{id_usuario}", estado.id_usuario.toString())) {
                            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                        }
                    } else if (estado.error != null) {
                        // Login fallido
                        Toast.makeText(context, estado.error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
}


