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

// ViewModel encargado de manejar la lógica de inicio de sesión
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Manejador de sesión para guardar y recuperar token e ID de usuario
    private val tokenManager = TokenManager(application.applicationContext)

    // Estado del resultado del login
    private val _loginEstado = MutableStateFlow<LoginResponse?>(null)
    val loginEstado: StateFlow<LoginResponse?> = _loginEstado

    // Función que realiza el proceso de inicio de sesión
    fun login(usuario: UsuarioLoginRequest) {
        viewModelScope.launch {
            try {
                // Llamada a la API para autenticar al usuario
                val respuesta = ApiServer.apiService.loginUsuario(usuario)

                // Actualiza el estado con la respuesta del servidor
                _loginEstado.value = respuesta

                Log.d("LoginVM", "Login exitoso: ${respuesta.msg}")

                // Guarda el token y el ID del usuario en preferencias seguras
                val idUs = respuesta.id_usuario?.toInt() ?: -1
                val tok = respuesta.token?.toString() ?: ""
                tokenManager.saveSession(tok, idUs)

            } catch (e: Exception) {
                var errorMsg = "Error desconocido"

                // Manejo de errores HTTP con extracción del mensaje de error
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

                // Actualiza el estado con el error para notificar a la UI
                _loginEstado.value = LoginResponse(error = errorMsg)
            }
        }
    }

    // Flujo para observar el token guardado
    fun getTokenFlow() = tokenManager.token

    // Flujo para observar el ID de usuario guardado
    fun getUserIdFlow() = tokenManager.userId

    // Función para cerrar sesión limpiando los datos almacenados
    fun logout() {
        viewModelScope.launch {
            tokenManager.clearSession()
        }
    }
}