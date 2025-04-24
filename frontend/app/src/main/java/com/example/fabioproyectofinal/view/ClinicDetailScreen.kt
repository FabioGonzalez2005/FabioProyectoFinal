package com.example.fabioproyectofinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.data.Clinic
import com.example.fabioproyectofinal.model.data.clinics
import com.example.fabioproyectofinal.model.navigation.AppScreens

@Composable
fun CustomTopBar(nombre: String, navController: NavHostController, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botón de retroceso
        IconButton(onClick = { navController.popBackStack() }) {
            Image(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp)
            )
        }

        // Nombre del usuario
        Text(
            text = nombre,
            style = TextStyle(
                color = Color(0xFFACBCA0),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )

        // Icono de usuario
        Image(
            painter = painterResource(id = R.drawable.icon_user),
            contentDescription = "Usuario",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ClinicDetailScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopBar("Fabio González Waschkowitz", navController = navController) {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Padding adicional si quieres
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            ClinicaItemCard(clinic = clinics.first())

            Spacer(modifier = Modifier.size(16.dp))

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

            // Título sección profesionales
            Text("Escoge profesional:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

            Spacer(modifier = Modifier.size(8.dp))

            // Lista de profesionales
            val professionals = listOf(
                "Alberto Medina" to "Osteópata",
                "Jimena Cáceres" to "Dermatología",
                "Armando Pérez" to "Oncología",
                "Cristina Morales" to "Rehabilitación",
            )

            LazyColumn {
                items(professionals.chunked(2)) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for ((name, specialty) in row) {
                            ProfessionalCard(name, specialty)
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
fun ClinicActionButton(text: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(48.dp)
        )
        Text(text, fontSize = 14.sp)
    }
}

// Tarjeta de clínica
@Composable
fun ClinicaItemCard(clinic: Clinic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = clinic.src),
                contentDescription = clinic.name,
                modifier = Modifier
                    .size(110.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(clinic.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(clinic.address, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProfessionalCard(name: String, specialty: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.icon_user),
            contentDescription = name,
            modifier = Modifier.size(60.dp)
        )
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(specialty, fontSize = 12.sp, color = Color.Gray)
    }
}
