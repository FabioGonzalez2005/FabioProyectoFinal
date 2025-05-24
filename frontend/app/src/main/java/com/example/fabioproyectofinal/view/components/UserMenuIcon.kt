package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.example.fabioproyectofinal.viewmodel.LoginViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UserMenuIcon(navController: NavHostController) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current

    // Estados locales para mostrar menú y diálogo
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // ViewModel para manejar el logout
    val loginViewModel: LoginViewModel = viewModel()

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        // Ícono de usuario con imagen remota y caché habilitada
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp")
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build()
            ),
            contentDescription = "Usuario",
            modifier = Modifier
                .size(36.dp)
                .padding(start = 8.dp)
                .clickable { expanded = true }
        )

        // Menú desplegable con opciones de usuario
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFFFFFFF))
        ) {
            // Opción: Editar perfil
            DropdownMenuItem(
                text = { Text("Editar perfil", fontFamily = afacadFont, color = Color(0xFFB2C2A4)) },
                onClick = {
                    expanded = false
                    showDialog = true
                }
            )
            // Opción: Cerrar sesión
            DropdownMenuItem(
                text = { Text("Cerrar sesión", fontFamily = afacadFont, color = Color(0xFFB2C2A4)) },
                onClick = {
                    expanded = false
                    loginViewModel.logout()
                    // Navega a la pantalla de login y limpia el back stack
                    navController.navigate(AppScreens.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }

    // Diálogo para editar perfil
    if (showDialog) {
        EditProfileDialog(onDismiss = { showDialog = false })
    }
}