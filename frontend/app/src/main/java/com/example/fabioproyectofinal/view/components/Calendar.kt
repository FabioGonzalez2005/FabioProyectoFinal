package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R


@Composable
fun CalendarComponent(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    workingDays: List<Int> = listOf(1, 2, 3, 4, 5),
    onMonthChanged: (YearMonth) -> Unit = {},
    initialMonth: YearMonth? = null,
) {
    // Fuente personalizada para los textos del calendario
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // Fecha actual guardada en estado para evitar recomposiciones innecesarias
    val today = remember { LocalDate.now() }
    // Mes actual basado en la fecha de hoy
    val currentActualMonth = remember { YearMonth.from(today) }
    // Mes inicial que se mostrará, puede venir por parámetro o ser el mes actual
    val startMonth = initialMonth ?: currentActualMonth

    // Límite máximo de mes permitido (16 meses adelante desde el actual)
    val maxAllowedMonth = remember { currentActualMonth.plusMonths(16) }
    // Mes mínimo permitido (mes actual)
    val minAllowedMonth = currentActualMonth

    // Mes que se está mostrando en el calendario, estado mutable para actualizaciones
    var displayedYearMonth by remember { mutableStateOf(startMonth) }

    // Primer día del mes que se muestra
    val firstDayOfMonth = displayedYearMonth.atDay(1)
    // Número total de días en el mes mostrado
    val daysInMonth = displayedYearMonth.lengthOfMonth()
    // Índice del primer día de la semana para alinear los días (0=lunes)
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7

    // Condición para habilitar el botón de mes anterior
    val canGoToPreviousMonth = displayedYearMonth.isAfter(minAllowedMonth) || displayedYearMonth == minAllowedMonth
    // Condición para habilitar el botón de mes siguiente
    val canGoToNextMonth = displayedYearMonth.isBefore(maxAllowedMonth)

    Column {
        // Tarjeta que contiene la cabecera del calendario con controles de mes
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título "Disponibilidad"
                Text(
                    text = "Disponibilidad:",
                    fontSize = 16.sp,
                    fontFamily = afacadFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB2C2A4)
                )

                // Fila con botones para cambiar mes y mostrar mes y año actual
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón para ir al mes anterior si está permitido
                    IconButton(
                        onClick = {
                            if (canGoToPreviousMonth) {
                                displayedYearMonth = displayedYearMonth.minusMonths(1)
                                onMonthChanged(displayedYearMonth)
                            }
                        },
                        enabled = canGoToPreviousMonth
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Mes anterior",
                            tint = if (canGoToPreviousMonth) Color(0xFFB2C2A4) else Color.Gray.copy(alpha = 0.5f)
                        )
                    }

                    // Texto que muestra el mes y año actual formateados
                    Text(
                        text = "${displayedYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                            .replaceFirstChar { it.uppercase() }} ${displayedYearMonth.year}",
                        color = Color(0xFFB2C2A4),
                        fontFamily = afacadFont,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Botón para ir al mes siguiente si está permitido
                    IconButton(
                        onClick = {
                            if (canGoToNextMonth) {
                                displayedYearMonth = displayedYearMonth.plusMonths(1)
                                onMonthChanged(displayedYearMonth)
                            }
                        },
                        enabled = canGoToNextMonth
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Mes siguiente",
                            modifier = Modifier.rotate(180f), // Rota el icono para apuntar a la derecha
                            tint = if (canGoToNextMonth) Color(0xFFB2C2A4) else Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Fila para mostrar los nombres de los días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Lista abreviada de días de la semana (lunes a domingo)
            val daysOfWeek = listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do")
            daysOfWeek.forEach { day ->
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontFamily = afacadFont,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Rejilla vertical para mostrar todos los días del mes en 7 columnas
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Espacios vacíos para alinear el primer día del mes al día correcto de la semana
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(40.dp))
            }

            // Items para cada día del mes
            items(daysInMonth) { day ->
                val date = firstDayOfMonth.plusDays(day.toLong())
                val dayOfWeek = date.dayOfWeek.value % 7
                val isWorkingDay = workingDays.contains(dayOfWeek)
                val isSelected = selectedDate == date
                val isPastDate = date.isBefore(today)
                val isToday = date == today
                // Determina si el día es seleccionable (no pasado y día laborable o hoy)
                val isEnabled = (!isPastDate || isToday) && isWorkingDay

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            when {
                                isSelected -> Color(0xFF859A72)     // Día seleccionado
                                !isWorkingDay -> Color(0xFFC47E7E)   // Día no laborable
                                isPastDate -> Color(0xFFD5D5D5)      // Días pasados
                                isToday -> Color(0xFF9EC8D5)          // Día actual
                                else -> Color(0xFFB2C2A4)             // Días habilitados
                            }
                        )
                        .clickable(enabled = isEnabled) {
                            if (isEnabled) onDateSelected(date) // Llama al callback si el día es válido
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val textColor = when {
                        isSelected -> Color.White
                        isPastDate -> Color.White
                        !isWorkingDay -> Color.White
                        else -> Color.White
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Número del día en el calendario
                        Text(
                            text = "${day + 1}",
                            color = textColor,
                            fontFamily = afacadFont,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}