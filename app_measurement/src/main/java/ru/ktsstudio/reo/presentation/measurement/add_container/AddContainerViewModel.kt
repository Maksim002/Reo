package ru.ktsstudio.reo.presentation.measurement.add_container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 10/10/2020.
 */
class AddContainerViewModel @Inject constructor(
    @Id private val mnoId: String,
    private val addContainerFeature: Feature<
        AddContainerFeature.Wish,
        AddContainerFeature.State,
        Nothing
        >,
    private val navigator: EditMeasurementNavigator,
    private val binder: Binder
) : RxViewModel(), Consumer<AddContainerUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<AddContainerFeature.Wish>()
    private val stateMutableLiveData = MutableLiveData<AddContainerUiState>()
    private val errorMessageMutableLiveData = SingleLiveEvent<String>()

    val state: LiveData<AddContainerUiState>
        get() = stateMutableLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageMutableLiveData

    init {
        setupBindings()
        initData()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: AddContainerUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun initData() {
        uiEventsSubject.onNext(AddContainerFeature.Wish.InitData(mnoId))
    }

    fun selectNewContainer() {
        uiEventsSubject.onNext(AddContainerFeature.Wish.SelectNewContainer)
    }

    fun selectMnoContainer(mnoContainerId: String) {
        uiEventsSubject.onNext(AddContainerFeature.Wish.SelectMnoContainer(mnoContainerId))
    }

    fun selectContainerType(containerTypeId: String) {
        uiEventsSubject.onNext(AddContainerFeature.Wish.SelectContainerType(containerTypeId))
    }

    fun navigateToEditContainer(measurementId: Long) {
        val currentState = stateMutableLiveData.value ?: return
        val selectedContainerType = currentState.containerTypes
            .find { it.isSelected }
            ?.containerType
        val selectedMnoContainer = currentState.mnoContainers
            .find { it.isSelected }
            ?.mnoContainer
        val isSeparate = selectedContainerType?.isSeparate.orFalse() ||
            selectedMnoContainer?.type?.isSeparate.orFalse()

        val navigationData = EditMeasurementNavigator.EditContainerNavigationData(
            measurementId = measurementId,
            mnoContainerId = selectedMnoContainer?.id,
            containerTypeId = selectedMnoContainer?.type?.id ?: selectedContainerType?.id
        )

        if (isSeparate) {
            navigator.measurementAddSeparateContainer(navigationData)
        } else {
            navigator.measurementAddMixedContainer(navigationData)
        }
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to addContainerFeature)
        bind(
            addContainerFeature to this@AddContainerViewModel
                using AddContainerUiStateTransformer()
        )
    }
}
