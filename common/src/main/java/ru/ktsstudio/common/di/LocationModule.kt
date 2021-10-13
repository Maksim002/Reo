package ru.ktsstudio.common.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.data.location.LocationProvider
import ru.ktsstudio.common.data.location.LocationProviderImpl
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.location.LocationRepositoryImpl

/**
 * Created by Igor Park on 04/10/2020.
 */
@Module
abstract class LocationModule {

    @Binds
    abstract fun bindLocationProvider(impl: LocationProviderImpl): LocationProvider

    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Module
    companion object {
        @Provides
        fun provideFusedLocationProvider(context: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
    }
}