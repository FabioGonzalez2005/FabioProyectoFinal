package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.navigation.AppScreens
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

// Tarjeta de clínica
@Composable
fun ClinicaCard(
    clinic: Clinic,
    navController: NavHostController? = null,
    userId: Int?,
    inFavourites: Boolean
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController?.navigate(route = AppScreens.ClinicDetailScreen.route.replace("{id_usuario}", userId.toString()))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(clinic.src),
                contentDescription = clinic.nombre,
                modifier = Modifier
                    .size(110.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(clinic.nombre, fontFamily = afacadFont, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(clinic.direccion, fontFamily = afacadFont, fontSize = 14.sp, color = Color.Gray)

                // Mostrar icono solo si está en favoritos
                if (inFavourites) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter("https://res.cloudinary.com/dr8es2ate/image/upload/icon_favourite_lxpak3.webp"),
                        contentDescription = "Favorito",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}