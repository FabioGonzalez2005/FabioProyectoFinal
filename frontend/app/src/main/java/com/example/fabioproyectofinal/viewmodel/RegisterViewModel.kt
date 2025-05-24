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

// ViewModel encargado de manejar el registro de usuarios
class RegisterViewModel : ViewModel() {

    // Estado interno (privado) que mantiene la respuesta del intento de registro
    private val _registroEstado = MutableStateFlow<MensajeResponse?>(null)

    // Exposición pública e inmutable del estado para observar desde la UI
    val registroEstado: StateFlow<MensajeResponse?> = _registroEstado

    // Función para registrar un usuario, recibiendo una solicitud de tipo UsuarioRegistroRequest
    fun registrarUsuario(usuario: UsuarioRegistroRequest) {
        // Lanza una corrutina en el alcance del ViewModel
        viewModelScope.launch {
            try {
                // Intenta llamar al servicio API para registrar el usuario
                val respuesta = ApiServer.apiService.registrarUsuario(usuario)

                // Si la llamada es exitosa, actualiza el estado con la respuesta
                _registroEstado.value = respuesta

                Log.d("RegisterVM", "Registro exitoso: ${respuesta.msg}")
            } catch (e: Exception) {
                var errorMsg = "Error desconocido"

                // Si la excepción fue una HttpException (error de servidor)
                if (e is HttpException) {
                    // Obtiene el cuerpo del error de la respuesta HTTP
                    val errorBody: ResponseBody? = e.response()?.errorBody()

                    // Intenta extraer un mensaje de error legible del cuerpo JSON
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

                Log.e("RegisterVM", "Fallo en registro: $errorMsg", e)

                // Actualiza el estado con un mensaje de error para mostrar en UI
                _registroEstado.value = MensajeResponse(error = errorMsg)
            }
        }
    }
}