package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val response = ApiServer.apiService.getDoctors()
                Log.d("TESTBEBE", "Obtuve doctores: ${response.size}")
                _doctors.value = response
            } catch (e: Exception) {
                Log.e("TESTBEBE", "Error al obtener doctores: ${e.message}", e)
            }
        }
    }
}
