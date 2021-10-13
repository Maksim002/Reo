package ru.ktsstudio.reo.presentation.measurement.morphology.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.State
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.Wish
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditMorphologyViewModel @Inject constructor(
    @MeasurementId private val measurementId: Long,
    private val editMorphologyFeature: Feature<Wish, State, Nothing>,
    private val measurementNavigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<EditMorphologyUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<Wish>()
    private val stateMutableLiveData = MutableLiveData<EditMorphologyUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()

    init {
        setupBindings()
        initData()
    }

    val state: LiveData<EditMorphologyUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData

    override fun accept(state: EditMorphologyUiState) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun openMorphologyEditing(categoryId: Long?) {
        measurementNavigator.measurementEditMorphologyItem(
            measurementId = measurementId,
            morphologyItemId = categoryId
        )
    }

    fun retry() = initData()

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to editMorphologyFeature)
        bind(
            editMorphologyFeature to this@EditMorphologyViewModel
                using EditMorphologyUiStateTransformer(resources)
        )
    }

    private fun initData() {
        uiEventsSubject.onNext(
            Wish.StartObservingMorphology(measurementId = measurementId)
        )
    }
}
