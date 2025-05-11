package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.viewmodel.AvailabilityViewModel
import com.example.fabioproyectofinal.view.components.*

@Composable
fun SelectProfessionalScreen(navController: NavHostController) {
    val dias = (1..31).map { it.toString().padStart(2, '0') }
    val meses = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    val años = (2025..2040).map { it.toString() }

    var selectedDiaIndex by remember { mutableStateOf(13) } // 14
    var selectedMesIndex by remember { mutableStateOf(8) } // Septiembre
    var selectedAñoIndex by remember { mutableStateOf(1) } // 2025

    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var buttonColor by remember { mutableStateOf(Color(0xFFF4F4F4)) }

    val availabilityVM: AvailabilityViewModel = viewModel()
    val disponibilidad by availabilityVM.disponibilidad.collectAsState()

    val selectedDia = dias[selectedDiaIndex]
    val selectedMes = meses[selectedMesIndex]
    val selectedAño = años[selectedAñoIndex]
    val fechaSeleccionada = "$selectedDia $selectedMes $selectedAño"

    val idDoctor = 1
    LaunchedEffect(Unit) {
        availabilityVM.cargarDisponibilidad(idDoctor)
    }

    Scaffold(
        topBar = {
            TopBar("Fabio González Waschkowitz", navController = navController) { }
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    DateSelectorItem(
                        value = selectedDia,
                        onIncrement = { if (selectedDiaIndex < dias.lastIndex) selectedDiaIndex++ },
                        onDecrement = { if (selectedDiaIndex > 0) selectedDiaIndex-- }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    DateSelectorItem(
                        value = selectedMes,
                        onIncrement = { if (selectedMesIndex < meses.lastIndex) selectedMesIndex++ },
                        onDecrement = { if (selectedMesIndex > 0) selectedMesIndex-- }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    DateSelectorItem(
                        value = selectedAño,
                        onIncrement = { if (selectedAñoIndex < años.lastIndex) selectedAñoIndex++ },
                        onDecrement = { if (selectedAñoIndex > 0) selectedAñoIndex-- }
                    )
                }
            }

                Spacer(modifier = Modifier.height(16.dp))

                val franjasDiaSeleccionado = disponibilidad.filter {
                    true // Filtrado personalizado si tienes fechas completas en backend
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in franjasDiaSeleccionado.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (j in 0..1) {
                                val franja = franjasDiaSeleccionado.getOrNull(i + j)
                                if (franja != null) {
                                    val rango = "${franja.hora_inicio} - ${franja.hora_fin}"
                                    TimeSlotButton(
                                        time = rango,
                                        isSelected = selectedSlot == rango,
                                        isAvailable = franja.disponible,
                                        onClick = {
                                            selectedSlot = rango
                                            if (franja.disponible) {
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

                Button(
                    onClick = {
                        if (selectedSlot != null) {
                            buttonColor = Color(0xFF859A72)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(text = "Aceptar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DateSelectorItem(value: String, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Incrementar",
            tint = Color(0xFFB2C2A4),
            modifier = Modifier.clickable { onIncrement() }
        )
        Card(
            modifier = Modifier
                .width(130.dp)
                .height(40.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = value, fontSize = 16.sp, color = Color(0xFFB2C2A4))
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Decrementar",
            tint = Color(0xFFB2C2A4),
            modifier = Modifier.clickable { onDecrement() }
        )
    }
}
