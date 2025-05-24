package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Muestra un mapa de Google con marcador centrado en una ubicación específica
@Composable
fun GoogleMapView(lat: Double, lng: Double) {
    // Estado de la cámara del mapa centrada en la latitud y longitud proporcionadas
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 15f)
    }

    // Controla si el mapa ha terminado de cargarse
    val isMapLoading = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Componente de mapa de Google con un marcador en la ubicación indicada
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoading.value = false // Oculta el indicador cuando el mapa esté listo
            }
        ) {
            // Marcador que señala la ubicación de la clínica
            Marker(
                state = MarkerState(position = LatLng(lat, lng)),
                title = "Clínica"
            )
        }

        // Indicador de carga mientras el mapa se inicializa
        if (isMapLoading.value) {
            CircularProgressIndicator(
                color = Color(0xFFB2C2A4)
            )
        }
    }
}