package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Seguro(
    val id_seguro: Int,
    val nombre: String
)

// ViewModel encargado de manejar los seguros disponibles en la app
class InsuranceViewModel : ViewModel() {

    // Lista de todos los seguros disponibles
    private val _todosLosSeguros = MutableStateFlow<List<Seguro>>(emptyList())
    val todosLosSeguros: StateFlow<List<Seguro>> = _todosLosSeguros

    // Seguros específicos del usuario actual
    private val _segurosUsuario = MutableStateFlow<List<Seguro>>(emptyList())
    val segurosUsuario: StateFlow<List<Seguro>> = _segurosUsuario

    // Mapa de seguros agrupados por clínica (clave: id de clínica)
    private val _segurosClinicaMap = MutableStateFlow<Map<Int, List<Seguro>>>(emptyMap())
    val segurosClinicaMap: StateFlow<Map<Int, List<Seguro>>> = _segurosClinicaMap

    // Bandera para saber si los seguros del usuario ya se cargaron
    private val _segurosUsuarioCargados = MutableStateFlow(false)
    val segurosUsuarioCargados: StateFlow<Boolean> = _segurosUsuarioCargados

    // Mapa que indica si los seguros de cada clínica fueron cargados
    private val _segurosClinicaCargados = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val segurosClinicaCargados: StateFlow<Map<Int, Boolean>> = _segurosClinicaCargados

    // Carga todos los seguros del sistema desde la API
    fun cargarTodosLosSeguros() {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getAllSeguros()
                _todosLosSeguros.value = resultado
            } catch (e: Exception) {
                Log.e("InsuranceVM", "Error al cargar todos los seguros: ${e.message}")
            }
        }
    }

    // Carga los seguros asociados al usuario indicado
    fun cargarSegurosUsuario(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getSegurosUsuario(idUsuario)
                _segurosUsuario.value = resultado
                _segurosUsuarioCargados.value = true
            } catch (e: Exception) {
                Log.e("InsuranceVM", "Error al cargar seguros del usuario: ${e.message}")
            }
        }
    }

    // Carga los seguros disponibles para una clínica específica
    fun cargarSegurosClinica(idClinica: Int) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getSegurosDeClinica(idClinica)

                // Se actualiza el mapa de seguros por clínica
                _segurosClinicaMap.value = _segurosClinicaMap.value.toMutableMap().apply {
                    put(idClinica, resultado)
                }

                // Se marca esta clínica como cargada
                _segurosClinicaCargados.value = _segurosClinicaCargados.value.toMutableMap().apply {
                    put(idClinica, true)
                }
            } catch (e: Exception) {
                Log.e("InsuranceVM", "Error al cargar seguros de la clínica: ${e.message}")
            }
        }
    }
}