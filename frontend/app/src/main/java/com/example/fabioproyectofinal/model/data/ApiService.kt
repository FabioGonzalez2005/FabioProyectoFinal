package com.example.fabioproyectofinal.model

import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.data.model.LoginResponse
import com.example.fabioproyectofinal.model.data.model.MensajeResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import com.example.fabioproyectofinal.model.data.model.UsuarioRegistroRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/clinicas")
    suspend fun getClinics(): List<Clinic>

    @GET("/doctores")
    suspend fun getDoctors(): List<Doctor>

    @POST("/usuario/registro")
    suspend fun registrarUsuario(@Body datos: UsuarioRegistroRequest): MensajeResponse

    @POST("/usuario/login")
    suspend fun loginUsuario(@Body datos: UsuarioLoginRequest): LoginResponse

}
