package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Availability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

// ViewModel encargado de manejar la disponibilidad de doctores y reservas
class AvailabilityViewModel : ViewModel() {
    // Lista observable de franjas de disponibilidad
    private val _disponibilidad = MutableStateFlow<List<Availability>>(emptyList())
    val disponibilidad: StateFlow<List<Availability>> = _disponibilidad

    // Carga toda la disponibilidad de un doctor
    fun cargarDisponibilidad(idDoctor: Int) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getDisponibilidadDoctor(idDoctor)
                _disponibilidad.value = resultado
                Log.d("AvailabilityVM", "Disponibilidad cargada: ${resultado.size} franjas")
            } catch (e: Exception) {
                Log.e("AvailabilityVM", "Error al cargar disponibilidad: ${e.message}", e)
            }
        }
    }

    // Carga la disponibilidad de un doctor en un día específico
    fun cargarDisponibilidadPorDia(idDoctor: Int, fecha: LocalDate) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getDisponibilidadPorDia(
                    idDoctor,
                    fecha.toString() // Formato esperado: yyyy-MM-dd
                )
                _disponibilidad.value = resultado
            } catch (e: Exception) {
                Log.e("AvailabilityVM", "Error al cargar disponibilidad por día: ${e.message}")
            }
        }
    }

    // Realiza la reserva de una franja horaria y notifica el resultado
    fun reservarFranja(idDisponibilidad: Int, idUsuario: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = ApiServer.apiService.reservarFranja(
                    mapOf(
                        "id_disponibilidad" to idDisponibilidad,
                        "id_usuario" to idUsuario
                    )
                )
                Log.d("AvailabilityVM", "Cita reservada con éxito")
                onResult(true)
            } catch (e: Exception) {
                Log.e("AvailabilityVM", "Error reservando franja: ${e.message}")
                onResult(false)
            }
        }
    }
}