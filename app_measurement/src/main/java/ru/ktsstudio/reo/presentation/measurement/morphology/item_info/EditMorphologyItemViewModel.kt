package ru.ktsstudio.reo.presentation.measurement.morphology.item_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.News
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.State
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.Wish
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditMorphologyItemViewModel @Inject constructor(
    @MeasurementId private val measurementId: Long,
    @Named("morphologyId") private val morphologyId: Long?,
    private val formFeature: Feature<FormFeature.Wish<MeasurementForm>, FormFeature.State, Nothing>,
    private val editMorphologyItemFeature: Feature<Wish, State, News>,
    private val measurementNavigator: EditMeasurementNavigator,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<EditMorphologyItemUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<EditMorphologyItemUiEvent>()
    private val stateMutableLiveData = MutableLiveData<EditMorphologyItemUiState>()
    private val errorMutableLiveData = SingleLiveEvent<String>()

    val state: LiveData<EditMorphologyItemUiState>
        get() = stateMutableLiveData
    val error: LiveData<String>
        get() = errorMutableLiveData

    init {
        setupBindings()
        initData()
    }

    override fun accept(state: EditMorphologyItemUiState) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun saveData() {
        uiEventsSubject.onNext(EditMorphologyItemUiEvent.SaveData(measurementId, morphologyId))
    }

    fun deleteData() {
        morphologyId?.let(EditMorphologyItemUiEvent::DeleteData)
            ?.let(uiEventsSubject::onNext)
    }

    fun onFieldChanged(dataType: ContainerDataType, value: String) {
        uiEventsSubject.onNext(EditMorphologyItemUiEvent.UpdateField(dataType, value))
    }

    fun onFocusChanged(dataType: ContainerDataType, focused: Boolean) {
        uiEventsSubject.onNext(EditMorphologyItemUiEvent.SwitchField(dataType, focused))
    }

    fun retry() {
        initData()
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to editMorphologyItemFeature using { it.toEditMorphologyItemFeatureWish() })
        bind(uiEventsSubject to formFeature using { it.toFormFeatureWish() })
        bind(
            combineLatest(
                editMorphologyItemFeature,
                Rx2Observable.wrap(formFeature).debounce(FORM_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            ).distinctUntilChanged()
                to this@EditMorphologyItemViewModel
                using EditMorphologyItemUiStateTransformer(resources)
        )
        bind(editMorphologyItemFeature.news to ::reportNews.toConsumer())
    }

    private fun initData() {
        uiEventsSubject.onNext(EditMorphologyItemUiEvent.InitData(morphologyId))
    }

    private fun reportNews(news: News) {
        when (news) {
            is News.DataUpdateFailed -> errorMutableLiveData.postValue(news.throwable.localizedMessage)
            is News.DataUpdateCompleted -> {
                measurementNavigator.measurementEditMorphologyItemCompleted()
            }
            is News.FormChanged -> uiEventsSubject.onNext(EditMorphologyItemUiEvent.UpdateForm(news.form))
        }.exhaustive
    }

    companion object {
        private const val FORM_DEBOUNCE_TIMEOUT = 200L
    }
}
