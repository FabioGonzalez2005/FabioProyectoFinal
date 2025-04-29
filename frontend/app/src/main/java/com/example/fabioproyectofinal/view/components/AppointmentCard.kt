package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.Appointment

@Composable
fun AppointmentCard(appointment: Appointment, navController: NavHostController? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = appointment.src),
                        contentDescription = appointment.name,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(end = 16.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = appointment.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = appointment.address,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(thickness = 2.dp, color = Color(0xFFCAD2C5))

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Cita:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.date,
                            fontSize = 18.sp,
                            color = Color(0xFFB2C2A4),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "13:00",
                        fontSize = 18.sp,
                        color = Color(0xFFB2C2A4),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* Acción de consulta */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Consulta")
                    }
                    Button(
                        onClick = { /* Acción de cancelar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2C2A4)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}
