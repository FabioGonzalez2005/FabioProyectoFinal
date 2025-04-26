package com.example.fabioproyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fabioproyectofinal.view.screens.MainScreenApp
import com.example.fabioproyectofinal.view.screens.ClinicDetailScreen

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
    }
}
