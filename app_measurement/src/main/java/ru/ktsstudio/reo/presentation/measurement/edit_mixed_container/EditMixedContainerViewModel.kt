package ru.ktsstudio.reo.presentation.measurement.edit_mixed_container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.ContainerId
import ru.ktsstudio.common.di.qualifiers.ContianerTypeId
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.common.di.qualifiers.MnoContainerId
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.News
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.Wish
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditMixedContainerViewModel @Inject constructor(
    @MeasurementId private val measurementId: Long,
    @ContainerId private val containerId: Long?,
    @MnoContainerId private val mnoContainerId: String?,
    @ContianerTypeId private val containerTypeId: String?,
    private val formFeature: Feature<FormFeature.Wish<MeasurementForm>, FormFeature.State, Nothing>,
    private val editContainerFeature: Feature<Wish, State, News>,
    private val measurementNavigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<EditMixedContainerUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<EditMixedContainerUiEvent>()
    private val stateMutableLiveData = MutableLiveData<EditMixedContainerUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()

    val state: LiveData<EditMixedContainerUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData

    init {
        setupBindings()
        initData()
    }

    override fun accept(state: EditMixedContainerUiState) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun saveData() {
        uiEventsSubject.onNext(
            EditMixedContainerUiEvent.SaveData(measurementId, containerId)
        )
    }

    fun deleteData() {
        containerId?.let(EditMixedContainerUiEvent::DeleteData)
            ?.let(uiEventsSubject::onNext)
    }

    fun onTextChanged(dataType: ContainerDataType, value: String) {
        uiEventsSubject.onNext(EditMixedContainerUiEvent.UpdateField(dataType, value))
    }

    fun switchField(dataType: ContainerDataType, hasFocus: Boolean) {
        uiEventsSubject.onNext(EditMixedContainerUiEvent.SwitchField(dataType, hasFocus))
    }

    fun retry() {
        initData()
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to editContainerFeature using { it.toEditMixedContainerFeatureWish() })
        bind(uiEventsSubject to formFeature using { it.toFormFeatureWish() })
        bind(
            combineLatest(
                editContainerFeature,
                Rx2Observable.wrap(formFeature).debounce(FORM_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            ).distinctUntilChanged()
                to this@EditMixedContainerViewModel
                using EditContainerUiStateTransformer(resources)
        )
        bind(editContainerFeature.news to ::reportNews.toConsumer())
    }

    private fun initData() {
        uiEventsSubject.onNext(EditMixedContainerUiEvent.InitData(containerId, mnoContainerId, containerTypeId))
    }

    private fun reportNews(news: News) {
        when (news) {
            is News.DataUpdateFailed -> errorMutableLiveData.postValue(news.throwable.localizedMessage)
            is News.DataUpdateCompleted -> {
                measurementNavigator.measurementContainerUpdated()
            }
            is News.FormChanged -> uiEventsSubject.onNext(EditMixedContainerUiEvent.UpdateForm(news.form))
        }.exhaustive
    }

    companion object {
        private const val FORM_DEBOUNCE_TIMEOUT = 200L
    }
}
