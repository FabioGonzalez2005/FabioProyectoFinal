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
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getCitas(@Query("id_usuario") idUsuario: Int): List<Appointment>

    @GET("/usuarios/{id_usuario}/favoritos")
    suspend fun getClinicasFavoritas(@Path("id_usuario") idUsuario: Int): List<Clinic>

    @GET("/clinicas/por-especialidad")
    suspend fun getClinicasPorEspecialidad(@Query("especialidad") especialidad: String): List<Clinic>

    @retrofit2.http.PUT("/perfil/{id_usuario}")
    suspend fun actualizarPerfil(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, String>
    ): MensajeResponse

    @retrofit2.http.PUT("/perfil/datos-de-interes/{id_usuario}")
    suspend fun actualizarDatosDeInteres(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, String?>
    ): MensajeResponse
}
