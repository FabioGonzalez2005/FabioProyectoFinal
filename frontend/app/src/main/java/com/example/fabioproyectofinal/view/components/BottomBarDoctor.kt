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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fabioproyectofinal.model.navigation.AppScreens

@Composable
fun BottomBarDoctor(navController: NavHostController, userId: Int) {
    val currentRoute = navController.currentDestination?.route
    val activeIndex = when {
        currentRoute?.startsWith("favourites_screen") == true -> 0
        currentRoute?.startsWith("main_screen_app") == true -> 1
        currentRoute?.startsWith("appointments_screen") == true -> 2
        else -> 1
    }

    val iconPositions = remember { mutableStateListOf<Float>() }
    val iconWidths = remember { mutableStateListOf<Float>() }

    val indicatorOffsetPx = remember { mutableStateOf(0f) }
    val indicatorWidthPx = remember { mutableStateOf(0f) }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F2))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = with(density) { indicatorOffsetPx.value.toDp() }, y = 4.dp)
                    .width(with(density) { indicatorWidthPx.value.toDp() })
                    .height(48.dp)
                    .background(Color(0xFFB2C2A4), shape = RoundedCornerShape(12.dp))
                    .align(Alignment.TopStart)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
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
                        Triple(null, "Calendario", AppScreens.AppointmentsScreen.route)
                    )

                    items.forEachIndexed { index, (imageUrl, desc, route) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .onGloballyPositioned { coordinates ->
                                    val x = with(density) { coordinates.positionInRoot().x - 16.dp.toPx() }
                                    val width = coordinates.size.width.toFloat()

                                    if (iconPositions.size <= index) {
                                        iconPositions.add(x)
                                        iconWidths.add(width)
                                    } else {
                                        iconPositions[index] = x
                                        iconWidths[index] = width
                                    }

                                    if (index == activeIndex) {
                                        indicatorOffsetPx.value = x
                                        indicatorWidthPx.value = width
                                    }
                                }
                                .clickable {
                                    navController.navigate(route.replace("{id_usuario}", userId.toString()))
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
}