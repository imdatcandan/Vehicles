package com.imdatcandan.vehicles.view

import com.imdatcandan.vehicles.model.VehicleList

sealed class ViewState {
    data class Success(val vehicleList: VehicleList) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
    data class Loading(val isLoading: Boolean) : ViewState()
}