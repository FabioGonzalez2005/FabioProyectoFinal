package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fabioproyectofinal.model.navigation.AppScreens

@Composable
fun BottomBarDoctor(navController: NavHostController, userId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F2))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    Triple(null, "Inicio", AppScreens.DoctorAppointmentsScreen.route),
                    Triple(null, "Calendario", AppScreens.DoctorPastAppointmentsScreen.route)
                )

                items.forEach { (imageUrl, desc, route) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                val resolvedRoute = route.replace("{id_usuario}", userId.toString())
                                if (navController.currentDestination?.route != resolvedRoute) {
                                    navController.navigate(resolvedRoute)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUrl != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = desc,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            val icon = when (desc) {
                                "Inicio" -> Icons.Default.Home
                                "Calendario" -> Icons.Default.DateRange
                                else -> Icons.Default.Home
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = desc,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}