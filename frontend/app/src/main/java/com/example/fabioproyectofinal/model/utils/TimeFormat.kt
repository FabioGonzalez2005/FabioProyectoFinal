package com.example.fabioproyectofinal.model.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.text.SimpleDateFormat
import java.time.ZonedDateTime

// Convertir fecha a formato de hora (12:30)
fun formatTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (_: Exception) {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
            dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (_: Exception) {
            try {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            } catch (_: Exception) {
                dateTimeString
            }
        }
    }
}

// Convertir hora a formato sin segundos (12:30)
fun formatHour(time: String): String {
    return time.trim().take(5)
}

fun formatFecha(fechaRaw: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
        val date = inputFormat.parse(fechaRaw)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        fechaRaw
    }
}

fun formatHora(fechaRaw: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("HH:mm", Locale("es", "ES"))
        val date = inputFormat.parse(fechaRaw)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        fechaRaw
    }
}

fun formatFechaCompleta(fechaString: String): String {
    return try {
        val formatter = DateTimeFormatter.RFC_1123_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(fechaString, formatter)
        val date = zonedDateTime.toLocalDate()

        val diaSemana = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        val dia = String.format("%02d", date.dayOfMonth)
        val mes = date.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        val anio = date.year

        "${diaSemana.replaceFirstChar { it.uppercase() }}, $dia de ${mes.replaceFirstChar { it.uppercase() }}, $anio"
    } catch (e: Exception) {
        println("⚠️ Error al parsear fecha: $fechaString → ${e.message}")
        "Fecha: $fechaString"
    }
}


