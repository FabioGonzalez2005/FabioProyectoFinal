package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.ProfessionalCardHorizontal
import com.example.fabioproyectofinal.view.components.TopBar

@Composable
fun SelectProfessionalScreen(navController: NavHostController) {
    var selectedDate by remember { mutableStateOf("14 marzo 2025") }
    var selectedSlot by remember { mutableStateOf<String?>(null) }

    val timeSlots = listOf(
        "8:00 - 9:00", "9:00 - 10:00",
        "12:00 - 13:00", "13:00 - 14:00",
        "14:00 - 15:00", "15:00 - 16:00"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfessionalCardHorizontal(
                name = "Alberto Medina",
                specialty = "Osteópata",
                price = "45",
                navController = navController
            ) {
                // Acción al hacer clic en la tarjeta
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Disponibilidad:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )

                Text(
                    text = selectedDate,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in timeSlots.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (j in 0..1) {
                            val slot = timeSlots.getOrNull(i + j)
                            if (slot != null) {
                                TimeSlotButton(
                                    time = slot,
                                    isSelected = selectedSlot == slot,
                                    onClick = { selectedSlot = slot }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Acción al aceptar
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F4))
            ) {
                Text(text = "Aceptar", color = Color.Gray)
            }
        }
    }
}

@Composable
fun TimeSlotButton(time: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 4.dp)
            .background(
                color = if (isSelected) Color(0xFFBFCDB2) else Color(0xFFDDE2D3),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}
