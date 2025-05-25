package com.example.fabioproyectofinal.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.fabioproyectofinal.R
import androidx.compose.ui.text.font.FontWeight
import com.example.fabioproyectofinal.model.data.model.Appointment

@Composable
fun EditRecordDialog(cita: Appointment, onDismiss: () -> Unit) {
    val afacadFont = FontFamily(Font(R.font.afacadfont, FontWeight.Normal))
    val context = LocalContext.current

    var showPatientInfoDialog by remember { mutableStateOf(false) }
    var showMedicalHistoryDialog by remember { mutableStateOf(false) }
    var showMedicalNotesDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Expediente del paciente",
                color = Color(0xFFB2C2A4),
                fontFamily = afacadFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedDialogButton(
                    text = "Información del paciente",
                    onClick = { showPatientInfoDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                AnimatedDialogButton(
                    text = "Historial médico",
                    onClick = { showMedicalHistoryDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                AnimatedDialogButton(
                    text = "Notas médicas",
                    onClick = { showMedicalNotesDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            // Botón obligatorio, aunque sea invisible o no haga nada
            Spacer(modifier = Modifier.height(0.dp))
        },
        dismissButton = {
            AnimatedDialogButton(
                text = "Cerrar",
                onClick = { onDismiss() },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        },
        containerColor = Color(0xFFFFF9F2),
        shape = RoundedCornerShape(12.dp)
    )

    if (showPatientInfoDialog) {
        PatientInformationDialog(cita = cita, onDismiss = { showPatientInfoDialog = false })
    }
    if (showMedicalHistoryDialog) {
        MedicalHistoryDialog(cita = cita, onDismiss = { showMedicalHistoryDialog = false })
    }
    if (showMedicalNotesDialog) {
        MedicalNotesDialog(cita = cita, onDismiss = { showMedicalNotesDialog = false })
    }
}