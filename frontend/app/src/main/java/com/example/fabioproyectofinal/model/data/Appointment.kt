package com.example.fabioproyectofinal.model.data

import com.example.fabioproyectofinal.R

// Enum que define el estado de la cita
enum class AppointmentStatus {
    CONFIRMED,
    REJECTED
}

data class Appointment(
    val id: Int,
    val name: String,
    val address: String,
    val src: Int,
    val date: String,
    val time: String,
    val status: AppointmentStatus,
)

// Lista de citas simuladas
val appointments = listOf(
    Appointment(
        id = 1,
        name = "Hospiten Lanzarote",
        address = "Cam. Lomo Gordo, s/n, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.hospiten,
        date = "04/06/2025",
        time = "13:00",
        status = AppointmentStatus.CONFIRMED
    ),
    Appointment(
        id = 2,
        name = "International Clinic",
        address = "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.international,
        date = "05/06/2025",
        time = "12:00",
        status = AppointmentStatus.REJECTED
    )
)
