package com.example.fabioproyectofinal.model

import com.example.fabioproyectofinal.model.data.model.Availability
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.data.model.LoginResponse
import com.example.fabioproyectofinal.model.data.model.MensajeResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import com.example.fabioproyectofinal.model.data.model.UsuarioRegistroRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/doctores")
    suspend fun getDoctors(): List<Doctor>

    @POST("/usuario/registro")
    suspend fun registrarUsuario(@Body datos: UsuarioRegistroRequest): MensajeResponse

    @POST("/usuario/login")
    suspend fun loginUsuario(@Body datos: UsuarioLoginRequest): LoginResponse

    @GET("/clinicas")
    suspend fun getClinics(): List<Clinic>

    @GET("/doctor/{id}/disponibilidad/completa")
    suspend fun getDisponibilidadDoctor(@retrofit2.http.Path("id") idDoctor: Int): List<Availability>

    @GET("/citas")
    suspend fun getCitas(): List<Appointment>
}
