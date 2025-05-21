package com.example.fabioproyectofinal.model.navigation

sealed class AppScreens(val route: String) {
    // Pantallas
    object MainScreenApp: AppScreens("main_screen_app/{id_usuario}")
    object FavouritesScreen: AppScreens("favourites_screen/{id_usuario}")
    object AppointmentsScreen: AppScreens("appointments_screen/{id_usuario}")
    object HistoryScreen: AppScreens("history_screen/{id_usuario}")
    object ClinicDetailScreen : AppScreens("clinic_screen/{id_usuario}/{id_clinica}") {
        fun createRoute(idUsuario: Int, idClinica: Int) = "clinic_screen/$idUsuario/$idClinica"
    }
    object SelectProfessionalScreen: AppScreens("select_professional_screen/{id_usuario}")
    object RegisterScreen: AppScreens("register_screen/{id_usuario}")
    object LoginScreen: AppScreens("login_screen/{id_usuario}")
    object DoctorListScreen: AppScreens("doctor_list_screen/{id_usuario}")
}