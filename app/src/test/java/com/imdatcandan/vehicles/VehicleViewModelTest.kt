package com.imdatcandan.vehicles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.imdatcandan.vehicles.domain.VehicleUseCase
import com.imdatcandan.vehicles.model.VehicleList
import com.imdatcandan.vehicles.view.ViewState
import com.imdatcandan.vehicles.viewmodel.VehicleViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class VehicleViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: VehicleViewModel
    private lateinit var mockedObserver: Observer<ViewState>

    private val useCase: VehicleUseCase = mockk(relaxed = true)
    private val vehicleList: VehicleList = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = VehicleViewModel(useCase)
        mockedObserver = createViewStateObserver()
        viewModel.stateLiveData.observeForever(mockedObserver)
    }

    @Test
    fun testSuccessViewState() = runBlockingTest {
        coEvery { useCase.getVehicleList() } returns vehicleList

        viewModel.getVehicleList()

        verifyOrder {
            mockedObserver.onChanged(ViewState.Loading(true))
            mockedObserver.onChanged(ViewState.Success(vehicleList))
            mockedObserver.onChanged(ViewState.Loading(false))
        }
    }

    @Test
    fun testErrorViewState() = runBlockingTest {
        coEvery { useCase.getVehicleList() } throws ERROR

        viewModel.getVehicleList()

        verifyOrder {
            mockedObserver.onChanged(ViewState.Loading(true))
            mockedObserver.onChanged(ViewState.Error(ERROR))
            mockedObserver.onChanged(ViewState.Loading(false))
        }
    }

    private companion object {
        private val ERROR = Exception("dummy error")
    }

    private fun createViewStateObserver(): Observer<ViewState> = spyk(Observer { })

}