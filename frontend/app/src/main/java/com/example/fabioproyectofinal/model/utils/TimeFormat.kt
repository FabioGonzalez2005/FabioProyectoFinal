package com.example.fabioproyectofinal.model.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.text.SimpleDateFormat

// Convertir fecha a formato largo (Lunes 20 de Mayo)
fun formatDateToLong(date: LocalDate): String {
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    val dayOfMonth = date.dayOfMonth
    val month = date.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    return "${dayOfWeek.replaceFirstChar { it.uppercase() }} $dayOfMonth de ${month.lowercase()}"
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
