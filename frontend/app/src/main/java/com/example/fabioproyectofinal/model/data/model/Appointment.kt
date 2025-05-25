package com.example.fabioproyectofinal.model.data.model

data class Appointment(
    val id_cita: Int,
    val id_usuario: Int,
    val id_doctor: Int,
    val fecha_cita: String,
    val estado: String,
    val condiciones_pasadas: String,
    val procedimientos_quirurgicos: String,
    val alergias: String,
    val antecedentes_familiares: String,
    val medicamento_y_dosis: String,
    val nota: String,
    val nombre_paciente: String? = null,
    val email: String? = null,
    val motivo_cancelacion: String,
    val nombre: String,
    val fecha_nacimiento: String,
    val telefono: String,
    val telefono_emergencia: String,
)