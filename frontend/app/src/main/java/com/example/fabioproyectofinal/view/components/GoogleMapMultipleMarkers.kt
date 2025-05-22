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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.fabioproyectofinal.model.navigation.AppScreens


@SuppressLint("MissingPermission")
@Composable
fun GoogleMapWithClinics(
    clinics: List<Clinic>,
    navController: NavHostController,
    userId: Int,
    onDismiss: () -> Unit
)
 {
    val iesHariaLatLng = LatLng(29.1453, -13.4863)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(iesHariaLatLng, 11f)
    }

    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }
    var lastClickedId by remember { mutableStateOf<Int?>(null) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false)
    ) {
        clinics.forEach { clinic ->
            val position = LatLng(clinic.lat, clinic.lng)
            Marker(
                state = MarkerState(position = position),
                title = clinic.nombre,
                snippet = clinic.direccion,
                onClick = {
                    if (lastClickedId == clinic.id_clinica) {
                        navController.navigate(
                            AppScreens.ClinicDetailScreen.createRoute(userId, clinic.id_clinica)
                        )
                        lastClickedId = null
                        selectedClinic = null
                    } else {
                        selectedClinic = clinic
                        lastClickedId = clinic.id_clinica
                    }
                    true
                }
            )
        }
    }

    selectedClinic?.let { clinic ->
        Box(modifier = Modifier.fillMaxSize()) {
            selectedClinic?.let { clinic ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // ← aquí sí es válido
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
                        onClick = {
                            onDismiss()
                            navController.navigate(AppScreens.ClinicDetailScreen.createRoute(userId, clinic.id_clinica))
                        }
                    )

                }
            }
        }
    }
}
