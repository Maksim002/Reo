package ru.ktsstudio.feature_map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.feature_map.R
import ru.ktsstudio.feature_map.domain.MapFeature
import ru.ktsstudio.feature_map.domain.models.CameraPosition
import ru.ktsstudio.feature_map.domain.models.Center
import ru.ktsstudio.feature_map.domain.models.Destination
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.domain.models.MapObject
import ru.ktsstudio.feature_map.ui.MapUiState
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class MapViewModel @Inject constructor(
    private val mapFeature: Feature<MapFeature.Wish, MapFeature.State, MapFeature.News>,
    private val resources: ResourceManager,
    private val schedulers: SchedulerProvider,
    private val binder: Binder,
    private val filterProvider: FilterProvider,
    private val navigator: RecycleMapNavigator
) : RxViewModel(), Consumer<MapUiState> {

    private val mapActionSubject = Rx2PublishSubject.create<MapFeature.Wish>()
    private val cameraPositionSubject = PublishSubject.create<CameraPosition>()
    private val searchQuerySubject = PublishSubject.create<String>()

    private val stateMutableLiveData = MutableLiveData<MapUiState>()
    private val animateCameraLiveData = SingleLiveEvent<Destination>()
    private val recycleObjectsLiveData = SingleLiveEvent<List<RecycleObject>>()
    private val navigateLiveData = SingleLiveEvent<List<String>>()
    private val errorMessageLiveData = SingleLiveEvent<String>()
    private val gpsEnableRequestLiveData = SingleLiveEvent<Unit>()

    val mapState: LiveData<MapUiState>
        get() = stateMutableLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageLiveData
    val animateCamera: LiveData<Destination>
        get() = animateCameraLiveData
    val recycleObjects: LiveData<List<RecycleObject>>
        get() = recycleObjectsLiveData
    val navigate: LiveData<List<String>>
        get() = navigateLiveData
    val gpsEnableRequest: LiveData<Unit>
        get() = gpsEnableRequestLiveData

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: MapUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun updateSearchQuery(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun openFilter() {
        navigator.mapFeatureToObjectFilter()
    }

    private fun setupBindings() = with(binder) {
        bind(mapActionSubject to mapFeature)
        bind(mapFeature to this@MapViewModel using MapStateTransformer())
        bind(mapFeature.news to ::reportNews.toConsumer())

        cameraPositionSubject
            .map(MapFeature.Wish::UpdateRegionForPosition)
            .observeOn(schedulers.ui)
            .subscribe(mapActionSubject::onNext, Timber::e)
            .store()

        searchQuerySubject.debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { MapFeature.Wish.ChangeSearchQuery(it) }
            .observeOn(schedulers.ui)
            .subscribe(mapActionSubject::onNext, Timber::e)
            .store()

        filterProvider.observeFilter()
            .map(MapFeature.Wish::ChangeFilter)
            .observeOn(schedulers.ui)
            .subscribe(mapActionSubject::onNext, Timber::e)
            .store()
    }

    fun mapReady() {
        val hasObjectsOnMap: Boolean = recycleObjectsLiveData.value
            ?.isNotEmpty()
            .orFalse()
        if (hasObjectsOnMap) {
            recycleObjectsLiveData.reemit()
        } else {
            mapActionSubject.onNext(MapFeature.Wish.InitCameraPosition)
            mapActionSubject.onNext(MapFeature.Wish.ObserveLocationAvailability)
        }
    }

    fun checkGpsState() {
        mapActionSubject.onNext(MapFeature.Wish.CheckGpsState)
    }

    fun showMyLocation() {
        mapActionSubject.onNext(MapFeature.Wish.ShowMyLocation)
    }

    fun setVisibleRegion(topLeft: GpsPoint, bottomRight: GpsPoint, center: GpsPoint, zoom: Float) {
        val newFrame = CameraPosition(
            topLeft = topLeft,
            bottomRight = bottomRight,
            center = Center(
                gpsPoint = center,
                zoom = zoom
            )
        )
        cameraPositionSubject.onNext(newFrame)
    }

    fun selectObject(mapObject: MapObject) {
        showObjectsInfo(listOf(mapObject.id))
        centerCamera(mapObject.latitude, mapObject.longitude)
    }

    fun selectObjects(mapObjects: List<MapObject>, clusterPosition: GpsPoint) {
        showObjectsInfo(mapObjects.map { it.id })
        centerCamera(clusterPosition.lat, clusterPosition.lng)
    }

    fun showSelectedObjects() {
        mapActionSubject.onNext(MapFeature.Wish.ShowSelectedObjectInfo)
    }

    private fun reportNews(news: MapFeature.News) {
        when (news) {
            is MapFeature.News.GpsAvailabilityState -> {
                when (news.gpsState) {
                    GpsState.DISABLED -> gpsEnableRequestLiveData.postValue(Unit)
                    else -> proceedGpsRequiredAction()
                }
            }
            is MapFeature.News.MyLocationFailed -> {
                errorMessageLiveData.postValue(resources.getString(R.string.error_my_location))
            }
            is MapFeature.News.LoadFailed -> {
                errorMessageLiveData.postValue(resources.getString(R.string.error_unknown))
            }

            is MapFeature.News.ShowLocation -> {
                animateCameraLiveData.postValue(
                    Destination(
                        center = Center(
                            gpsPoint = news.point,
                            zoom = news.zoom
                        ),
                        showObjectInfo = news.showObjectInfo
                    )
                )
            }

            is MapFeature.News.ShowRecycleObjects -> {
                recycleObjectsLiveData.postValue(news.objects)
            }

            is MapFeature.News.ShowObjectInfo -> {
                navigateLiveData.postValue(news.objectIds)
            }
        }
    }

    private fun proceedGpsRequiredAction() {
        mapActionSubject.onNext(MapFeature.Wish.ShowMyLocation)
    }

    private fun showObjectsInfo(objectIds: List<String>) {
        mapActionSubject.onNext(MapFeature.Wish.SelectObjects(objectIds))
    }

    fun clearObjectInfo() {
        mapActionSubject.onNext(MapFeature.Wish.ClearObjects)
    }

    private fun centerCamera(latitude: Double, longitude: Double) {
        mapActionSubject.onNext(
            MapFeature.Wish.ShowLocation(
                GpsPoint(latitude, longitude)
            )
        )
    }

    private fun <T> MutableLiveData<T>.reemit() {
        this.value = this.value
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}