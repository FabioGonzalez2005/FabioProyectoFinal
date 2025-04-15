package com.example.fabioproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fabioproyectofinal.ui.theme.FabioProyectoFInalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FabioProyectoFInalTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar() }
                ) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Datos simulados
data class Clinica(val nombre: String, val direccion: String, val imagen: Int)

val clinicas = listOf(
    Clinica("Hospiten Lanzarote", "Cam. Lomo Gordo, s/n, 35510 Puerto del Carmen, Las Palmas", R.drawable.hospiten),
    Clinica("International Clinic", "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas", R.drawable.international),
    Clinica("Clínicas Dr. Mager - Deutsche Ärzte", "Avda de las Playas, C. Chalana, 37, 35510 Puerto del Carmen", R.drawable.mager)
)

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Buscador", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(clinicas) { clinica ->
                ClinicaCard(clinica)
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB2C2A4), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("ESP ▼", color = Color.White)
        Text("(logo)", color = Color.White)
        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Nombre clínica o dirección:") },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ClinicaCard(clinica: Clinica) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = clinica.imagen),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
            )
            Column {
                Text(text = clinica.nombre, fontWeight = FontWeight.Bold)
                Text(text = clinica.direccion, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        containerColor = Color(0xFFB2C2A4),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favoritos")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Home, contentDescription = "Inicio")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
            }
        }
    }
}
