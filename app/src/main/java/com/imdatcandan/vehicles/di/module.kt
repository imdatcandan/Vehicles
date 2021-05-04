package com.imdatcandan.vehicles.di


import com.imdatcandan.vehicles.BuildConfig
import com.imdatcandan.vehicles.repository.VehicleRepository
import com.imdatcandan.vehicles.repository.VehicleService
import com.imdatcandan.vehicles.viewmodel.VehicleViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { VehicleRepository(get()) }
    viewModel { VehicleViewModel(get()) }
}

val networkModule: Module = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideWeatherService(get()) }
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val secretKeyInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .header(SECRET_KEY, BuildConfig.SECRET_KEY)
            .build()
        chain.proceed(request)
    }

    return OkHttpClient.Builder()
        .addInterceptor(secretKeyInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideWeatherService(retrofit: Retrofit): VehicleService =
    retrofit.create(VehicleService::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private const val BASE_URL = "https://api.jsonbin.io/"
private const val SECRET_KEY = "secret-key"