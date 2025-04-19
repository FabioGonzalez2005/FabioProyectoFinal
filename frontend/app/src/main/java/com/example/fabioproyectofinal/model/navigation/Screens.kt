package com.example.fabioproyectofinal.model.navigation

import com.example.fabioproyectofinal.view.MainScreenApp
import com.example.fabioproyectofinal.view.ClinicDetailScreen

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main_screen_app")
    object ClinicDetailScreen: AppScreens("clinic_screen")
}