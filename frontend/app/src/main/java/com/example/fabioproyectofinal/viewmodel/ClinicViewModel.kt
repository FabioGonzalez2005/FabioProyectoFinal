package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Clinic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel encargado de obtener y filtrar la lista de clínicas
class ClinicViewModel : ViewModel() {

    // Estado observable de la lista de clínicas filtradas o completas
    private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
    val clinics: StateFlow<List<Clinic>> = _clinics

    // Copia local de todas las clínicas para realizar búsquedas sin volver a la API
    private var allClinics: List<Clinic> = emptyList()

    // Obtiene las clínicas desde la API según el ID del usuario
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
    // Filtra clínicas localmente por nombre, dirección o especialidad
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