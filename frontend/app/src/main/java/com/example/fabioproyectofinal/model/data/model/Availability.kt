package com.example.fabioproyectofinal.model.data.model

data class Availability(
    val id_disponibilidad: Int,
    val dia_semana: String,
    val hora_inicio: String,
    val hora_fin: String,
    val disponible: Boolean
)