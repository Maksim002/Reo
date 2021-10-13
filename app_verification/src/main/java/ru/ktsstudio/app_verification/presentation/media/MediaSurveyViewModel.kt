package ru.ktsstudio.app_verification.presentation.media

import android.net.Uri
import androidx.lifecycle.LiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.domain.models.CapturingMedia
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.12.2020.
 */
internal class MediaSurveyViewModel<DraftType> @Inject constructor(
    private val feature: Feature<
        ObjectSurveyFeature.Wish<DraftType>,
        ObjectSurveyFeature.State<DraftType>,
        ObjectSurveyFeature.News<DraftType>
        >,
    private val binder: Binder
) : RxViewModel() {

    private val gpsEnabledLiveData = SingleLiveEvent<Boolean>()
    private val gpsEnableRequestLiveData = SingleLiveEvent<Unit>()
    private val captureMediaMutableLiveData = SingleLiveEvent<CapturingMedia>()
    private val capturingMediaPermissionCheckLiveData = SingleLiveEvent<Unit>()

    private val uiEventsSubject = Rx2PublishSubject.create<ObjectSurveyFeature.Wish<DraftType>>()

    val gpsEnabled: LiveData<Boolean>
        get() = gpsEnabledLiveData
    val gpsEnableRequest: LiveData<Unit>
        get() = gpsEnableRequestLiveData
    val capturingMedia: LiveData<CapturingMedia>
        get() = captureMediaMutableLiveData
    val capturingMediaPermissionCheck: LiveData<Unit>
        get() = capturingMediaPermissionCheckLiveData

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun addMedia(valueConsumer: ValueConsumer<List<Media>, *>) {
        uiEventsSubject.onNext(
            ObjectSurveyFeature.Wish.AddMedia(valueConsumer as ValueConsumer<List<Media>, DraftType>)
        )
    }

    fun mediaCaptured() {
        uiEventsSubject.onNext(ObjectSurveyFeature.Wish.MediaCaptured())
    }

    fun deleteMedia(deletedMedia: Pair<ValueConsumer<List<Media>, *>, Media>) {
        uiEventsSubject.onNext(
            ObjectSurveyFeature.Wish.DeleteSurveyMedia(
                deletedMedia as Pair<ValueConsumer<List<Media>, DraftType>, Media>
            )
        )
    }

    fun cancelPhotoCapture(uri: Uri) {
        uiEventsSubject.onNext(ObjectSurveyFeature.Wish.CancelPhoto(uri))
    }

    fun checkGpsState() {
        uiEventsSubject.onNext(ObjectSurveyFeature.Wish.CheckGpsState())
    }

    fun captureMediaRequest() {
        uiEventsSubject.onNext(ObjectSurveyFeature.Wish.CaptureMediaRequest())
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject to feature)
        binder.bind(feature.news to ::consumeSurveyFeatureNews.toConsumer())
    }

    private fun consumeSurveyFeatureNews(news: ObjectSurveyFeature.News<DraftType>) {
        when (news) {
            is ObjectSurveyFeature.News.GpsAvailabilityState -> {
                when (news.gpsState) {
                    GpsState.ENABLED -> gpsEnabledLiveData.postValue(true)
                    GpsState.REJECTED -> { gpsEnabledLiveData.postValue(false) }
                    GpsState.DISABLED -> gpsEnableRequestLiveData.postValue(Unit)
                }
            }
            is ObjectSurveyFeature.News.CancelPhotoWithoutLocation -> {
                gpsEnabledLiveData.postValue(false)
                uiEventsSubject.onNext(ObjectSurveyFeature.Wish.CancelPhoto(news.uri))
            }
            is ObjectSurveyFeature.News.CaptureMedia -> {
                captureMediaMutableLiveData.postValue(news.capturingMedia)
            }
            is ObjectSurveyFeature.News.CheckMediaPermission -> {
                capturingMediaPermissionCheckLiveData.postValue(Unit)
            }
            is ObjectSurveyFeature.News.Exit,
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted,
            is ObjectSurveyFeature.News.ShowExitDialog,
            is ObjectSurveyFeature.News.DraftIsInvalid -> {
            }
        }.exhaustive
    }
}
