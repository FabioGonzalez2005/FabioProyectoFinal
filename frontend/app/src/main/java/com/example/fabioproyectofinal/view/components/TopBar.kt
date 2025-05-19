package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.example.fabioproyectofinal.model.session.SessionManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



// Barra de navegación superior
@Composable
fun TopBar(navController: NavHostController, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botón de retroceso
        IconButton(onClick = { navController.popBackStack() }) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_back_rn6lna.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)    // cache en disco
                        .memoryCachePolicy(CachePolicy.ENABLED)  // cache en memoria
                        .build()
                ),
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp)
            )
        }

        // Nombre + icono
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = SessionManager.nombre ?: "Usuario",
                style = TextStyle(
                    color = Color(0xFFACBCA0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            UserMenuIcon(navController)
        }
    }
}

@Composable
fun UserMenuIcon(navController: NavHostController) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFFFFFFF))
        ) {
            DropdownMenuItem(
                text = { Text("Editar perfil", color = Color(0xFFB2C2A4)) },
                onClick = {
                    expanded = false
                    navController.navigate("editar_perfil")
                }
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión", color = Color(0xFFB2C2A4)) },
                onClick = {
                    expanded = false
                    navController.navigate(AppScreens.LoginScreen.route)
                }
            )
        }
    }
}
