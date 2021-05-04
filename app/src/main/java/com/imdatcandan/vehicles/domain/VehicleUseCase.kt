package com.imdatcandan.vehicles.domain

class VehicleUseCase(private val vehicleRepository: VehicleRepository) {

    suspend fun getVehicleList() = vehicleRepository.getVehicleList()
}
