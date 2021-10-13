package ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
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
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal class SecondaryResourcesSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val feature: Feature<
        ObjectSurveyFeature.Wish<SecondaryResourcesSurveyDraft>,
        ObjectSurveyFeature.State<SecondaryResourcesSurveyDraft>,
        ObjectSurveyFeature.News<SecondaryResourcesSurveyDraft>>,
    private val nestedObjectHolderFeature: Feature<
        NestedObjectHolderFeature.Wish<SecondaryResourcesSurveyDraft, SecondaryResourceEntity>,
        NestedObjectHolderFeature.State<SecondaryResourcesSurveyDraft>,
        NestedObjectHolderFeature.News>,
    private val referenceFeature: Feature<
        ReferenceFeature.Wish,
        ReferenceFeature.State,
        Nothing>,
    private val binder: Binder,
    private val resourceManager: ResourceManager
) : RxViewModel() {

    private val uiEventsSubject = Rx2PublishSubject.create<SecondaryResourcesUiEvent>()
    private val stateMutableLiveData = MutableLiveData<SecondaryResourcesSurveyUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<SecondaryResourcesSurveyUiState>
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
        (updater as? Updater<SecondaryResourcesSurveyDraft>)
            ?.let { SecondaryResourcesUiEvent.UpdateSurvey(it) }
            ?.let(uiEventsSubject::onNext)
    }

    fun addEntity(entity: SecondaryResourceEntity) {
        uiEventsSubject.onNext(SecondaryResourcesUiEvent.AddSecondaryResourceEntity(entity))
    }

    fun deleteEntity(id: String, entity: SecondaryResourceEntity) {
        uiEventsSubject.onNext(SecondaryResourcesUiEvent.DeleteSecondaryResourceEntity(id, entity))
    }

    fun saveSurvey() {
        uiEventsSubject.onNext(SecondaryResourcesUiEvent.SaveSurvey(objectId))
    }

    private fun init() {
        uiEventsSubject.onNext(SecondaryResourcesUiEvent.Load(objectId))
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject.formDebounce() to feature using { it.toSurveyFeatureWish() })
        binder.bind(uiEventsSubject to nestedObjectHolderFeature using { it.toNestedObjectFeatureWish() })
        binder.bind(uiEventsSubject to referenceFeature using { it.toReferenceFeatureWish() })

        binder.bind(
            combineLatest(
                feature
                    .distinctUntilChanged()
                    .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) },
                Rx2Observable.wrap(referenceFeature)
                    .map {
                        val secondaryReferences = it.references
                            .groupBy { it.type }[ReferenceType.SECONDARY_RESOURCES]
                            .orEmpty()
                        it.copy(references = secondaryReferences)
                    }
            ).distinctUntilChanged() to liveDataConsumer(stateMutableLiveData)
                using SecondaryResourcesUiStateTransformer(resourceManager)
        )
        binder.bind(
            nestedObjectHolderFeature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to emptyConsumer()
        )

        binder.bind(feature.news to ::consumeSurveyFeatureNews.toConsumer())
    }

    private fun consumeSurveyFeatureNews(news: ObjectSurveyFeature.News<SecondaryResourcesSurveyDraft>) {
        when (news) {
            is ObjectSurveyFeature.News.DraftIsInvalid -> {
                errorMutableLiveData.postValue(
                    resourceManager.getString(R.string.survey_secondary_resources_incorrect_form)
                )
            }
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted -> {
                errorMutableLiveData.postValue(resourceManager.getString(R.string.survey_media_update_not_complete))
            }
            is ObjectSurveyFeature.News.CancelPhotoWithoutLocation,
            is ObjectSurveyFeature.News.GpsAvailabilityState -> {
            }
            is ObjectSurveyFeature.News.CaptureMedia -> {
            }
            is ObjectSurveyFeature.News.CheckMediaPermission,
            is ObjectSurveyFeature.News.Exit,
            is ObjectSurveyFeature.News.ShowExitDialog -> {
            }
        }.exhaustive
    }

    private fun syncDraftBetweenFeatures(draft: SecondaryResourcesSurveyDraft) {
        uiEventsSubject.onNext(SecondaryResourcesUiEvent.SetSurvey(draft))
    }
}
