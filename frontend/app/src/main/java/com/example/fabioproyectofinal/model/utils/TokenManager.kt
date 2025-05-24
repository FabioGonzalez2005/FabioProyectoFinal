package com.example.fabioproyectofinal.model.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear un DataStore de preferencias con el nombre "auth_preferences"
val Context.dataStore by preferencesDataStore(name = "auth_preferences")

// Objeto que contiene las claves para las preferencias almacenadas
object TokenKeys {
    val AUTH_TOKEN = stringPreferencesKey("auth_token")  // Clave para el token de autenticación (String)
    val USER_ID = intPreferencesKey("user_id")           // Clave para el ID del usuario (Int)
}

// Clase encargada de manejar el almacenamiento y recuperación del token y userId
class TokenManager(private val context: Context) {

    // Flow que emite el token de autenticación almacenado (puede ser null si no existe)
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TokenKeys.AUTH_TOKEN]
    }

    // Flow que emite el ID de usuario almacenado (puede ser null si no existe)
    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[TokenKeys.USER_ID]
    }

    // Función suspendida para guardar token y userId en DataStore
    suspend fun saveSession(token: String, userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[TokenKeys.AUTH_TOKEN] = token
            preferences[TokenKeys.USER_ID] = userId
        }
    }

    // Función suspendida para limpiar todas las preferencias guardadas (cerrar sesión)
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}