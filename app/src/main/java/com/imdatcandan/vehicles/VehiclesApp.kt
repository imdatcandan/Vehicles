package com.imdatcandan.vehicles

import android.app.Application
import com.imdatcandan.vehicles.di.appModule
import com.imdatcandan.vehicles.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VehiclesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VehiclesApp)
            modules(arrayListOf(appModule, networkModule))
        }
    }
}