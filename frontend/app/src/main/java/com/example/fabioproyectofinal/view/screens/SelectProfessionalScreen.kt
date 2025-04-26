package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.data.clinics
import com.example.fabioproyectofinal.view.components.ClinicActionButton
import com.example.fabioproyectofinal.view.components.ClinicaCard
import com.example.fabioproyectofinal.view.components.TopBar
import com.example.fabioproyectofinal.view.components.ProfessionalCard

@Composable
fun SelectProfessionalScreen(navController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar("Fabio González Waschkowitz", navController = navController) { /* Acción */ }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                ClinicaCard(clinic = clinics.first())
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClinicActionButton("Dirección", R.drawable.icon_map) { /* Acción */ }
                ClinicActionButton("Llamar", R.drawable.icon_call) { /* Acción */ }
                ClinicActionButton("Instagram", R.drawable.icon_instagram) { /* Acción */ }
            }

            Spacer(modifier = Modifier.size(24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título sección profesionales
                        Text(
                            "Escoge profesional:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFFB2C2A4)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        // Lista de profesionales
                        val professionals = listOf(
                            "Alberto Medina" to "Osteópata",
                            "Jimena Cáceres" to "Dermatología",
                            "Armando Pérez" to "Oncología",
                            "Cristina Morales" to "Rehabilitación",
                        )

                        LazyColumn {
                            items(professionals.chunked(2)) { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    pair.forEach { (name, specialty) ->
                                        ProfessionalCard(name = name, specialty = specialty, navController) { /* Acción */ }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

        }
    }