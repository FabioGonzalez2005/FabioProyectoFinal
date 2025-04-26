package com.example.fabioproyectofinal.model.navigation

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main")
    object FavouritesScreen: AppScreens("favourites_screen")
    object ClinicDetailScreen: AppScreens("clinic_screen")
    object SelectProfessionalScreen: AppScreens("select_professional_screen")
}