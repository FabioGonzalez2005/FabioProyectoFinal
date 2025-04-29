package com.example.fabioproyectofinal.model.data

import com.example.fabioproyectofinal.R

data class History(
    val id: Int,
    val name: String,
    val address: String,
    val src: Int,
    val date: String,
    val time: String,
    val professional: String,
    val specialty: String
)

// Lista de citas pasadas
val pastAppointments = listOf(
    History(
        id = 1,
        name = "Hospiten Lanzarote",
        address = "Cam. Lomo Gordo, s/n, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.hospiten,
        date = "04/06/2025",
        time = "13:00",
        professional = "Alberto Medina",
        specialty = "Osteópata",
    ),
    History(
        id = 2,
        name = "International Clinic",
        address = "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.international,
        date = "05/06/2025",
        time = "12:00",
        professional = "Alberto Medina",
        specialty = "Osteópata",
    )
)
