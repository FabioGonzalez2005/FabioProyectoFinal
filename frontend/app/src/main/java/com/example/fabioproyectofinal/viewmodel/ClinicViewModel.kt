package com.example.fabioproyectofinal.viewmodel

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

    init {
        fetchClinics()
    }

    private fun fetchClinics() {
        viewModelScope.launch {
            try {
                val result = ApiServer.apiService.getClinics()
                _clinics.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
