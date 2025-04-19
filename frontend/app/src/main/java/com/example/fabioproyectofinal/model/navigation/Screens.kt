package com.example.fabioproyectofinal.model.navigation

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main_screen_app")
    object ClinicDetailScreen: AppScreens("clinic_screen")
}