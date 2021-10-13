package ru.ktsstudio.app_verification.presentation.object_survey.tech

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.common.distinctUntilChanged
import ru.ktsstudio.app_verification.presentation.object_survey.common.formDebounce
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.emptyConsumer
import ru.ktsstudio.common.utils.mvi.liveDataConsumer
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 07.12.2020.
 */
internal class ObjectTechnicalSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val surveyFeature: Feature<
        ObjectSurveyFeature.Wish<TechnicalSurveyDraft>,
        ObjectSurveyFeature.State<TechnicalSurveyDraft>,
        ObjectSurveyFeature.News<TechnicalSurveyDraft>
        >,
    private val nestedObjectHolderFeature: Feature<
        NestedObjectHolderFeature.Wish<TechnicalSurveyDraft, TechnicalCardType>,
        NestedObjectHolderFeature.State<TechnicalSurveyDraft>,
        NestedObjectHolderFeature.News>,
    private val referenceFeature: Feature<
        ReferenceFeature.Wish,
        ReferenceFeature.State,
        Nothing
        >,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<ObjectTechnicalSurveyUiState> {

    private val actionSubject = Rx2PublishSubject.create<ObjectTechnicalSurveyUiEvent>()

    private val stateLiveData = MutableLiveData<ObjectTechnicalSurveyUiState>()
    private val errorLiveData = SingleLiveEvent<String>()

    val state: LiveData<ObjectTechnicalSurveyUiState>
        get() = stateLiveData
    val error: LiveData<String>
        get() = errorLiveData

    init {
        setupBindings()
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.LoadSurvey(objectId))
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: ObjectTechnicalSurveyUiState) {
        stateLiveData.postValue(state)
    }

    fun retry() {
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.LoadSurvey(objectId))
    }

    fun updateSurvey(updater: Updater<*>) {
        (updater as? Updater<TechnicalSurveyDraft>)
            ?.let { ObjectTechnicalSurveyUiEvent.UpdateSurvey(it) }
            ?.let(actionSubject::onNext)
    }

    fun saveSurvey() {
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.SaveSurvey(objectId))
    }

    fun addEntity(entity: TechnicalCardType) {
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.AddEquipmentEntity(entity))
    }

    fun deleteEntity(id: String, entity: TechnicalCardType) {
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.DeleteEquipmentEntity(id, entity))
    }

    private fun setupBindings() {
        binder.bind(actionSubject.formDebounce() to surveyFeature using { it.toSurveyFeatureWish() })
        binder.bind(actionSubject to nestedObjectHolderFeature using { it.toNestedObjectFeatureWish() })
        binder.bind(actionSubject to referenceFeature using { it.toReferenceFeatureWish() })
        binder.bind(
            combineLatest(
                surveyFeature,
                referenceFeature
            )
                .distinctUntilChanged()
                to liveDataConsumer(stateLiveData)
                using ObjectTechnicalSurveyUiStateTransformer(resources)
        )
        binder.bind(
            surveyFeature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to emptyConsumer()
        )
        binder.bind(
            nestedObjectHolderFeature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to emptyConsumer()
        )
        binder.bind(surveyFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: ObjectSurveyFeature.News<TechnicalSurveyDraft>) {
        when (news) {
            is ObjectSurveyFeature.News.DraftIsInvalid -> errorLiveData.postValue(
                resources.getString(R.string.survey_infrastructure_not_filled_form)
            )
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted -> {
                errorLiveData.postValue(
                    resources.getString(R.string.survey_media_update_not_complete)
                )
            }
            is ObjectSurveyFeature.News.CancelPhotoWithoutLocation,
            is ObjectSurveyFeature.News.GpsAvailabilityState,
            is ObjectSurveyFeature.News.CaptureMedia,
            is ObjectSurveyFeature.News.CheckMediaPermission,
            is ObjectSurveyFeature.News.Exit,
            is ObjectSurveyFeature.News.ShowExitDialog -> {
            }
        }.exhaustive
    }

    private fun syncDraftBetweenFeatures(draft: TechnicalSurveyDraft) {
        actionSubject.onNext(ObjectTechnicalSurveyUiEvent.SetSurvey(draft))
    }
}
