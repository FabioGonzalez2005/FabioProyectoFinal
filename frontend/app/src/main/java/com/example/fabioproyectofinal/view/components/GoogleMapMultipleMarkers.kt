package com.example.fabioproyectofinal.view.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.maps.android.compose.*
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fabioproyectofinal.model.navigation.AppScreens
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.launch

// Buscar clínicas desde el Mapa
@SuppressLint("MissingPermission")
@Composable
fun GoogleMapWithClinics(
    clinics: List<Clinic>,
    navController: NavHostController,
    userId: Int,
    onDismiss: () -> Unit
) {
    // LatLng inicial del mapa (IES Haria)
    val iesHariaLatLng = LatLng(29.1453, -13.4863)

    // Estado de la cámara, inicializado centrado en iesHariaLatLng con zoom 11
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(iesHariaLatLng, 11f)
    }

    // Estado para almacenar la clínica seleccionada actualmente (o null si ninguna)
    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }

    // Almacena el id de la última clínica clickeada para controlar selección/deselección
    var lastClickedId by remember { mutableStateOf<Int?>(null) }

    // CoroutineScope para lanzar animaciones y otras tareas asincrónicas
    val coroutineScope = rememberCoroutineScope()

    // Componente GoogleMap que muestra el mapa
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false), // No muestra ubicación actual
        onMapClick = {
            // Al hacer click fuera de un marcador, se deselecciona la clínica
            selectedClinic = null
            lastClickedId = null
        }
    ) {
        // Itera sobre la lista de clínicas para colocar marcadores en el mapa
        clinics.forEach { clinic ->
            val position = LatLng(clinic.lat, clinic.lng)
            Marker(
                state = MarkerState(position = position),
                title = clinic.nombre,
                snippet = clinic.direccion,
                onClick = {
                    if (lastClickedId == clinic.id_clinica) {
                        // Si el marcador ya estaba seleccionado, deseleccionarlo
                        lastClickedId = null
                        selectedClinic = null
                    } else {
                        // Seleccionar la clínica clickeada y guardar su id
                        selectedClinic = clinic
                        lastClickedId = clinic.id_clinica

                        // Animar la cámara para acercarse a la clínica con un ligero desplazamiento vertical
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(
                                    LatLng(clinic.lat - 0.01, clinic.lng),
                                    13f
                                ),
                                durationMs = 600
                            )
                        }
                    }
                    true // Consume el evento click para que no se propague
                }
            )
        }
    }

    // Si hay una clínica seleccionada, mostrar una tarjeta con detalles en la parte inferior
    selectedClinic?.let { clinic ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                ClinicaCard(
                    clinic = clinic,
                    navController = navController,
                    userId = userId,
                    inFavourites = clinic.inFavourites,
                    isClickable = true,
                    mostrarIconoVacio = false,
                    botonFavoritoActivo = false,
                    mostrarBotonSeguros = false,
                    paddingActivo = false,
                    mostrarFavoritos = false,
                    mostrarCompatibilidad = false,
                    onClick = {
                        // Al pulsar la tarjeta, cerrar el diálogo y navegar al detalle de la clínica
                        onDismiss()
                        navController.navigate(AppScreens.ClinicDetailScreen.createRoute(userId, clinic.id_clinica))
                    }
                )
            }
        }
    }
}