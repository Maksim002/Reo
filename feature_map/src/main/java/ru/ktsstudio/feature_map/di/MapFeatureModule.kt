package ru.ktsstudio.feature_map.di

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.feature_map.data.CameraPositionStore
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.feature_map.domain.MapActor
import ru.ktsstudio.feature_map.domain.MapFeature
import ru.ktsstudio.feature_map.domain.MapNewsPublisher
import ru.ktsstudio.feature_map.domain.MapReducer
import ru.ktsstudio.feature_map.domain.models.CameraPosition
import ru.ktsstudio.feature_map.domain.models.Center

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal object MapFeatureModule {
    private const val DEFAULT_ZOOM = 15f

    @Provides
    @JvmStatic
    fun provideInitialState(): MapFeature.State {
        return MapFeature.State(
            selectedObjectIds = emptyList(),
            isCameraPositionInitialized = false,
            isAutoCentringAllowed = false,
            isMyLocationLoading = false,
            isMyLocationEnabled = false,
            cameraPosition = CameraPosition(
                topLeft = GpsPoint.EMPTY,
                bottomRight = GpsPoint.EMPTY,
                center = Center(
                    gpsPoint = GpsPoint.EMPTY,
                    zoom = DEFAULT_ZOOM
                )
            ),
            currentFilter = Filter.EMPTY
        )
    }

    @Provides
    @JvmStatic
    fun provideMapFeature(
        initialState: MapFeature.State,
        locationRepository: LocationRepository,
        mapRepository: MapRepository,
        cameraPositionStore: CameraPositionStore,
        schedulers: SchedulerProvider,
        filterUpdater: FilterUpdater
    ): Feature<
        MapFeature.Wish,
        MapFeature.State,
        MapFeature.News
        > {
        return MapFeature(
            initialState = initialState,
            actor = MapActor(
                locationRepository,
                mapRepository,
                cameraPositionStore,
                schedulers,
                filterUpdater
            ),
            reducer = MapReducer(),
            newsPublisher = MapNewsPublisher()
        )
    }


    @Provides
    @JvmStatic
    fun provideBinder(): Binder {
        return Binder()
    }
}
