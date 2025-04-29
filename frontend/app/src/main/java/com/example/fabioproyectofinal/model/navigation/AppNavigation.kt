package com.example.fabioproyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fabioproyectofinal.view.screens.AppointmentsScreen
import com.example.fabioproyectofinal.view.screens.MainScreenApp
import com.example.fabioproyectofinal.view.screens.ClinicDetailScreen
import com.example.fabioproyectofinal.view.screens.FavouritesScreen
import com.example.fabioproyectofinal.view.screens.HistoryScreen
import com.example.fabioproyectofinal.view.screens.SelectProfessionalScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreenApp(navController)
        }
        composable("clinic_screen") { backStackEntry ->
            val clinicId = backStackEntry.arguments?.getString("clinicId")?.toIntOrNull()
            ClinicDetailScreen(navController)
        }
        composable("favourites_screen") {
            FavouritesScreen(navController)
        }
        composable("appointments_screen") {
            AppointmentsScreen(navController)
        }
        composable("history_screen") {
            HistoryScreen(navController)
        }
        composable("select_professional_screen") {
            SelectProfessionalScreen(navController)
        }
    }
}
