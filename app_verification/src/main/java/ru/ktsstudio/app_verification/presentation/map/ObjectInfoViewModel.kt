package ru.ktsstudio.app_verification.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoFeature
import ru.ktsstudio.app_verification.domain.models.Route
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.06.2020.
 */
class ObjectInfoViewModel @Inject constructor(
    @Id private val objectIds: List<String>,
    private val resources: ResourceManager,
    private val objectInfoFeature: Feature<
        ObjectInfoFeature.Wish,
        ObjectInfoFeature.State,
        ObjectInfoFeature.News
        >,
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>,
    private val binder: Binder
) : RxViewModel(), Consumer<ObjectInfoUiState> {

    private val uiEventsSubject = PublishSubject.create<ObjectInfoFeature.Wish>()

    private val stateMutableLiveData = MutableLiveData<ObjectInfoUiState>()
    private val navigateRouteMapLiveData = SingleLiveEvent<Route>()
    private val toastMessageLiveData = SingleLiveEvent<String>()
    private val gpsEnableRequestLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<ObjectInfoUiState>
        get() = stateMutableLiveData
    val navigateRouteMap: LiveData<Route>
        get() = navigateRouteMapLiveData
    val toastMessage: LiveData<String>
        get() = toastMessageLiveData
    val gpsEnableRequest: LiveData<Unit>
        get() = gpsEnableRequestLiveData

    init {
        setupBindings()
        loadData()
    }

    fun retry() {
        loadData()
    }

    fun openMapWithRoute() {
        uiEventsSubject.onNext(ObjectInfoFeature.Wish.OpenMapWithRoute)
    }

    fun checkGpsState(destination: GpsPoint) {
        uiEventsSubject.onNext(ObjectInfoFeature.Wish.CheckGpsState(destination))
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: ObjectInfoUiState) {
        stateMutableLiveData.postValue(state)
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject to objectInfoFeature)
        binder.bind(objectInfoFeature to this using ObjectUiStateTransformer(objectTypeUiMapper))
        binder.bind(objectInfoFeature.news to ::reportNews.toConsumer())
    }

    private fun loadData() {
        uiEventsSubject.onNext(ObjectInfoFeature.Wish.LoadData(objectIds))
    }

    private fun reportNews(news: ObjectInfoFeature.News) {
        when (news) {
            is ObjectInfoFeature.News.GpsAvailabilityState -> {
                when (news.gpsState) {
                    GpsState.DISABLED -> gpsEnableRequestLiveData.postValue(Unit)
                    else -> proceedGpsRequiredAction()
                }
            }
            is ObjectInfoFeature.News.NavigateToMapWithRoute -> navigateRouteMapLiveData.postValue(
                news.route
            )
            is ObjectInfoFeature.News.ShowLocationNotAvailableToast -> toastMessageLiveData.postValue(
                resources.getString(R.string.location_is_not_available)
            )
        }
    }

    private fun proceedGpsRequiredAction() {
        uiEventsSubject.onNext(ObjectInfoFeature.Wish.OpenMapWithRoute)
    }
}
