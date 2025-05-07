package com.example.fabioproyectofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.LoginResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    private val _loginEstado = MutableStateFlow<LoginResponse?>(null)
    val loginEstado: StateFlow<LoginResponse?> = _loginEstado

    fun login(usuario: UsuarioLoginRequest) {
        viewModelScope.launch {
            try {
                val respuesta = ApiServer.apiService.loginUsuario(usuario)
                _loginEstado.value = respuesta
                Log.d("LoginVM", "Login exitoso: ${respuesta.msg}")
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

                Log.e("LoginVM", "Fallo en login: $errorMsg", e)
                _loginEstado.value = LoginResponse(error = errorMsg)
            }
        }
    }
}
