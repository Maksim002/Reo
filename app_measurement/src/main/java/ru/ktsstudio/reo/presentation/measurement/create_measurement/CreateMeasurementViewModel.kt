package ru.ktsstudio.reo.presentation.measurement.create_measurement

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.create_measurement.CapturingMedia
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.presentation.ToastMessage
import ru.ktsstudio.common.presentation.toToastMessage
import ru.ktsstudio.reo.domain.measurement.create_measurement.IncorrectGainException
import ru.ktsstudio.reo.domain.measurement.start.LocationUnavailableException
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag
import ru.ktsstudio.reo.ui.measurement.create.CreateMeasurementFragment
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 10/10/2020.
 */
class CreateMeasurementViewModel @Inject constructor(
    @Id private val mnoId: String,
    @MeasurementId private val measurementId: Long?,
    private val createMeasurementFeature: Feature<
        CreateMeasurementFeature.Wish,
        CreateMeasurementFeature.State,
        CreateMeasurementFeature.News
        >,
    private val navigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<CreateMeasurementUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<CreateMeasurementFeature.Wish>()

    private val stateMutableLiveData = MutableLiveData<CreateMeasurementUiState>()
    private val captureMediaMutableLiveData = SingleLiveEvent<CapturingMedia>()
    private val errorMessageMutableLiveData = SingleLiveEvent<ToastMessage>()
    private val gpsEnabledLiveData = SingleLiveEvent<Boolean>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()
    private val actualMeasurementId: Long?
        get() = stateMutableLiveData.value
            ?.measurementId
    private val isEditMode: Boolean = measurementId != null

    val state: LiveData<CreateMeasurementUiState>
        get() = stateMutableLiveData
    val errorMessage: LiveData<ToastMessage>
        get() = errorMessageMutableLiveData
    val capturingMedia: LiveData<CapturingMedia>
        get() = captureMediaMutableLiveData
    val gpsEnabled: LiveData<Boolean>
        get() = gpsEnabledLiveData
    val exit: LiveData<Unit>
        get() = exitMutableLiveData

    init {
        setupBindings()
        initMeasurement()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: CreateMeasurementUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun initMeasurement() {
        uiEventsSubject.onNext(
            CreateMeasurementFeature.Wish.StartObservingMeasurement(
                mnoId = mnoId,
                measurementLocalId = measurementId
            )
        )
    }

    fun captureMediaRequest(category: MeasurementMediaCategory, withLocation: Boolean) {
        uiEventsSubject.onNext(
            CreateMeasurementFeature.Wish.CaptureMediaRequest(
                category,
                withLocation
            )
        )
    }

    fun gpsEnableRequestRejected() {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.GpsEnableRejected)
    }

    fun mediaCaptured() {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.MediaCaptured)
    }

    fun deleteMedia(mediaId: Long) {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.DeleteMedia(mediaId))
    }

    fun onCommentChanged(comment: String) {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.CommentChanged(comment))
    }

    fun addEntity(qualifier: MeasurementEntity) {
        when (qualifier) {
            MeasurementEntity.CONTAINER -> {
                actualMeasurementId?.let { measurementId ->
                    navigator.measurementAddContainer(mnoId, measurementId)
                }
            }
            MeasurementEntity.MORPHOLOGY -> {
                actualMeasurementId?.let(navigator::measurementEditMorphology)
            }
        }.exhaustive
    }

    fun openContainer(containerId: Long, isSeparate: Boolean) {
        if (isSeparate) {
            navigator.measurementEditSeparateContainer(actualMeasurementId!!, containerId)
        } else {
            navigator.measurementEditMixedContainer(actualMeasurementId!!, containerId)
        }
    }

    fun checkGpsState() {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.CheckGpsState)
    }

    fun exitWithSaveCheck() {
        uiEventsSubject.onNext(
            CreateMeasurementFeature.Wish.ExitWithSaveCheck
        )
    }

    fun clearData() {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.ClearData)
    }

    fun setMeasurementLocation() {
        uiEventsSubject.onNext(CreateMeasurementFeature.Wish.SetLocation)
    }

    private fun navigateToPreview() {
        actualMeasurementId?.let { id ->
            navigator.measurementPreviewCard(
                measurementId = id,
                returnTag = if (isEditMode) {
                    MeasurementReturnTag.MEASUREMENT_DETAILS
                } else {
                    MeasurementReturnTag.MNO_DETAILS
                }
            )
        }
            ?: error("Unreachable condition: measurement ID is not set!")
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to createMeasurementFeature)
        bind(
            createMeasurementFeature to this@CreateMeasurementViewModel
                using CreateMeasurementUiStateTransformer(resources)
        )
        bind(createMeasurementFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: CreateMeasurementFeature.News) {
        when (news) {
            is CreateMeasurementFeature.News.AddMediaFailed -> {
                when (news.throwable) {
                    is LocationUnavailableException -> {
                        errorMessageMutableLiveData.postValue(
                            resources.getString(R.string.location_is_not_available).toToastMessage()
                        )
                    }
                    else -> {
                        errorMessageMutableLiveData.postValue(news.throwable.localizedMessage.toToastMessage())
                    }
                }
            }

            is CreateMeasurementFeature.News.CaptureMedia -> {
                captureMediaMutableLiveData.postValue(news.capturingMedia)
            }
            is CreateMeasurementFeature.News.DeleteMediaFailed -> {
                errorMessageMutableLiveData.postValue(news.throwable.localizedMessage.toToastMessage())
            }
            is CreateMeasurementFeature.News.SetLocationCompleted -> {
                navigateToPreview()
            }
            is CreateMeasurementFeature.News.SetLocationFailed -> {
                val errorMessage = if (news.throwable is IncorrectGainException) {
                    resources.getString(R.string.measurement_incorrect_gain_values)
                        .toToastMessage(Toast.LENGTH_LONG)
                } else {
                    news.throwable.localizedMessage.toToastMessage()
                }
                errorMessageMutableLiveData.postValue(errorMessage)
            }
            is CreateMeasurementFeature.News.ConfirmDataClear -> {
                navigator.measurementConfirmDataClear(CreateMeasurementFragment.DIALOG_ACTION_KEY)
            }
            is CreateMeasurementFeature.News.ExitMeasurement -> {
                exitMutableLiveData.postValue(Unit)
            }
            is CreateMeasurementFeature.News.GpsAvailabilityState -> {
                when (news.gpsState) {
                    GpsState.ENABLED -> gpsEnabledLiveData.postValue(true)
                    GpsState.REJECTED -> gpsEnabledLiveData.postValue(false)
                    GpsState.DISABLED -> {
                        navigator.measurementGpsEnableRequest(
                            message = resources.getString(R.string.geo_data_request_dialog_message)
                        )
                    }
                }
            }
        }.exhaustive
    }
}
