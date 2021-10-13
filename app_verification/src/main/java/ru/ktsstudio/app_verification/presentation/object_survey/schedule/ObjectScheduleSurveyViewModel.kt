package ru.ktsstudio.app_verification.presentation.object_survey.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.common.formDebounce
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.liveDataConsumer
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
internal class ObjectScheduleSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val feature: Feature<
        ObjectSurveyFeature.Wish<ScheduleSurveyDraft>,
        ObjectSurveyFeature.State<ScheduleSurveyDraft>,
        ObjectSurveyFeature.News<ScheduleSurveyDraft>
        >,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel() {

    private val actionSubject =
        Rx2PublishSubject.create<ObjectSurveyFeature.Wish<ScheduleSurveyDraft>>()

    private val stateLiveData = MutableLiveData<ObjectScheduleSurveyUiState>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()
    private val errorMutableLiveData = SingleLiveEvent<String>()

    val state: LiveData<ObjectScheduleSurveyUiState>
        get() = stateLiveData
    val exit: LiveData<Unit>
        get() = exitMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData

    init {
        setupBindings()
        init()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun retry() {
        init()
    }

    fun updateSurvey(updater: Updater<*>) {
        (updater as? Updater<ScheduleSurveyDraft>)
            ?.let { ObjectSurveyFeature.Wish.UpdateSurvey(it) }
            ?.let(actionSubject::onNext)
    }

    fun saveSurvey() {
        actionSubject.onNext(ObjectSurveyFeature.Wish.SaveSurvey(objectId))
    }

    private fun init() {
        actionSubject.onNext(ObjectSurveyFeature.Wish.Load(objectId))
    }

    private fun setupBindings() {
        binder.bind(actionSubject.formDebounce() to feature)
        binder.bind(
            Rx2Observable.wrap(feature).distinctUntilChanged()
    to liveDataConsumer(stateLiveData) using ObjectScheduleSurveyUiStateTransformer(resources)
        )
        binder.bind(feature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: ObjectSurveyFeature.News<ScheduleSurveyDraft>) {
        when (news) {
            is ObjectSurveyFeature.News.DraftIsInvalid -> errorMutableLiveData.postValue(
                resources.getString(R.string.survey_infrastructure_not_filled_form)
            )
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted -> {
                errorMutableLiveData.postValue(resources.getString(R.string.survey_media_update_not_complete))
            }
            is ObjectSurveyFeature.News.CancelPhotoWithoutLocation,
            is ObjectSurveyFeature.News.GpsAvailabilityState,
            is ObjectSurveyFeature.News.CaptureMedia,
            is ObjectSurveyFeature.News.CheckMediaPermission,
            is ObjectSurveyFeature.News.Exit,
            is ObjectSurveyFeature.News.ShowExitDialog -> {}
        }.exhaustive
    }
}
