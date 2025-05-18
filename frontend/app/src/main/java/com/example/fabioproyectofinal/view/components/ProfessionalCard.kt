package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.navigation.AppScreens

// Tarjeta de profesional en vertical
@Composable
fun ProfessionalCard(name: String, specialty: String, navController: NavHostController, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .clickable { navController.navigate(route = AppScreens.SelectProfessionalScreen.route) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_user_aueq9d.webp")
                        .diskCachePolicy(CachePolicy.ENABLED)    // cache en disco
                        .memoryCachePolicy(CachePolicy.ENABLED)  // cache en memoria
                        .build()
                ),
                contentDescription = name,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB2C2A4)
            )
            Text(
                text = specialty,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}





