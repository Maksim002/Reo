package ru.ktsstudio.reo.presentation.measurement.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.News
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.State
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.Wish
import ru.ktsstudio.reo.navigation.MeasurementNavigator
import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal class MeasurementDetailsViewModel @Inject constructor(
    @Id private val measurementId: Long,
    @Named("isPreviewMode") private val isPreviewMode: Boolean,
    @Named("returnTag") private val returnTag: MeasurementReturnTag?,
    private val binder: Binder,
    private val navigator: MeasurementNavigator,
    uiStateTransformer: MeasurementDetailsUiStateTransformer,
    feature: Feature<Wish, State, News>
) : RxViewModel(), Consumer<MeasurementDetailsUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<Wish>()
    private val stateMutableLiveData = MutableLiveData<MeasurementDetailsUiState>()
    private val errorMessageMutableLiveData = SingleLiveEvent<String>()

    init {
        binder.bind(uiEventsSubject to feature)
        binder.bind(feature to this using uiStateTransformer)
        binder.bind(feature.news to ::reportNews.toConsumer())
        uiEventsSubject.onNext(Wish.Load(measurementId, isPreviewMode))
    }

    val state: LiveData<MeasurementDetailsUiState>
        get() = stateMutableLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageMutableLiveData

    override fun accept(state: MeasurementDetailsUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun retry() {
        uiEventsSubject.onNext(Wish.Load(measurementId, isPreviewMode))
    }

    fun takeMeasurementAction() {
        if (isPreviewMode) {
            uiEventsSubject.onNext(Wish.CreateMeasurement)
        } else {
            uiEventsSubject.onNext(Wish.EditMeasurement)
        }
    }

    override fun onCleared() {
        super.onCleared()
        binder.clear()
    }

    private fun reportNews(news: News) {
        when (news) {
            is News.MeasurementCreateFailed -> {
                errorMessageMutableLiveData.postValue(news.throwable.localizedMessage)
            }
            is News.MeasurementCreated -> {
                returnTag?.let(navigator::measurementDetailsEditComplete)
                    ?: error("Return destination is not set!")
            }
            is News.OpenEditMeasurement -> {
                if (isPreviewMode) error("Already editing!")
                navigator.measurementDetailsEditMeasurement(
                    mnoId = news.mnoId,
                    measurementId = news.measurementId
                )
            }
        }.exhaustive
    }
}
