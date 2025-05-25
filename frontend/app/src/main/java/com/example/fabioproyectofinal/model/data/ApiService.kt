package com.example.fabioproyectofinal.model

import com.example.fabioproyectofinal.model.data.model.Availability
import com.example.fabioproyectofinal.model.data.model.Clinic
import com.example.fabioproyectofinal.model.data.model.Appointment
import com.example.fabioproyectofinal.model.data.model.Doctor
import com.example.fabioproyectofinal.model.data.model.LoginResponse
import com.example.fabioproyectofinal.model.data.model.MensajeResponse
import com.example.fabioproyectofinal.model.data.model.UsuarioLoginRequest
import com.example.fabioproyectofinal.model.data.model.UsuarioRegistroRequest
import com.example.fabioproyectofinal.viewmodel.Seguro
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
    suspend fun getClinics(
        @Query("id_usuario") idUsuario: Int? = null
    ): List<Clinic>

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

    @GET("/perfil/datos-de-interes/{id_usuario}")
    suspend fun obtenerDatosDeInteres(
        @Path("id_usuario") idUsuario: Int
    ): List<Map<String, String?>>

    @retrofit2.http.PUT("/perfil/datos-de-interes/{id_usuario}")
    suspend fun actualizarDatosDeInteres(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, String?>
    ): MensajeResponse

    @GET("/perfil/{id_usuario}")
    suspend fun obtenerPerfil(
        @Path("id_usuario") idUsuario: Int
    ): List<LoginResponse>

    @retrofit2.http.HTTP(method = "DELETE", path = "/citas/eliminar", hasBody = true)
    suspend fun eliminarCita(
        @Body datos: Map<String, Int>
    ): MensajeResponse

    @POST("/usuarios/{id_usuario}/favoritos/agregar")
    suspend fun agregarAFavoritos(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, Int>
    ): MensajeResponse

    @retrofit2.http.HTTP(method = "DELETE", path = "/usuarios/{id_usuario}/favoritos/eliminar", hasBody = true)
    suspend fun eliminarDeFavoritos(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, Int>
    ): MensajeResponse

    @GET("/doctor/{id}/disponibilidad/por-dia")
    suspend fun getDisponibilidadPorDia(
        @Path("id") idDoctor: Int,
        @Query("fecha") fecha: String  // formato: yyyy-MM-dd
    ): List<Availability>

    @POST("/doctor/disponibilidad/reservar")
    suspend fun reservarFranja(@Body datos: Map<String, Int>): MensajeResponse

    @GET("/seguros")
    suspend fun getAllSeguros(): List<Seguro>

    @GET("/usuarios/{id_usuario}/seguros")
    suspend fun getSegurosUsuario(@Path("id_usuario") idUsuario: Int): List<Seguro>

    @POST("/usuarios/{id_usuario}/seguros/agregar")
    suspend fun agregarSeguroAUsuario(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, Int>
    ): MensajeResponse

    @retrofit2.http.HTTP(method = "DELETE", path = "/usuarios/{id_usuario}/seguros/eliminar", hasBody = true)
    suspend fun eliminarSeguroDeUsuario(
        @Path("id_usuario") idUsuario: Int,
        @Body datos: Map<String, Int>
    ): MensajeResponse

    @GET("/clinicas/{id_clinica}/seguros")
    suspend fun getSegurosDeClinica(@Path("id_clinica") idClinica: Int): List<Seguro>

    @GET("/medico/citas/{id_doctor}")
    suspend fun getCitasDelDoctorPorDia(
        @Path("id_doctor") idDoctor: Int,
        @Query("fecha") fecha: String
    ): List<Appointment>

    @GET("/doctor/por-usuario/{id_usuario}")
    suspend fun getIdDoctorPorUsuario(
        @Path("id_usuario") idUsuario: Int
    ): List<Map<String, Int>>

    @PUT("citas/cancelar/{id_cita}")
    suspend fun cancelarCitaConMotivo(
        @Path("id_cita") idCita: Int,
        @Body data: Map<String, String>
    )

    @PUT("/citas/editar-datos-medicos/{id_cita}")
    suspend fun actualizarDatosCita(
        @Path("id_cita") idCita: Int,
        @Body datos: Map<String, String>
    ): MensajeResponse


}


