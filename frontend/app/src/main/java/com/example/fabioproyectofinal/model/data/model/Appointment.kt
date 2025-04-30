package com.example.fabioproyectofinal.model.data.model

import com.example.fabioproyectofinal.R

// Enum que define el estado de la cita
enum class AppointmentStatus {
    Confirmado,
    Pendiente,
    Cancelado
}

data class Appointment(
    val id: Int,
    val name: String,
    val address: String,
    val src: Int,
    val date: String,
    val time: String,
    val status: AppointmentStatus,
    val professional: String,
    val specialty: String
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
        status = AppointmentStatus.Confirmado,
        professional = "Alberto Medina",
        specialty = "Osteópata"
    ),
    Appointment(
        id = 2,
        name = "International Clinic",
        address = "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.international,
        date = "05/06/2025",
        time = "12:00",
        status = AppointmentStatus.Pendiente,
        professional = "Alberto Medina",
        specialty = "Osteópata"

    ),
    Appointment(
        id = 2,
        name = "International Clinic",
        address = "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.international,
        date = "05/06/2025",
        time = "12:00",
        status = AppointmentStatus.Pendiente,
        professional = "Alberto Medina",
        specialty = "Osteópata"
    ),
    Appointment(
        id = 2,
        name = "International Clinic",
        address = "C. Acatife, 9, 35510 Puerto del Carmen, Las Palmas",
        src = R.drawable.international,
        date = "05/06/2025",
        time = "12:00",
        status = AppointmentStatus.Cancelado,
        professional = "Alberto Medina",
        specialty = "Osteópata"
    )
)
