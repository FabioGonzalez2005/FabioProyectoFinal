package com.example.fabioproyectofinal.model.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.BottomBar
import com.example.fabioproyectofinal.view.components.TopBar
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@Composable
fun CalendarScreen(navController: NavHostController, userId: Int?) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val currentMonth = remember { YearMonth.now() }
    val daysInMonth = remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
    }

    val weekDays = listOf("L", "M", "X", "J", "V", "S", "D")

    Scaffold(
        containerColor = Color(0xFFFFF9F2),
        topBar = {
            TopBar(navController = navController) { /* Acción */ }
        },
        bottomBar = {
            BottomBar(navController = navController, userId = userId ?: -1)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Encabezado días de la semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        color = Color(0xFF7C8B6B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Días del mes
            val daysGrouped = daysInMonth.chunked(7)
            daysGrouped.forEach { week ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        val isSelected = day == selectedDate
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFFB2C2A4) else Color.Transparent)
                                .clickable { selectedDate = day }
                        ) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                color = if (isSelected) Color.White else Color(0xFF7C8B6B),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fecha seleccionada: ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                color = Color(0xFF7C8B6B),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
