package com.imdatcandan.vehicles.domain

import com.imdatcandan.vehicles.model.VehicleList

class VehicleRepository(private val vehicleService: VehicleService) {

    suspend fun getVehicleList(): VehicleList {
        return vehicleService.getVehicleList()
    }
}
