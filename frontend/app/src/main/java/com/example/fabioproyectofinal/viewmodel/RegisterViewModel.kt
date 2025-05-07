package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.MensajeResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioRegistroRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import okhttp3.ResponseBody
import org.json.JSONObject

class RegisterViewModel : ViewModel() {

    private val _registroEstado = MutableStateFlow<MensajeResponse?>(null)
    val registroEstado: StateFlow<MensajeResponse?> = _registroEstado

    fun registrarUsuario(usuario: UsuarioRegistroRequest) {
        viewModelScope.launch {
            try {
                val respuesta = ApiServer.apiService.registrarUsuario(usuario)
                _registroEstado.value = respuesta
                Log.d("AuthVM", "Registro exitoso: ${respuesta.msg}")
            } catch (e: Exception) {
                var errorMsg = "Error desconocido"

                if (e is HttpException) {
                    val errorBody: ResponseBody? = e.response()?.errorBody()
                    errorBody?.string()?.let { json ->
                        try {
                            val obj = JSONObject(json)
                            errorMsg = obj.optString("error", errorMsg)
                        } catch (jsonEx: Exception) {
                            errorMsg = "Error al parsear respuesta del servidor"
                        }
                    }
                } else {
                    errorMsg = e.message ?: "Excepción desconocida"
                }

                Log.e("AuthVM", "Fallo en registro: $errorMsg", e)
                _registroEstado.value = MensajeResponse(error = errorMsg)
            }
        }
    }
}
