package com.example.fabioproyectofinal.view.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fabioproyectofinal.R
import com.example.fabioproyectofinal.model.ApiServer
import com.example.fabioproyectofinal.model.session.SessionManager
import com.example.fabioproyectofinal.viewmodel.InsuranceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.ui.Alignment


// Diálogo para seleccionar y actualizar los seguros médicos del usuario
@Composable
fun InsuranceDialog(onDismiss: () -> Unit) {
    val viewModel: InsuranceViewModel = viewModel()
    val seguros by viewModel.todosLosSeguros.collectAsState()
    val usuarioId = SessionManager.idUsuario

    // Lista de IDs de seguros seleccionados actualmente
    val selectedSeguros = remember { mutableStateListOf<Int>() }

    // Lista actual de seguros que el usuario tiene asignados
    val segurosUsuario by viewModel.segurosUsuario.collectAsState()

    // Al cambiar la lista de seguros del usuario, actualiza los seleccionados
    LaunchedEffect(segurosUsuario) {
        selectedSeguros.clear()
        selectedSeguros.addAll(segurosUsuario.map { it.id_seguro })
    }

    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))

    // Carga inicial de todos los seguros y los del usuario
    LaunchedEffect(Unit) {
        viewModel.cargarTodosLosSeguros()
        usuarioId?.let { viewModel.cargarSegurosUsuario(it) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Selecciona tus Seguros",
                fontFamily = afacadFont,
                color = Color(0xFFB2C2A4),
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            // Muestra los seguros en una grilla con checkbox por cada uno
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                horizontalArrangement = Arrangement.Center,
                userScrollEnabled = true,
            ) {
                items(seguros) { seguro ->
                    val isSelected = selectedSeguros.contains(seguro.id_seguro)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        // Checkbox que permite seleccionar/deseleccionar seguros
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) {
                                    selectedSeguros.remove(seguro.id_seguro)
                                } else {
                                    selectedSeguros.add(seguro.id_seguro)
                                }
                            }
                        )
                        Text(
                            seguro.nombre,
                            fontFamily = afacadFont,
                            color = Color(0xFF7C8B6B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            // Guarda los cambios al presionar "Guardar"
            AnimatedDialogButton(
                text = "Guardar",
                onClick = {
                    usuarioId?.let { id ->
                        val anteriores = segurosUsuario.map { it.id_seguro }.toSet()
                        val actuales = selectedSeguros.toSet()

                        // Determina qué seguros se deben agregar y eliminar
                        val aAgregar = actuales - anteriores
                        val aEliminar = anteriores - actuales

                        // Llamadas a la API para agregar nuevos seguros
                        aAgregar.forEach { idSeguro ->
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    ApiServer.apiService.agregarSeguroAUsuario(id, mapOf("id_seguro" to idSeguro))
                                } catch (e: Exception) {
                                    Log.e("Seguro", "Error al agregar seguro: ${e.message}")
                                }
                            }
                        }

                        // Llamadas a la API para eliminar seguros deseleccionados
                        aEliminar.forEach { idSeguro ->
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    ApiServer.apiService.eliminarSeguroDeUsuario(id, mapOf("id_seguro" to idSeguro))
                                } catch (e: Exception) {
                                    Log.e("Seguro", "Error al eliminar seguro: ${e.message}")
                                }
                            }
                        }
                    }
                    onDismiss()
                }
            )
        },
        dismissButton = {
            // Botón para cerrar el diálogo sin guardar cambios
            AnimatedDialogButton(
                text = "Cerrar",
                onClick = onDismiss
            )
        },
        containerColor = Color(0xFFFFF9F2),
        shape = RoundedCornerShape(12.dp)
    )
}