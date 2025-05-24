package com.example.fabioproyectofinal.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight

// Componente para animar los botones y darles una interfaz bonita
@Composable
fun AnimatedDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Define la familia tipográfica personalizada usando la fuente 'afacadfont'
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // Estado para controlar si el botón está presionado o no
    var pressed by remember { mutableStateOf(false) }

    // Animación de escala que cambia cuando el botón está presionado (reduce tamaño)
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        label = "buttonScale"
    )

    Button(
        onClick = {
            pressed = true
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Texto blanco del botón
        Text(text, fontFamily = afacadFont, color = Color.White)
    }

    // Efecto que se ejecuta cuando cambia el estado 'pressed'
    LaunchedEffect(pressed) {
        if (pressed) {
            delay(100) // Espera breve para que se vea la animación de pulsación
            onClick()  // Ejecuta la acción proporcionada al presionar el botón
            pressed = false // Reinicia el estado para permitir futuras pulsaciones
        }
    }
}