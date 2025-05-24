package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.model.utils.formatFecha
import com.example.fabioproyectofinal.model.utils.formatHora
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.fabioproyectofinal.R

// Tarjeta que muestra información de una cita médica pasada y permite ver el expediente completo
@Composable
fun HistoryCard(
    appointment: Appointment,         // Datos de la cita
    doctor: Doctor?,                  // Profesional asociado
    clinic: Clinic?,                  // Clínica donde se realizó
    navController: NavHostController? = null // Opcional (por si se desea navegación futura)
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 6.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )) {
                // Parte superior: imagen y datos de la clínica
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(clinic?.src)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()
                        ),
                        contentDescription = clinic?.nombre,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(end = 16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = clinic?.nombre.toString(),
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = clinic?.direccion.toString(),
                            fontSize = 14.sp,
                            fontFamily = afacadFont,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 2.dp, color = Color(0xFFCAD2C5))
                Spacer(modifier = Modifier.height(8.dp))

                // Fecha y hora de la cita
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Cita:",
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatFecha(appointment.fecha_cita),
                            fontSize = 18.sp,
                            fontFamily = afacadFont,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = formatHora(appointment.fecha_cita),
                        fontSize = 18.sp,
                        fontFamily = afacadFont,
                        color = Color(0xFFB2C2A4),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Profesional que atendió la cita
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Profesional:",
                        fontSize = 14.sp,
                        fontFamily = afacadFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = doctor?.nombre + " (" + doctor?.especialidad + ")",
                        fontSize = 12.sp,
                        fontFamily = afacadFont
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para ver expediente
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data("https://res.cloudinary.com/dr8es2ate/image/upload/icon_expediente_kb1n0q.webp")
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .build()
                            ),
                            contentDescription = "Icono expediente",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Ver expediente",
                            color = Color.White,
                            fontFamily = afacadFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Función de extensión para mostrar "No especificado" si el campo está vacío
        fun String?.orNoInfo(): String = if (this.isNullOrBlank()) "No especificado" else this

        // Diálogo que muestra el expediente de la cita
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    AnimatedDialogButton(
                        text = "Cerrar",
                        onClick = { showDialog = false }
                    )
                },
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Expediente",
                            fontFamily = afacadFont,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB2C2A4),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        item {
                            SectionTitle("Información del paciente")
                            InfoLine("Nombre completo", SessionManager.nombre.orNoInfo())
                            InfoLine("Fecha de nacimiento", SessionManager.fecha_nacimiento.orNoInfo())
                            InfoLine("Teléfono", SessionManager.telefono.orNoInfo())
                            InfoLine("Emergencia", SessionManager.telefono_emergencia.orNoInfo())

                            Spacer(Modifier.height(6.dp))
                            SectionTitle("Historial médico")
                            InfoLine("Condiciones pasadas", appointment.condiciones_pasadas.orNoInfo())
                            InfoLine("Procedimientos quirúrgicos", appointment.procedimientos_quirurgicos.orNoInfo())
                            InfoLine("Alergias", appointment.alergias.orNoInfo())
                            InfoLine("Antecedentes familiares", appointment.antecedentes_familiares.orNoInfo())

                            Spacer(Modifier.height(6.dp))
                            SectionTitle("Notas médicas")
                            InfoLine("Medicamento y dosis", appointment.medicamento_y_dosis.orNoInfo())
                            InfoLine("Nota", appointment.nota.orNoInfo())
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color(0xFFFFF9F2)
            )
        }
    }
}

// Título estilizado para secciones dentro de una pantalla
@Composable
fun SectionTitle(text: String) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = afacadFont,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB2C2A4),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

// Línea de información con etiqueta destacada y valor en texto plano
@Composable
fun InfoLine(label: String, value: String) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        // Etiqueta en negrita con viñeta
        Text(
            text = "• $label: ",
            fontFamily = afacadFont,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        // Valor asociado
        Text(
            text = value,
            fontFamily = afacadFont,
            color = Color.DarkGray
        )
    }
}
