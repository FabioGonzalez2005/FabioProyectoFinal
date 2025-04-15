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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
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
    var searchText by remember { mutableStateOf("") }

    val clinicasFiltradas = clinicas.filter {
        it.nombre.contains(searchText, ignoreCase = true) ||
                it.direccion.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F2))
            .height(64.dp)
    ) {
        // Navegación superior
        TopBar()
        // "Buscador"
        Text(
            text = "Buscador",
            color = Color(0xFFB2C2A4),
            fontSize = 40.sp,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
        )

        // Barra de búsqueda
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Buscar",
                    modifier = Modifier.size(16.dp) // ícono más pequeño
                )
            },
            placeholder = {
                Text(
                    "Buscar clínicas",
                    fontSize = 12.sp // texto más pequeño
                )
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 12.sp), // tamaño del texto
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .height(42.dp), // altura total
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )


        // Lista de tarjetas de clínicas disponibles
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(clinicasFiltradas) { clinica ->
                ClinicaItem(clinica)
            }
        }

    }
}
@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFB2C2A4),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ESP ▼", color = Color.White)
                Text("(logo)", color = Color.White)
                Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
            }
        }
    }
}

@Composable
fun ClinicaItem(clinica: Clinica) {
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
                painter = painterResource(id = clinica.imagen),
                contentDescription = clinica.nombre,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(clinica.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(clinica.direccion, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

// Barra de navegación inferior
@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F2))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(1f)
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
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favoritos", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario", modifier = Modifier.size(32.dp), tint = Color.White)
                }
            }
        }
    }
}