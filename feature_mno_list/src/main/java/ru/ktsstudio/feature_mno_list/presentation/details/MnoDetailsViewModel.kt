package ru.ktsstudio.feature_mno_list.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.feature_mno_list.domain.details.MnoDetailsFeature
import ru.ktsstudio.feature_mno_list.navigation.MnoNavigator
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 01.10.2020.
 */
internal class MnoDetailsViewModel @Inject constructor(
    @Id private val mnoId: String,
    private val binder: Binder,
    uiStateTransformer: MnoUiStateTransformer,
    feature: Feature<MnoDetailsFeature.Wish, MnoDetailsFeature.State, Nothing>,
    private val navigator: MnoNavigator
) : RxViewModel(), Consumer<MnoDetailsUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<MnoDetailsFeature.Wish>()
    private val stateMutableLiveData = MutableLiveData<MnoDetailsUiState>()

    val state: LiveData<MnoDetailsUiState>
        get() = stateMutableLiveData

    init {
        binder.bind(uiEventsSubject to feature)
        binder.bind(feature to this using uiStateTransformer)
        uiEventsSubject.onNext(MnoDetailsFeature.Wish.Load(mnoId))
    }

    override fun accept(state: MnoDetailsUiState) {
        stateMutableLiveData.postValue(state)
    }

    fun retry() {
        uiEventsSubject.onNext(MnoDetailsFeature.Wish.Load(mnoId))
    }

    fun startMeasurement() {
        navigator.startMeasurement(mnoId)
    }

    override fun onCleared() {
        super.onCleared()
        binder.clear()
    }
}