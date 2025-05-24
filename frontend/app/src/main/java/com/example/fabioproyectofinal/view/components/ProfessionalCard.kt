package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R


// Tarjeta vertical que muestra un profesional (nombre + especialidad)
@Composable
fun ProfessionalCard(
    name: String,                      // Nombre del profesional
    specialty: String,                // Especialidad médica
    navController: NavHostController, // Para navegación
    userId: Int?,                     // ID del usuario
    onClick: () -> Unit               // Acción al hacer clic en la tarjeta
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onClick() }, // Navegación o acción personalizada al tocar
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del profesional (actualmente una imagen fija genérica)
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                ),
                contentDescription = name,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del profesional
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = afacadFont,
                color = Color(0xFFB2C2A4)
            )

            // Especialidad médica
            Text(
                text = specialty,
                fontSize = 12.sp,
                fontFamily = afacadFont,
                color = Color.Gray
            )
        }
    }
}



