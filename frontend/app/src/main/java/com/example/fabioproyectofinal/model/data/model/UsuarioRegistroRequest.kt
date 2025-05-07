package com.example.fabioproyectofinal.model.data.model

data class UsuarioRegistroRequest(
    val nombre: String,
    val email: String,
    val usuario: String,
    val contraseña: String
)

data class MensajeResponse(
    val msg: String? = null,
    val error: String? = null
)