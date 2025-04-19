package com.example.fabioproyectofinal.model.navigation

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main")
    object ClinicDetailScreen: AppScreens("clinic_screen")
}