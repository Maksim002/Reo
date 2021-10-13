package ru.ktsstudio.reo.presentation.measurement.edit_waste_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.ContainerId
import ru.ktsstudio.common.di.qualifiers.WasteTypeId
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.News
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.Wish
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditWasteTypeViewModel @Inject constructor(
    @ContainerId private val containerId: Long,
    @WasteTypeId private val wasteTypeId: String?,
    private val editWasteTypeFeature: Feature<Wish, State, News>,
    private val formFeature: Feature<FormFeature.Wish<MeasurementForm>, FormFeature.State, Nothing>,
    private val measurementNavigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<EditWasteTypeUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<EditWasteTypeUiEvent>()
    private val stateMutableLiveData = MutableLiveData<EditWasteTypeUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()

    val state: LiveData<EditWasteTypeUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData

    init {
        setupBindings()
        initData()
    }

    override fun accept(state: EditWasteTypeUiState) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun saveData() {
        uiEventsSubject.onNext(EditWasteTypeUiEvent.SaveData(containerId, wasteTypeId))
    }

    fun deleteData() {
        wasteTypeId?.let { EditWasteTypeUiEvent.DeleteData(containerId, wasteTypeId) }
            ?.let(uiEventsSubject::onNext)
    }

    fun onFieldChanged(dataType: ContainerDataType, value: String) {
        uiEventsSubject.onNext(EditWasteTypeUiEvent.UpdateField(dataType, value))
    }

    fun onFocusChanged(dataType: ContainerDataType, focused: Boolean) {
        uiEventsSubject.onNext(EditWasteTypeUiEvent.SwitchField(dataType, focused))
    }

    fun retry() {
        initData()
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to editWasteTypeFeature using { it.toEditWasteTypeFeatureWish() })
        bind(uiEventsSubject to formFeature using { it.toFormFeatureWish() })
        bind(
            combineLatest(
                editWasteTypeFeature,
                Rx2Observable.wrap(formFeature)
                    .debounce(FORM_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            ).distinctUntilChanged()
                to this@EditWasteTypeViewModel
                using EditWasteTypeUiStateTransformer(resources)
        )
        bind(editWasteTypeFeature.news to ::reportNews.toConsumer())
    }

    private fun initData() {
        uiEventsSubject.onNext(
            EditWasteTypeUiEvent.InitData(wasteTypeId)
        )
    }

    private fun reportNews(news: News) {
        when (news) {
            is News.DataUpdateFailed -> errorMutableLiveData.postValue(news.throwable.localizedMessage)
            is News.DataUpdateCompleted -> {
                measurementNavigator.measurementWasteTypeUpdated()
            }
            is News.FormChanged -> uiEventsSubject.onNext(EditWasteTypeUiEvent.UpdateForm(news.form))
        }.exhaustive
    }

    companion object {
        private const val FORM_DEBOUNCE_TIMEOUT = 200L
    }
}
