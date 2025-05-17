package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.Clinic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteClinicsViewModel : ViewModel() {

    private val _favoritas = MutableStateFlow<List<Clinic>>(emptyList())
    val favoritas: StateFlow<List<Clinic>> = _favoritas

    fun fetchFavoritas(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val response = ApiServer.apiService.getClinicasFavoritas(idUsuario)
                Log.d("FavoritosVM", "Clínicas favoritas obtenidas: ${response.size}")
                _favoritas.value = response
            } catch (e: Exception) {
                Log.e("FavoritosVM", "Error al obtener favoritas: ${e.message}", e)
            }
        }
    }
}