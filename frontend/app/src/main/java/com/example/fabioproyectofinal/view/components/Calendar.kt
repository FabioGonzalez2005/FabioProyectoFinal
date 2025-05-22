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
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val today = remember { LocalDate.now() }
    val currentActualMonth = remember { YearMonth.from(today) }
    val startMonth = initialMonth ?: currentActualMonth

    val maxAllowedMonth = remember { currentActualMonth.plusMonths(16) }
    val minAllowedMonth = currentActualMonth

    var displayedYearMonth by remember { mutableStateOf(startMonth) }

    val firstDayOfMonth = displayedYearMonth.atDay(1)
    val daysInMonth = displayedYearMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7

    val canGoToPreviousMonth = displayedYearMonth.isAfter(minAllowedMonth) || displayedYearMonth == minAllowedMonth

    val canGoToNextMonth = displayedYearMonth.isBefore(maxAllowedMonth)


    Column {
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
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texto "Disponibilidad"
                Text(
                    text = "Disponibilidad:",
                    fontSize = 16.sp,
                    fontFamily = afacadFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB2C2A4)
                )

                // Row con botones e info de mes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Mes Anterior
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

                    // Nombre del mes y año
                    Text(
                        text = "${displayedYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                            .replaceFirstChar { it.uppercase() }} ${displayedYearMonth.year}",
                        color = Color(0xFFB2C2A4),
                        fontFamily = afacadFont,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Botón Mes Siguiente
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
                            modifier = Modifier.rotate(180f),
                            tint = if (canGoToNextMonth) Color(0xFFB2C2A4) else Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }




    // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(40.dp))
            }

            // Días del mes
            items(daysInMonth) { day ->
                val date = firstDayOfMonth.plusDays(day.toLong())
                val dayOfWeek = date.dayOfWeek.value % 7
                val isWorkingDay = workingDays.contains(dayOfWeek)
                val isSelected = selectedDate == date
                val isPastDate = date.isBefore(today)
                val isToday = date == today
                val isEnabled = (!isPastDate || isToday) && isWorkingDay

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            when {
                                isSelected -> Color(0xFF859A72)
                                !isWorkingDay -> Color(0xFFC47E7E)
                                isPastDate -> Color(0xFFD5D5D5)
                                isToday -> Color(0xFF9EC8D5)
                                else -> Color(0xFFB2C2A4)
                            }
                        )
                        .clickable(enabled = isEnabled) {
                            if (isEnabled) onDateSelected(date)
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