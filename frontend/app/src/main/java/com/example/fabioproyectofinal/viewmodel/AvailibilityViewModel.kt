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

class AvailabilityViewModel : ViewModel() {

    private val _disponibilidad = MutableStateFlow<List<Availability>>(emptyList())
    val disponibilidad: StateFlow<List<Availability>> = _disponibilidad

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

    fun cargarDisponibilidadPorDia(idDoctor: Int, fecha: LocalDate) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getDisponibilidadPorDia(
                    idDoctor,
                    fecha.toString() // se convierte automáticamente a yyyy-MM-dd
                )
                _disponibilidad.value = resultado
            } catch (e: Exception) {
                Log.e("AvailabilityVM", "Error al cargar disponibilidad por día: ${e.message}")
            }
        }
    }

}
