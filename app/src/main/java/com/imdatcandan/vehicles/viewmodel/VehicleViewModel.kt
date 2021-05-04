package com.imdatcandan.vehicles.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdatcandan.vehicles.repository.VehicleRepository
import com.imdatcandan.vehicles.view.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehicleViewModel(private val vehicleRepository: VehicleRepository) : ViewModel() {

    val stateLiveData: MutableLiveData<ViewState> = MutableLiveData(ViewState.Loading(true))

    init {
        getVehicleList()
    }

    fun getVehicleList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val vehicleList = vehicleRepository.getVehicleList()
                emitNewState(ViewState.Success(vehicleList))
            } catch (exception: Exception) {
                emitNewState(ViewState.Error(exception))
            } finally {
                emitNewState(ViewState.Loading(false))
            }
        }
    }

    private fun emitNewState(newState: ViewState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = newState
        }
    }
}