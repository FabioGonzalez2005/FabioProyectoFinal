package com.example.fabioproyectofinal.model

import com.example.fabioproyectofinal.model.data.model.Clinic
import retrofit2.http.GET

interface ApiService {
    @GET("/clinicas")
    suspend fun getClinics(): List<Clinic>
}
