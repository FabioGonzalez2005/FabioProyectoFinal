package com.example.fabioproyectofinal.model.data.model

import com.example.fabioproyectofinal.R

data class Clinic(
    val id: Int,
    val name: String,
    val address: String,
    val src: Int
)

// Datos simulados
val clinics = listOf(
    Clinic(1,"Hospiten Lanzarote", "Cam. Lomo Gordo, s/n, 35510 Puerto del Carmen, Las Palmas", R.drawable.hospiten),
    Clinic(2, "International Clinic", "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas", R.drawable.international),
    Clinic(3, "Clínicas Dr. Mager - Deutsche Ärzte", "Avda de las Playas, C. Chalana, 37, 35510 Puerto del Carmen", R.drawable.mager),
    Clinic(4, "Clínicas Dr. Mager - Deutsche Ärzte", "Avda de las Playas, C. Chalana, 37, 35510 Puerto del Carmen", R.drawable.mager),
    Clinic(5, "Clínicas Dr. Mager - Deutsche Ärzte", "Avda de las Playas, C. Chalana, 37, 35510 Puerto del Carmen", R.drawable.mager)

)