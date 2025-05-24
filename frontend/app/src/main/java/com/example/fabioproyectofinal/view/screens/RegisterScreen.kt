package com.example.fabioproyectofinal.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.fabioproyectofinal.viewmodel.RegisterViewModel
import com.example.fabioproyectofinal.model.data.model.UsuarioRegistroRequest
import com.example.fabioproyectofinal.model.utils.VerificationField
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RegisterScreen(navController: NavHostController) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // Estados del formulario
    var fullname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    // ViewModel para registro
    val authViewModel: RegisterViewModel = viewModel()
    val estadoRegistro by authViewModel.registroEstado.collectAsState()

    // Validación del formulario
    val formularioValido = fullname.isNotBlank() &&
            VerificationField.isEmailValid(email) &&
            confirmPassword == password &&
            VerificationField.isUsernameValid(username) &&
            VerificationField.isPasswordValid(password)

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
            // Logo
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

            // Título
            Text("Crea una cuenta", fontFamily = afacadFont, color = Color(0xFFB2C2A4), fontSize = 40.sp)

            // Botones de navegación entre login y registro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón para volver a iniciar sesión
                Button(
                    onClick = {
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text("Iniciar sesión", color = Color(0xFFB2C2A4), fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }

                // Botón activo de registro
                Button(
                    onClick = { /* Ya estás aquí */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text("Crea una cuenta", color = Color.White, fontFamily = afacadFont, modifier = Modifier.padding(vertical = 9.dp))
                }
            }

            // Campos del formulario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Introduce tu usuario", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7C8B6B), unfocusedBorderColor = Color(0xFF7C8B6B))
            )

            OutlinedTextField(
                value = fullname,
                onValueChange = { fullname = it },
                label = { Text("Introduce tu nombre completo", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7C8B6B), unfocusedBorderColor = Color(0xFF7C8B6B))
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Introduce tu correo electrónico", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7C8B6B), unfocusedBorderColor = Color(0xFF7C8B6B))
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Introduce tu contraseña", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7C8B6B), unfocusedBorderColor = Color(0xFF7C8B6B))
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirma tu contraseña", fontFamily = afacadFont, color = Color(0xFF7C8B6B)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7C8B6B), unfocusedBorderColor = Color(0xFF7C8B6B))
            )

            // Botón de confirmación
            Button(
                onClick = {
                    if (formularioValido) {
                        val nuevoUsuario = UsuarioRegistroRequest(
                            nombre = fullname,
                            email = email,
                            usuario = username,
                            contraseña = password
                        )
                        authViewModel.registrarUsuario(nuevoUsuario)

                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }

                        Toast.makeText(context, "✅ Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        // Mostrar error según el caso
                        val errorMsg = when {
                            username.isBlank() -> "Por favor, escribe tu nombre de usuario"
                            fullname.isBlank() -> "Por favor, escribe tu nombre completo"
                            email.isBlank() -> "Por favor, escribe un correo"
                            password.isBlank() -> "Por favor, escribe una contraseña"
                            confirmPassword.isBlank() -> "Por favor, confirma la contraseña"
                            !VerificationField.isEmailValid(email) -> "Correo electrónico no válido."
                            password != confirmPassword -> "Las contraseñas no coinciden"
                            !VerificationField.isUsernameValid(username) -> "Usuario inválido. Debe tener entre 6 y 20 caracteres, sin espacios"
                            !VerificationField.isPasswordValid(password) -> "Contraseña inválida. Debe tener al menos 4 letras, un número y solo caracteres válidos"
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
        }
    }
}