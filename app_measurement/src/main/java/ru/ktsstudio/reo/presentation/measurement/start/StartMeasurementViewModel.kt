package ru.ktsstudio.reo.presentation.measurement.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.start.LocationUnavailableException
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 10/10/2020.
 */
class StartMeasurementViewModel @Inject constructor(
    private val starMeasurementFeature: Feature<
        StartMeasurementFeature.Wish,
        StartMeasurementFeature.State,
        StartMeasurementFeature.News
        >,
    private val navigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<StartMeasurementUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<StartMeasurementFeature.Wish>()

    private val stateMutableLiveData = MutableLiveData<StartMeasurementUiState>()
    private val errorMessageMutableLiveData = SingleLiveEvent<String>()
    private val skippMeasurementLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<StartMeasurementUiState>
        get() = stateMutableLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageMutableLiveData
    val skipMeasurement: LiveData<Unit>
        get() = skippMeasurementLiveData

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: StartMeasurementUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun resolveMeasurement(mnoId: String) {
        uiEventsSubject.onNext(StartMeasurementFeature.Wish.ResolveMeasurement(mnoId = mnoId))
    }

    fun skipMeasurement(mnoId: String, impossibilityReason: String, withLocation: Boolean) {
        uiEventsSubject.onNext(
            StartMeasurementFeature.Wish.SkipMeasurement(mnoId, impossibilityReason, withLocation)
        )
    }

    fun setMnoActiveState(isActive: Boolean) {
        uiEventsSubject.onNext(StartMeasurementFeature.Wish.SetMnoActiveState(isActive))
    }

    fun addComment(comment: String) {
        uiEventsSubject.onNext(StartMeasurementFeature.Wish.AddComment(comment))
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to starMeasurementFeature)
        bind(
            starMeasurementFeature to this@StartMeasurementViewModel using {
                StartMeasurementUiState(
                    isMnoActive = it.isMnoActive,
                    isLoading = it.isUpdating,
                    isOptionSelected = it.isOptionSelected,
                    measurementActionText = getActionTitle(it.isMnoActive),
                    comment = it.comment
                )
            }
        )
        bind(starMeasurementFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: StartMeasurementFeature.News) {
        when (news) {
            is StartMeasurementFeature.News.MeasurementResolvedToSkip -> {
                skippMeasurementLiveData.postValue(Unit)
            }

            is StartMeasurementFeature.News.MeasurementSkipped -> {
                navigator.measurementSkipped()
            }

            is StartMeasurementFeature.News.MeasurementFailed -> {
                when (news.throwable) {
                    is LocationUnavailableException -> {
                        errorMessageMutableLiveData.postValue(
                            resources.getString(R.string.location_is_not_available)
                        )
                    }
                    else -> {
                        errorMessageMutableLiveData.postValue(news.throwable.localizedMessage)
                    }
                }
            }

            is StartMeasurementFeature.News.MeasurementProceed -> {
                navigator.measurementCreateCard(news.mnoId)
            }
        }.exhaustive
    }

    private fun getActionTitle(isMnoActive: Boolean): String {
        val stringRes = if (isMnoActive) {
            R.string.measurement_create_card
        } else {
            R.string.measurement_finish
        }
        return resources.getString(stringRes)
    }
}
