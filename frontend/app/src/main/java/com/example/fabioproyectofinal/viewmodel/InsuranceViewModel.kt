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

class InsuranceViewModel : ViewModel() {

    private val _todosLosSeguros = MutableStateFlow<List<Seguro>>(emptyList())
    val todosLosSeguros: StateFlow<List<Seguro>> = _todosLosSeguros

    private val _segurosUsuario = MutableStateFlow<List<Seguro>>(emptyList())
    val segurosUsuario: StateFlow<List<Seguro>> = _segurosUsuario

    private val _segurosClinica = MutableStateFlow<List<Seguro>>(emptyList())
    val segurosClinica: StateFlow<List<Seguro>> = _segurosClinica

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

    fun cargarSegurosUsuario(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getSegurosUsuario(idUsuario)
                _segurosUsuario.value = resultado
            } catch (e: Exception) {
                Log.e("InsuranceVM", "Error al cargar seguros del usuario: ${e.message}")
            }
        }
    }

    fun cargarSegurosClinica(idClinica: Int) {
        viewModelScope.launch {
            try {
                val resultado = ApiServer.apiService.getSegurosDeClinica(idClinica)
                _segurosClinica.value = resultado
            } catch (e: Exception) {
                Log.e("InsuranceVM", "Error al cargar seguros de la clínica: ${e.message}")
            }
        }
    }
}
