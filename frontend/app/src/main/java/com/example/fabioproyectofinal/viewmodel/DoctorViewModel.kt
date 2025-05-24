package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel encargado de obtener y exponer la lista de doctores
class DoctorViewModel : ViewModel() {

    // Lista de doctores obtenida desde la API
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    // Al inicializar el ViewModel se cargan los doctores automáticamente
    init {
        fetchDoctors()
    }

    // Realiza la llamada a la API para obtener la lista de doctores
    private fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val response = ApiServer.apiService.getDoctors()
                Log.d("DoctorVM", "Obtuve doctores: ${response.size}")
                _doctors.value = response
            } catch (e: Exception) {
                Log.e("DoctorVM", "Error al obtener doctores: ${e.message}", e)
            }
        }
    }
}