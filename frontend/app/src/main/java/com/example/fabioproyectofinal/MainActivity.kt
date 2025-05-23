package com.example.fabioproyectofinal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Citas"
            val descriptionText = "Canal para notificaciones de reservas"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("appointment_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
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
