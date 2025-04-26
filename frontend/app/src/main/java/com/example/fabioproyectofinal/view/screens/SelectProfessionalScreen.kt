package com.example.fabioproyectofinal.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.ProfessionalCardHorizontal
import com.example.fabioproyectofinal.view.components.TopBar

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
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                ProfessionalCardHorizontal(name = "Alberto Medina", specialty = "Osteópata", "45", navController) { /* Acción */ }
                }
            }
        }