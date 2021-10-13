package ru.ktsstudio.reo.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.reo.domain.map.mno_info.MnoInfoFeature
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.06.2020.
 */
class MnoInfoViewModel @Inject constructor(
    @Id private val objectIds: List<String>,
    private val mnoInfoFeature: Feature<MnoInfoFeature.Wish, MnoInfoFeature.State, Nothing>,
    private val binder: Binder
) : RxViewModel(), Consumer<MnoInfoUiState> {

    private val uiEventsSubject = PublishSubject.create<MnoInfoFeature.Wish>()

    private val stateMutableLiveData = MutableLiveData<MnoInfoUiState>()

    init {
        setupBindings()
        loadData()
    }

    val state: LiveData<MnoInfoUiState>
        get() = stateMutableLiveData

    fun retry() {
        loadData()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: MnoInfoUiState) {
        stateMutableLiveData.postValue(state)
    }

    private fun setupBindings() {
        binder.bind(uiEventsSubject to mnoInfoFeature)
        binder.bind(mnoInfoFeature to this using MnoUiStateTransformer())
    }

    private fun loadData() {
        uiEventsSubject.onNext(MnoInfoFeature.Wish.LoadData(objectIds))
    }
}
