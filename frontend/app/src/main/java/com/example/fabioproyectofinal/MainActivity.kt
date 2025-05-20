package com.example.fabioproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fabioproyectofinal.navigation.AppNavigation
import com.example.fabioproyectofinal.ui.theme.FabioProyectoFInalTheme
import com.example.fabioproyectofinal.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = viewModel()

            FabioProyectoFInalTheme {
                Scaffold { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
