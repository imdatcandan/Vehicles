package com.imdatcandan.vehicles.repository

import com.imdatcandan.vehicles.model.VehicleList
import retrofit2.http.GET
import retrofit2.http.Headers

interface VehicleService {

    @Headers("Content-Type: application/json")
    @GET("b/5fa8ff8dbd01877eecdb898f")
    suspend fun getVehicleList(): VehicleList
}