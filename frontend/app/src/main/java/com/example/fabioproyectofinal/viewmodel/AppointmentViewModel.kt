package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel : ViewModel() {

    private val _citas = MutableStateFlow<List<Appointment>>(emptyList())
    val citas: StateFlow<List<Appointment>> = _citas

    init {
        fetchCitas()
    }

    private fun fetchCitas() {
        viewModelScope.launch {
            try {
                val response = ApiServer.apiService.getCitas()
                Log.d("CitaVM", "Obtuve citas: ${response.size}")
                _citas.value = response
            } catch (e: Exception) {
                Log.e("CitaVM", "Error al obtener citas: ${e.message}", e)
            }
        }
    }
}
