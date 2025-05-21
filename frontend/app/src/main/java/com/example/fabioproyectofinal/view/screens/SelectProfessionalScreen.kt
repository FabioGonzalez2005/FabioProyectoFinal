package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.CalendarComponent
import com.example.fabioproyectofinal.viewmodel.AvailabilityViewModel
import com.example.fabioproyectofinal.view.components.*
import java.time.LocalDate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R

@Composable
fun SelectProfessionalScreen(navController: NavHostController, userId: Int?) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var buttonColor by remember { mutableStateOf(Color(0xFFF4F4F4)) }

    val availabilityVM: AvailabilityViewModel = viewModel()

    val idDoctor = 1
    LaunchedEffect(Unit) {
        availabilityVM.cargarDisponibilidad(idDoctor)
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController) { }
        },
        bottomBar = {
            BottomBar(navController = navController, userId = userId ?: -1)
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

                Spacer(modifier = Modifier.height(8.dp))

                CalendarComponent(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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
                    Text(text = "Buscar cita", fontFamily = afacadFont, color = Color.White)
                }
            }
        }
    }
