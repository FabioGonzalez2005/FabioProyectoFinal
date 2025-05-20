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

@Composable
fun AnimatedDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var pressed by remember { mutableStateOf(false) }
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
        Text(text, fontFamily = afacadFont, color = Color.White)
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            delay(100)
            onClick()
            pressed = false
        }
    }
}
