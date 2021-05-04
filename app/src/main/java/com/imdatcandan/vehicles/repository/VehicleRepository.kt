package com.imdatcandan.vehicles.repository

import com.imdatcandan.vehicles.model.VehicleList

class VehicleRepository(private val vehicleService: VehicleService) {

    suspend fun getVehicleList(): VehicleList {
        return vehicleService.getVehicleList()
    }
}
