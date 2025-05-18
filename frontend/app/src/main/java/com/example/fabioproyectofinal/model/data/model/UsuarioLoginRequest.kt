package com.example.fabioproyectofinal.model.data.model

data class UsuarioLoginRequest(
    val usuario: String,
    val contraseña: String
)

data class LoginResponse(
    val msg: String? = null,
    val id_usuario: Int? = null,
    val usuario: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    val error: String? = null,
    val fecha_nacimiento: String? = null,
    val telefono: String? = null,
    val contacto_emergencia: String? = null,
    val alergias: String? = null,
    val antecedentes_familiares: String? = null,
    val condiciones_pasadas: String? = null,
    val procedimientos_quirurgicos: String? = null
)