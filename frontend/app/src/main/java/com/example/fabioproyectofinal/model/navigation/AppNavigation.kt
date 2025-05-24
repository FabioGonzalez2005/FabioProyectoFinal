package com.example.fabioproyectofinal.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fabioproyectofinal.view.screens.RegisterScreen
import com.example.fabioproyectofinal.view.screens.AppointmentsScreen
import com.example.fabioproyectofinal.view.screens.MainScreenApp
import com.example.fabioproyectofinal.view.screens.ClinicDetailScreen
import com.example.fabioproyectofinal.view.screens.DoctorListScreen
import com.example.fabioproyectofinal.view.screens.FavouritesScreen
import com.example.fabioproyectofinal.view.screens.HistoryScreen
import com.example.fabioproyectofinal.view.screens.LoginScreen
import com.example.fabioproyectofinal.view.screens.SelectProfessionalScreen
import com.example.fabioproyectofinal.viewmodel.LoginViewModel
import androidx.compose.runtime.getValue
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.session.SessionManager

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val token by loginViewModel.getTokenFlow().collectAsState(initial = null)
    val userId by loginViewModel.getUserIdFlow().collectAsState(initial = null)

    LaunchedEffect(token, userId) {
        userId?.takeIf { !token.isNullOrBlank() && it != -1 }?.let { id ->
            try {
                val perfil = ApiServer.apiService.obtenerPerfil(id).firstOrNull()
                perfil?.let {
                    SessionManager.idUsuario = it.id_usuario
                    SessionManager.nombre = it.nombre
                    SessionManager.email = it.email
                    SessionManager.username = it.usuario
                    SessionManager.fecha_nacimiento = it.fecha_nacimiento
                    SessionManager.telefono = it.telefono
                    SessionManager.telefono_emergencia = it.telefono_emergencia
                    SessionManager.alergias = it.alergias
                    SessionManager.antecedentes_familiares = it.antecedentes_familiares
                    SessionManager.condiciones_pasadas = it.condiciones_pasadas
                    SessionManager.procedimientos_quirurgicos = it.procedimientos_quirurgicos
                }

                navController.navigate("main_screen_app/$id") {
                    popUpTo("main") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error al cargar perfil del usuario", e)
            }
        }
    }

    NavHost(navController = navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            LoginScreen(navController)
        }
        composable("clinic_screen/{id_usuario}/{id_clinica}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            val idClinica = backStackEntry.arguments?.getString("id_clinica")?.toIntOrNull()
            ClinicDetailScreen(navController, userId, idClinica)
        }

        composable("main_screen_app/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            MainScreenApp(navController, userId)
        }
        composable("favourites_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            FavouritesScreen(navController, userId)
        }
        composable("appointments_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            AppointmentsScreen(navController, userId)
        }
        composable("history_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            HistoryScreen(navController, userId)
        }
        composable("select_professional_screen/{id_usuario}/{id_doctor}/{nombreDoctor}/{nombreClinica}/{precio}/{especialidad}")
        { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            val idDoctor = backStackEntry.arguments?.getString("id_doctor")?.toIntOrNull()
            val nombreDoctor = backStackEntry.arguments?.getString("nombreDoctor")?.replace("-", " ") ?: ""
            val nombreClinica = backStackEntry.arguments?.getString("nombreClinica")?.replace("-", " ") ?: ""
            val precio = backStackEntry.arguments?.getString("precio")?.replace("-", " ") ?: ""
            val especialidad = backStackEntry.arguments?.getString("especialidad")?.replace("-", " ") ?: ""

            SelectProfessionalScreen(
                navController = navController,
                userId = userId,
                idDoctor = idDoctor ?: -1,
                nombreDoctor = nombreDoctor,
                nombreClinica = nombreClinica,
                precio = precio,
                especialidad = especialidad
            )
        }


        composable("register_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            RegisterScreen(navController)
        }
        composable("login_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            LoginScreen(navController)
        }
        composable("doctor_list_screen/{id_usuario}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id_usuario")?.toIntOrNull()
            DoctorListScreen(navController, userId)
        }
    }

}
