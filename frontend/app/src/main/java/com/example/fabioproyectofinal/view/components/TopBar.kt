package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
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
import com.example.fabioproyectofinal.model.session.SessionManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R


// Barra de navegación superior personalizada
@Composable
fun TopBar(navController: NavHostController, onClick: () -> Unit) {
    // Fuente personalizada
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botón de retroceso que navega al destino anterior
        IconButton(onClick = { navController.popBackStack() }) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_back_rn6lna.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)    // habilita caché en disco
                        .memoryCachePolicy(CachePolicy.ENABLED)  // habilita caché en memoria
                        .build()
                ),
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp)
            )
        }

        // Nombre del usuario y menú de opciones (editar perfil, cerrar sesión)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = SessionManager.nombre ?: "Usuario",
                style = TextStyle(
                    color = Color(0xFFACBCA0),
                    fontSize = 14.sp,
                    fontFamily = afacadFont,
                    fontWeight = FontWeight.Medium
                )
            )
            UserMenuIcon(navController)
        }
    }
}