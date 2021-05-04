package com.imdatcandan.vehicles.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.imdatcandan.vehicles.R
import com.imdatcandan.vehicles.databinding.ActivityVehiclesBinding
import com.imdatcandan.vehicles.model.Current
import com.imdatcandan.vehicles.model.State
import com.imdatcandan.vehicles.viewmodel.VehicleViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class VehiclesActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var vehicleMap: GoogleMap
    private val viewModel: VehicleViewModel by viewModel()
    private lateinit var binding: ActivityVehiclesBinding

    private var allList: List<Current> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.stateLiveData.observe(this) {
            when (it) {
                is ViewState.Success -> {
                    allList = it.vehicleList.data.current
                    showRequestedList(State.ALL)
                }
                is ViewState.Error -> showErrorDialog(it.exception)
                is ViewState.Loading -> binding.progressBar.showLoading(it.isLoading)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showRequestedList(state: State) {
        vehicleMap.clear()
        val filteredList = allList.filter { it.state == state.name }
        if (state == State.ALL) {
            setRequestedListOnMap(allList)
        } else {
            setRequestedListOnMap(filteredList)
        }
    }

    private fun setRequestedListOnMap(requestedList: List<Current>) {
        requestedList.forEach { current ->
            val vehiclePosition = LatLng(current.latitude, current.longitude)
            vehicleMap.addMarker(
                MarkerOptions().position(vehiclePosition).title(current.state).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        State.valueOf(current.state).iconColor
                    )
                )
            )
            vehicleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    vehiclePosition,
                    ZOOM_LEVEL
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all_list -> showRequestedList(State.ALL)
            R.id.active_list -> showRequestedList(State.ACTIVE)
            R.id.damaged_list -> showRequestedList(State.DAMAGED)
            R.id.lost_list -> showRequestedList(State.LOST)
            R.id.maintenance_list -> showRequestedList(State.MAINTENANCE)
            R.id.low_battery_list -> showRequestedList(State.LOW_BATTERY)
            R.id.gps_issue_list -> showRequestedList(State.GPS_ISSUE)
            R.id.last_search_list -> showRequestedList(State.LAST_SEARCH)
            R.id.missing_list -> showRequestedList(State.MISSING)
            R.id.out_of_order_list -> showRequestedList(State.OUT_OF_ORDER)
            else -> showRequestedList(State.ALL)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        vehicleMap = googleMap
    }

    private fun showErrorDialog(exception: Throwable) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_error_title)
            .setMessage(getString(R.string.dialog_error_message, exception.localizedMessage))
            .setPositiveButton(R.string.dialog_error_button) { _, _ ->
                viewModel.getVehicleList()
            }.create()
            .show()
    }

    private companion object {
        private const val ZOOM_LEVEL = 12f

    }
}