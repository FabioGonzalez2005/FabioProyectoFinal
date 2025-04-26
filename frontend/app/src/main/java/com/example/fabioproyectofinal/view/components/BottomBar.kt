package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.navigation.AppScreens

// Barra de navegación inferior
@Composable
fun BottomBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F2))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFB2C2A4),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favoritos", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = { navController.navigate(route = AppScreens.MainScreenApp.route)}) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario", modifier = Modifier.size(32.dp), tint = Color.White)
                }
            }
        }
    }
}