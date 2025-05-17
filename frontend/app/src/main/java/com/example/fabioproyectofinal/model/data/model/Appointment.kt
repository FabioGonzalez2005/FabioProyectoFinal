package com.example.fabioproyectofinal.model.data.model

enum class AppointmentStatus {
    Confirmado,
    Pendiente,
    Cancelado
}

data class Appointment(
    val id_cita: Int,
    val id_usuario: Int,
    val id_doctor: Int,
    val fecha_cita: String,
    val estado: String
)