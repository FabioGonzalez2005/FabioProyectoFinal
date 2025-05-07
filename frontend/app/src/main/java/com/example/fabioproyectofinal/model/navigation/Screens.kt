package com.example.fabioproyectofinal.model.navigation

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main")
    object FavouritesScreen: AppScreens("favourites_screen")
    object AppointmentsScreen: AppScreens("appointments_screen")
    object HistoryScreen: AppScreens("history_screen")
    object ClinicDetailScreen: AppScreens("clinic_screen")
    object SelectProfessionalScreen: AppScreens("select_professional_screen")
    object RegisterScreen: AppScreens("register_screen")
    object LoginScreen: AppScreens("login_screen")
}