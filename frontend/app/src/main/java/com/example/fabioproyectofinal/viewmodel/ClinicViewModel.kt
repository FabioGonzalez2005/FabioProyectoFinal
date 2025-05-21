package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Clinic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClinicViewModel : ViewModel() {
    private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
    val clinics: StateFlow<List<Clinic>> = _clinics

    // Copia local para filtrar sin llamar a la API cada vez
    private var allClinics: List<Clinic> = emptyList()

    fun fetchClinics(usuario_id: Int) {
        viewModelScope.launch {
            try {
                val result = ApiServer.apiService.getClinics(usuario_id)
                allClinics = result
                _clinics.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun buscarClinicas(query: String) {
        val lowerQuery = query.lowercase()

        val resultados = allClinics.filter { clinica ->
            val nombreMatch = clinica.nombre.lowercase().contains(lowerQuery)
            val direccionMatch = clinica.direccion.lowercase().contains(lowerQuery)
            val especialidadMatch = clinica.especialidad?.lowercase()?.contains(lowerQuery) == true
            Log.d("ClinicaFiltrada", "Clinica: ${clinica.nombre}, Especialidad: ${clinica.especialidad}")

            nombreMatch || direccionMatch || especialidadMatch
        }

        _clinics.value = resultados
    }

}

