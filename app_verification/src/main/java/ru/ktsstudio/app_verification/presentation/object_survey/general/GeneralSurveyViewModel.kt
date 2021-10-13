package ru.ktsstudio.app_verification.presentation.object_survey.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.general.models.GeneralSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.common.distinctUntilChanged
import ru.ktsstudio.app_verification.presentation.object_survey.common.formDebounce
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mvi.liveDataConsumer
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyViewModel @Inject constructor(
    @Id private val objectId: String,
    private val feature: Feature<
        GeneralSurveyFeature.Wish,
        GeneralSurveyFeature.State,
        GeneralSurveyFeature.News
        >,
    private val binder: Binder,
    private val resourceManager: ResourceManager,
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>
) : RxViewModel() {

    private val actionSubject = Rx2PublishSubject.create<GeneralSurveyFeature.Wish>()

    private val stateLiveData = MutableLiveData<GeneralSurveyUiState>()
    private val exitMutableLiveData = SingleLiveEvent<Unit>()
    private val exitDialogMutableLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<GeneralSurveyUiState>
        get() = stateLiveData
    val exit: LiveData<Unit>
        get() = exitMutableLiveData
    val exitDialog: LiveData<Unit>
        get() = exitDialogMutableLiveData

    init {
        setupBindings()
        actionSubject.onNext(GeneralSurveyFeature.Wish.Load(objectId))
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun retry() {
        actionSubject.onNext(GeneralSurveyFeature.Wish.Load(objectId))
    }

    fun updateSurvey(updater: Updater<*>) {
        (updater as? Updater<GeneralSurveyDraft>)
            ?.let(GeneralSurveyFeature.Wish::UpdateSurvey)
            ?.let(actionSubject::onNext)
    }

    fun saveSurvey() {
        actionSubject.onNext(GeneralSurveyFeature.Wish.SaveSurvey(objectId))
    }

    fun exit() {
        actionSubject.onNext(GeneralSurveyFeature.Wish.Exit)
    }

    private fun setupBindings() {
        binder.bind(actionSubject.formDebounce() to feature)
        binder.bind(
            feature.distinctUntilChanged()
    to liveDataConsumer(stateLiveData)
    using GeneralSurveyUiStateTransformer(resourceManager, objectTypeUiMapper)
        )
        binder.bind(feature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: GeneralSurveyFeature.News) {
        when (news) {
            is GeneralSurveyFeature.News.Exit -> exitMutableLiveData.postValue(Unit)
            is GeneralSurveyFeature.News.ShowExitDialog -> exitDialogMutableLiveData.postValue(Unit)
        }
    }
}
