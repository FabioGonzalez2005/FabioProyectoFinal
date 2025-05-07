package com.example.fabioproyectofinal.model.data.model

data class UsuarioLoginRequest(
    val usuario: String,
    val contraseña: String
)

data class LoginResponse(
    val msg: String? = null,
    val id_usuario: Int? = null,
    val nombre: String? = null,
    val email: String? = null,
    val error: String? = null
)
