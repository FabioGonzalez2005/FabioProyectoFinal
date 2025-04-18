package com.example.fabioproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.fabioproyectofinal.ui.theme.FabioProyectoFInalTheme
import com.example.fabioproyectofinal.view.BottomNavigationBar
import com.example.fabioproyectofinal.view.MainScreenApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FabioProyectoFInalTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar() }
                ) { innerPadding ->
                    MainScreenApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
