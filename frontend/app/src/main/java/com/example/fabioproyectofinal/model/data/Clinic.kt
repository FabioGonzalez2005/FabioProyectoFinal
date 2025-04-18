package com.example.fabioproyectofinal.model.data

import com.example.fabioproyectofinal.R

data class Clinic(
    val nombre: String,
    val direccion: String,
    val imagen: Int
)

// Datos simulados
val clinics = listOf(
    Clinic("Hospiten Lanzarote", "Cam. Lomo Gordo, s/n, 35510 Puerto del Carmen, Las Palmas", R.drawable.hospiten),
    Clinic("International Clinic", "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas", R.drawable.international),
    Clinic("Clínicas Dr. Mager - Deutsche Ärzte", "Avda de las Playas, C. Chalana, 37, 35510 Puerto del Carmen", R.drawable.mager)
)