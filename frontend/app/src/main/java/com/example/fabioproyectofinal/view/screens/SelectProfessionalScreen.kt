package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.ProfessionalCardHorizontal
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.view.components.TimeSlotButton

@Composable
fun SelectProfessionalScreen(navController: NavHostController) {
    var selectedDate by remember { mutableStateOf("14 marzo 2025") }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var buttonColor by remember { mutableStateOf(Color(0xFFF4F4F4)) }

    val dateOptions = listOf(
        "14 marzo 2025",
        "15 marzo 2025",
        "16 marzo 2025",
        "17 marzo 2025"
    )

    val timeSlots = listOf(
        "8:00 - 9:00" to true,
        "9:00 - 10:00" to false, // no disponible
        "12:00 - 13:00" to true,
        "13:00 - 14:00" to true,
        "14:00 - 15:00" to false, // no disponible
        "15:00 - 16:00" to true
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
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Disponibilidad
                Card(
                    modifier = Modifier
                        .size(width = 180.dp, height = 45.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Disponibilidad:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB2C2A4),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    }

                // Selector de día
                Card(
                    modifier = Modifier
                        .size(width = 180.dp, height = 45.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedDate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB2C2A4),
                                modifier = Modifier.padding(start = 8.dp)

                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Desplegar",
                                tint = Color(0xFFB2C2A4)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color.White)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            dateOptions.forEach { date ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedDate = date
                                        expanded = false
                                    },
                                    text = {
                                        Text(
                                            text = date,
                                            color = Color(0xFFB2C2A4),
                                            fontSize = 16.sp
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
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
                                val (time, isAvailable) = slot
                                TimeSlotButton(
                                    time = time,
                                    isSelected = selectedSlot == time,
                                    isAvailable = isAvailable,
                                    onClick = {
                                        selectedSlot = time
                                        if (isAvailable) {
                                            buttonColor = Color(0xFFB2C2A4)
                                        }
                                    }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(width = 188.dp, height = 42.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de aceptar
            Button(
                onClick = {
                    // Acción al aceptar
                    if (selectedSlot != null) {
                        buttonColor = Color(0xFF859A72)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(text = "Aceptar", color = Color.White)
            }
        }
    }
}