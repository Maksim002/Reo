package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.common.distinctUntilChanged
import ru.ktsstudio.app_verification.presentation.object_survey.common.formDebounce
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.emptyConsumer
import ru.ktsstudio.common.utils.mvi.liveDataConsumer
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

internal class ProductSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val feature: Feature<
        ObjectSurveyFeature.Wish<ProductionSurveyDraft>,
        ObjectSurveyFeature.State<ProductionSurveyDraft>,
        ObjectSurveyFeature.News<ProductionSurveyDraft>
        >,
    private val nestedObjectHolderFeature: Feature<
        NestedObjectHolderFeature.Wish<ProductionSurveyDraft, ProducedEntityType>,
        NestedObjectHolderFeature.State<ProductionSurveyDraft>,
        NestedObjectHolderFeature.News>,
    private val binder: Binder,
    private val resourceManager: ResourceManager
) : RxViewModel() {

    private val uiEventsSubject = Rx2PublishSubject.create<ProductUiEvent>()
    private val stateMutableLiveData = MutableLiveData<ProductSurveyUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<ProductSurveyUiState>
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
        (updater as? Updater<ProductionSurveyDraft>)
            ?.let { ProductUiEvent.UpdateSurvey(it) }
            ?.let(uiEventsSubject::onNext)
    }

    fun addEntity(entity: ProducedEntityType) {
        uiEventsSubject.onNext(ProductUiEvent.AddEquipmentEntity(entity))
    }

    fun deleteEntity(id: String, entity: ProducedEntityType) {
        uiEventsSubject.onNext(ProductUiEvent.DeleteEquipmentEntity(id, entity))
    }

    fun saveSurvey() {
        uiEventsSubject.onNext(ProductUiEvent.SaveSurvey(objectId))
    }

    private fun init() {
        uiEventsSubject.onNext(ProductUiEvent.Load(objectId))
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject.formDebounce() to feature using { it.toSurveyFeatureWish() })
        binder.bind(uiEventsSubject to nestedObjectHolderFeature using { it.toNestedObjectFeatureWish() })

        binder.bind(
            feature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to liveDataConsumer(stateMutableLiveData)
                using ProductSurveyUiStateTransformer(resourceManager)
        )
        binder.bind(
            nestedObjectHolderFeature
                .distinctUntilChanged()
                .doOnNext { it.draft?.let(::syncDraftBetweenFeatures) }
                to emptyConsumer()
        )

        binder.bind(feature.news to ::consumeSurveyFeatureNews.toConsumer())
    }

    private fun consumeSurveyFeatureNews(news: ObjectSurveyFeature.News<ProductionSurveyDraft>) {
        when (news) {
            is ObjectSurveyFeature.News.DraftIsInvalid -> {
                errorMutableLiveData.postValue(
                    resourceManager.getString(R.string.survey_infrastructure_not_filled_form)
                )
            }
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

    private fun syncDraftBetweenFeatures(draft: ProductionSurveyDraft) {
        uiEventsSubject.onNext(ProductUiEvent.SetSurvey(draft))
    }
}
