package com.example.fabioproyectofinal.view.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.view.components.CalendarComponent
import com.example.fabioproyectofinal.viewmodel.AvailabilityViewModel
import com.example.fabioproyectofinal.view.components.*
import java.time.LocalDate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.utils.formatHora
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.fabioproyectofinal.model.data.model.Availability
import android.app.NotificationManager
import android.widget.Toast
import androidx.compose.ui.text.style.TextAlign
import com.example.fabioproyectofinal.model.utils.formatFecha

@Composable
fun SelectProfessionalScreen(
    navController: NavHostController,
    userId: Int?,
    idDoctor: Int,
    nombreDoctor: String,
    nombreClinica: String,
    precio: String,
    especialidad: String
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val availabilityVM: AvailabilityViewModel = viewModel()
    val disponibilidad by availabilityVM.disponibilidad.collectAsState()
    var selectedAvailability by remember { mutableStateOf<Availability?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(selectedDate) {
        availabilityVM.cargarDisponibilidadPorDia(idDoctor, selectedDate)
    }

    LaunchedEffect(Unit) {
        availabilityVM.cargarDisponibilidadPorDia(idDoctor, selectedDate)
    }

    Scaffold(
        topBar = { TopBar(navController = navController) {} },
        bottomBar = { BottomBar(navController = navController, userId = userId ?: -1) },
        containerColor = Color(0xFFFFF9F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9F2))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfessionalCardHorizontal(
                    name = nombreDoctor,
                    specialty = especialidad,
                    price = precio
                )
                Spacer(modifier = Modifier.height(8.dp))
                CalendarComponent(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (disponibilidad.isNotEmpty()) {
                Text(
                    text = "Horarios disponibles:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = afacadFont,
                    color = Color(0xFF859A72),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .heightIn(max = 400.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(disponibilidad) { item ->
                        val isSelected = selectedAvailability?.id_disponibilidad == item.id_disponibilidad
                        val backgroundColor = when {
                            !item.disponible -> Color(0xFFC47E7E)
                            isSelected -> Color(0xFF859A72)
                            else -> Color(0xFFB2C2A4)
                        }

                        Button(
                            onClick = { selectedAvailability = item },
                            enabled = item.disponible,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                disabledContainerColor = Color(0xFFC47E7E),
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "${formatHora(item.fecha_inicio)} - ${formatHora(item.fecha_fin)}",
                                fontSize = 16.sp,
                                fontFamily = afacadFont,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showConfirmationDialog = true },
                    enabled = selectedAvailability != null,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedAvailability != null) Color(0xFFB2C2A4) else Color(0xFFCCCCCC)
                    )
                ) {
                    Text("Reservar", fontFamily = afacadFont, color = Color.White)
                }
            }

            // 🔔 AlertDialog de confirmación
            if (showConfirmationDialog && selectedAvailability != null) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = {
                        Text(
                            "Confirmar cita",
                            fontFamily = afacadFont,
                            color = Color(0xFFB2C2A4),
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Column {
                            Text(
                                "¿Deseas confirmar esta reserva?",
                                fontFamily = afacadFont,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ProfessionalCardHorizontal(
                                name = nombreDoctor,
                                specialty = especialidad,
                                price = precio
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                userId?.let { uid ->
                                    availabilityVM.reservarFranja(
                                        selectedAvailability!!.id_disponibilidad,
                                        uid
                                    ) { success ->
                                        if (success) {
                                            showConfirmationDialog = false
                                            val fechaTexto = formatFecha(selectedAvailability!!.fecha_inicio)
                                            val horaTexto = formatHora(selectedAvailability!!.fecha_inicio)
                                            selectedAvailability = null
                                            availabilityVM.cargarDisponibilidadPorDia(idDoctor, selectedDate)

                                            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                            val notification = NotificationCompat.Builder(context, "appointment_channel")
                                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                .setContentTitle("Cita reservada")
                                                .setStyle(
                                                    NotificationCompat.BigTextStyle().bigText(
                                                        "Has reservado tu cita con $nombreDoctor en $nombreClinica a las $horaTexto el $fechaTexto"
                                                    )
                                                )
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .build()

                                            notificationManager.notify(1, notification)
                                            Toast.makeText(context, "Cita reservada con éxito", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirmar", color = Color.White, fontFamily = afacadFont)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showConfirmationDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC47E7E)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancelar", fontFamily = afacadFont)
                        }
                    },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}