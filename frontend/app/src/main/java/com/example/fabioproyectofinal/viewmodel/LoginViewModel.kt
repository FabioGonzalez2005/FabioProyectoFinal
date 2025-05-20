package com.example.fabioproyectofinal.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.data.model.LoginResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import com.example.fabioproyectofinal.model.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application.applicationContext)

    private val _loginEstado = MutableStateFlow<LoginResponse?>(null)
    val loginEstado: StateFlow<LoginResponse?> = _loginEstado

    fun login(usuario: UsuarioLoginRequest) {
        viewModelScope.launch {
            try {
                val respuesta = ApiServer.apiService.loginUsuario(usuario)
                _loginEstado.value = respuesta
                Log.d("LoginVM", "Login exitoso: ${respuesta.msg}")
                val idUs = respuesta.id_usuario?.toInt() ?: -1
                val tok = respuesta.token?.toString() ?: ""
                tokenManager.saveSession(tok, idUs)

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

    fun getTokenFlow() = tokenManager.token
    fun getUserIdFlow() = tokenManager.userId

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearSession()
        }
    }
}
