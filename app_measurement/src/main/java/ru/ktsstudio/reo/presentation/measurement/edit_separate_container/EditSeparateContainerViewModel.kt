package ru.ktsstudio.reo.presentation.measurement.edit_separate_container

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
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditSeparateContainerViewModel @Inject constructor(
    @MeasurementId private val measurementId: Long,
    @ContainerId private val containerId: Long?,
    @MnoContainerId private val mnoContainerId: String?,
    @ContianerTypeId private val containerTypeId: String?,
    private val editContainerFeature: Feature<
        EditSeparateContainerFeature.Wish,
        EditSeparateContainerFeature.State,
        EditSeparateContainerFeature.News>,
    private val measurementNavigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<EditSeparateContainerUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<EditSeparateContainerFeature.Wish>()
    private val stateMutableLiveData = MutableLiveData<EditSeparateContainerUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()
    private val dataClearedLiveData = SingleLiveEvent<Unit>()

    init {
        setupBindings()
        initData()
    }

    val state: LiveData<EditSeparateContainerUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData
    val dataCleared: LiveData<Unit>
        get() = dataClearedLiveData

    override fun accept(state: EditSeparateContainerUiState) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun saveData() {
        uiEventsSubject.onNext(EditSeparateContainerFeature.Wish.SaveData)
    }

    fun deleteData() {
        uiEventsSubject.onNext(EditSeparateContainerFeature.Wish.DeleteData)
    }

    fun clearData() {
        uiEventsSubject.onNext(EditSeparateContainerFeature.Wish.ClearUnfinishedData)
    }

    fun onTextChanged(dataType: ContainerDataType, value: String) {
        uiEventsSubject.onNext(EditSeparateContainerFeature.Wish.UpdateField(dataType, value))
    }

    fun openWasteTypeEditing(wasteTypeId: String?) {
        state.value
            ?.containerId
            ?.let { containerId ->
                measurementNavigator.measurementEditWasteType(containerId, wasteTypeId)
            }
    }

    fun retry() {
        uiEventsSubject.onNext(
            EditSeparateContainerFeature.Wish.InitData(
                measurementId = measurementId,
                containerId = containerId,
                mnoContainerId = mnoContainerId,
                containerTypeId = containerTypeId
            )
        )
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to editContainerFeature)
        bind(
            editContainerFeature to this@EditSeparateContainerViewModel
                using EditSeparateContainerUiStateTransformer(resources)
        )
        bind(editContainerFeature.news to ::reportNews.toConsumer())
    }

    private fun initData() {
        uiEventsSubject.onNext(
            EditSeparateContainerFeature.Wish.InitData(
                measurementId = measurementId,
                containerId = containerId,
                mnoContainerId = mnoContainerId,
                containerTypeId = containerTypeId
            )
        )
    }

    private fun reportNews(news: EditSeparateContainerFeature.News) {
        when (news) {
            is EditSeparateContainerFeature.News.DataUpdateFailed -> errorMutableLiveData.postValue(
                news.throwable.localizedMessage
            )
            is EditSeparateContainerFeature.News.DataCleared -> dataClearedLiveData.postValue(Unit)
            is EditSeparateContainerFeature.News.DataUpdated -> measurementNavigator.measurementContainerUpdated()
        }
    }
}
