package ru.ktsstudio.app_verification.presentation.object_survey.equipment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.common.distinctUntilChanged
import ru.ktsstudio.app_verification.presentation.object_survey.common.formDebounce
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
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal class EquipmentSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val feature: Feature<
        ObjectSurveyFeature.Wish<EquipmentSurveyDraft>,
        ObjectSurveyFeature.State<EquipmentSurveyDraft>,
        ObjectSurveyFeature.News<EquipmentSurveyDraft>
        >,
    private val nestedObjectHolderFeature: Feature<
        NestedObjectHolderFeature.Wish<EquipmentSurveyDraft, EquipmentEntity>,
        NestedObjectHolderFeature.State<EquipmentSurveyDraft>,
        NestedObjectHolderFeature.News>,
    private val referenceFeature: Feature<
        ReferenceFeature.Wish,
        ReferenceFeature.State,
        Nothing
        >,
    private val binder: Binder,
    private val resourceManager: ResourceManager
) : RxViewModel() {

    private val uiEventsSubject = Rx2PublishSubject.create<EquipmentUiEvent>()
    private val stateMutableLiveData = MutableLiveData<EquipmentSurveyUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<EquipmentSurveyUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData
    val exit: LiveData<Unit>
        get() = exitMutableLiveData

    init {
        setupBindings()
        init()
    }

    override fun onCleared() {
        super.onCleared()
        binder.clear()
    }

    fun retry() {
        init()
    }

    fun updateSurvey(updater: Updater<*>) {
        (updater as? Updater<EquipmentSurveyDraft>)
            ?.let { EquipmentUiEvent.UpdateSurvey(it) }
            ?.let(uiEventsSubject::onNext)
    }

    fun addEntity(entity: EquipmentEntity) {
        uiEventsSubject.onNext(EquipmentUiEvent.AddEquipmentEntity(entity))
    }

    fun deleteEntity(id: String, entity: EquipmentEntity) {
        uiEventsSubject.onNext(EquipmentUiEvent.DeleteEquipmentEntity(id, entity))
    }

    fun saveSurvey() {
        uiEventsSubject.onNext(EquipmentUiEvent.SaveSurvey(objectId))
    }

    private fun init() {
        uiEventsSubject.onNext(EquipmentUiEvent.Load(objectId))
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject.formDebounce() to feature using { it.toSurveyFeatureWish() })
        binder.bind(uiEventsSubject to nestedObjectHolderFeature using { it.toNestedObjectFeatureWish() })

        binder.bind(
            combineLatest(
                feature,
                referenceFeature
            )
                .distinctUntilChanged()
                .doOnNext { (state, _) ->
                    state.draft?.let(::syncDraftBetweenFeatures)
                }
                to liveDataConsumer(stateMutableLiveData)
                using EquipmentUiStateTransformer(resourceManager)
        )
        binder.bind(
            nestedObjectHolderFeature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to emptyConsumer()
        )
        binder.bind(
            uiEventsSubject to referenceFeature
                using { it.toReferenceFeatureWish() }
        )

        binder.bind(feature.news to ::consumeSurveyFeatureNews.toConsumer())
    }

    private fun consumeSurveyFeatureNews(news: ObjectSurveyFeature.News<EquipmentSurveyDraft>) {
        when (news) {
            is ObjectSurveyFeature.News.DraftIsInvalid -> errorMutableLiveData.postValue(
                resourceManager.getString(R.string.survey_infrastructure_not_filled_form)
            )
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted -> {
                errorMutableLiveData.postValue(resourceManager.getString(R.string.survey_media_update_not_complete))
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

    private fun syncDraftBetweenFeatures(draft: EquipmentSurveyDraft) {
        uiEventsSubject.onNext(EquipmentUiEvent.SetSurvey(draft))
    }
}
