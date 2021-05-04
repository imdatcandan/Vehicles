package com.imdatcandan.vehicles

import com.imdatcandan.vehicles.domain.VehicleRepository
import com.imdatcandan.vehicles.domain.VehicleUseCase
import com.imdatcandan.vehicles.model.VehicleList
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class VehicleUseCaseTest {

    private lateinit var useCase: VehicleUseCase

    private val vehicleRepository: VehicleRepository = mockk(relaxed = true)
    private val vehicleList: VehicleList = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = VehicleUseCase(vehicleRepository)
    }

    @Test
    fun testGetVehicleList() = runBlockingTest {
        coEvery { vehicleRepository.getVehicleList() } returns vehicleList

        useCase.getVehicleList()

        coVerifyOrder {
            vehicleRepository.getVehicleList()
        }
    }

}