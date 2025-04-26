package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.R


// Barra de navegación superior
@Composable
fun TopBar(nombre: String, navController: NavHostController, onClick: () -> Unit) {
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
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp)
            )
        }

        // Nombre del usuario
        Text(
            text = nombre,
            style = TextStyle(
                color = Color(0xFFACBCA0),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )

        // Icono de usuario
        Image(
            painter = painterResource(id = R.drawable.icon_user),
            contentDescription = "Usuario",
            modifier = Modifier
                .size(24.dp)
                .clickable { onClick() }
        )

    }
}
