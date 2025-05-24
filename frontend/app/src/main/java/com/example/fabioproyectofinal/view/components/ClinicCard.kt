package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.navigation.AppScreens
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.viewmodel.InsuranceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Tarjeta de clínica
@Composable
fun ClinicaCard(
    clinic: Clinic,
    navController: NavHostController? = null,
    userId: Int?,
    inFavourites: Boolean,
    isClickable: Boolean,
    mostrarIconoVacio: Boolean = true,
    botonFavoritoActivo: Boolean = true,
    mostrarBotonSeguros: Boolean = true,
    paddingActivo: Boolean = true,
    mostrarFavoritos: Boolean = true,
    mostrarCompatibilidad: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    var estaEnFavoritos by remember { mutableStateOf(inFavourites) }

    val viewModel: InsuranceViewModel = viewModel()
    val segurosUsuario by viewModel.segurosUsuario.collectAsState()
    val segurosClinicaMap by viewModel.segurosClinicaMap.collectAsState()
    val segurosClinica = segurosClinicaMap[clinic.id_clinica] ?: emptyList()
    var showInsuranceDialog by remember { mutableStateOf(false) }

    val usuarioCargado by viewModel.segurosUsuarioCargados.collectAsState()
    val segurosClinicaCargadosMap by viewModel.segurosClinicaCargados.collectAsState()
    val clinicaCargada = segurosClinicaCargadosMap[clinic.id_clinica] ?: false


    LaunchedEffect(userId, clinic.id_clinica) {
        userId?.let { viewModel.cargarSegurosUsuario(it) }
        viewModel.cargarSegurosClinica(clinic.id_clinica)
    }

    LaunchedEffect(inFavourites) {
        estaEnFavoritos = inFavourites
    }

    if (!mostrarCompatibilidad || (usuarioCargado && clinicaCargada)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (paddingActivo) Modifier.padding(vertical = 8.dp) else Modifier)
                .then(
                    if (isClickable) Modifier.clickable {
                        onClick?.invoke() ?: navController?.navigate(
                            AppScreens.ClinicDetailScreen.createRoute(
                                userId ?: -1,
                                clinic.id_clinica
                            )
                        )
                    } else Modifier
                ),
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
                Image(
                    painter = rememberAsyncImagePainter(clinic.src),
                    contentDescription = clinic.nombre,
                    modifier = Modifier
                        .size(110.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text(
                        clinic.nombre,
                        fontFamily = afacadFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        clinic.direccion,
                        fontFamily = afacadFont,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        userId?.let { uid ->
                            // Icono de favorito
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                                if (mostrarFavoritos && (estaEnFavoritos || mostrarIconoVacio)) {
                                    val painter = if (estaEnFavoritos)
                                        rememberAsyncImagePainter("https://res.cloudinary.com/dr8es2ate/image/upload/icon_favourite_lxpak3.webp")
                                    else
                                        rememberAsyncImagePainter("https://res.cloudinary.com/dr8es2ate/image/upload/favourite_unselected_hq502n.webp")

                                    Image(
                                        painter = painter,
                                        contentDescription = "Favorito",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .then(
                                                if (botonFavoritoActivo) Modifier.clickable {
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        try {
                                                            val api = ApiServer.apiService
                                                            if (estaEnFavoritos) {
                                                                api.eliminarDeFavoritos(
                                                                    userId!!,
                                                                    mapOf("id_clinica" to clinic.id_clinica)
                                                                )
                                                            } else {
                                                                api.agregarAFavoritos(
                                                                    userId,
                                                                    mapOf("id_clinica" to clinic.id_clinica)
                                                                )
                                                            }
                                                            estaEnFavoritos = !estaEnFavoritos
                                                        } catch (e: Exception) {
                                                            println("Error modificando favoritos: ${e.message}")
                                                        }
                                                    }
                                                } else Modifier
                                            )
                                    )
                                }
                            }


                            userId?.let { uid ->
                                Box(modifier = Modifier.weight(4f), contentAlignment = Alignment.CenterEnd) {
                                    if (mostrarCompatibilidad && usuarioCargado && clinicaCargada && segurosUsuario.isNotEmpty() && segurosClinica.isNotEmpty()) {
                                        val idsUsuario = segurosUsuario.map { it.id_seguro }.toSet()
                                        val idsClinica = segurosClinica.map { it.id_seguro }.toSet()
                                        val idsCoincidentes = idsUsuario.intersect(idsClinica)

                                        val nombresCoincidentes = segurosUsuario
                                            .filter { it.id_seguro in idsCoincidentes }
                                            .map { it.nombre }

                                        val textoCompatibilidad =
                                            if (nombresCoincidentes.isNotEmpty()) {
                                                "Compatible con: ${nombresCoincidentes.joinToString(", ")}"
                                            } else {
                                                "No tienes seguros compatibles"
                                            }

                                        Text(
                                            text = textoCompatibilidad,
                                            fontFamily = afacadFont,
                                            fontSize = 12.sp,
                                            color = Color(0xFF7C8B6B)
                                        )
                                    }
                                }
                            }

                            // Botón de seguros
                            if (mostrarBotonSeguros) {
                                Button(
                                    onClick = {
                                        showInsuranceDialog = true
                                        viewModel.cargarSegurosClinica(clinic.id_clinica)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFB2C2A4
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = "Seguros aceptados",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showInsuranceDialog) {
            val segurosClinicaMap by viewModel.segurosClinicaMap.collectAsState()
            val seguros = segurosClinicaMap[clinic.id_clinica] ?: emptyList()


            AlertDialog(
                onDismissRequest = { showInsuranceDialog = false },
                title = {
                    Text(
                        "Seguros aceptados",
                        color = Color(0xFFB2C2A4),
                        fontFamily = afacadFont,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column {
                        if (seguros.isEmpty()) {
                            Text(
                                "Esta clínica no tiene seguros registrados.",
                                fontFamily = afacadFont,
                                color = Color.Gray
                            )
                        } else {
                            seguros.forEach {
                                Text("• ${it.nombre}", fontFamily = afacadFont, color = Color.Black)
                            }
                        }
                    }
                },
                confirmButton = {
                    AnimatedDialogButton(
                        text = "Cerrar",
                        onClick = { showInsuranceDialog = false }
                    )
                },
                containerColor = Color(0xFFFFF9F2),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}