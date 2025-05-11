package com.example.fabioproyectofinal.model.data.model

data class Clinic(
    val id_clinica: Int,
    val nombre: String,
    val direccion: String,
    val src: String,
    val telefono: Int,
    val web: String,
    val inFavourites: Boolean
)