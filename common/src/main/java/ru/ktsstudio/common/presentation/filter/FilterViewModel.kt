package ru.ktsstudio.common.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.data.FilterDataFeature
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterViewModel<Field, DataType, UiDataType : UiFilterItem>(
    private val filterFeature: Feature<
        FilterFeature.Wish,
        FilterFeature.State,
        FilterFeature.News>,
    private val filterDataFeature: FilterDataFeature<DataType>,
    private val binder: Binder,
    private val fieldToKeyMapper: FilterUiFieldToKeyMapper<Field>,
    private val uiStateTransformer: (Pair<FilterFeature.State, FilterDataFeature.State<DataType>>)
    -> FilterDataUiState<Field, UiDataType>
) : RxViewModel(), Consumer<FilterDataUiState<Field, UiDataType>> {

    private val filterEventsSubject = Rx2PublishSubject.create<FilterFeature.Wish>()
    private val filterDataEventsSubject = Rx2PublishSubject.create<FilterDataFeature.Wish>()

    private val mutableState = MutableLiveData<FilterDataUiState<Field, UiDataType>>()
    private val filterAppliedLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<FilterDataUiState<Field, UiDataType>>
        get() = mutableState

    val filterApplied: LiveData<Unit>
        get() = filterAppliedLiveData

    init {
        setupBindings()
        filterDataEventsSubject.onNext(FilterDataFeature.Wish.Load)
    }

    override fun onCleared() {
        super.onCleared()
        binder.clear()
    }

    fun changeFilterValue(key: Field, value: String) {
        filterEventsSubject.onNext(
            FilterFeature.Wish.ChangeField(
                fieldKey = fieldToKeyMapper.map(key),
                fieldValue = value
            )
        )
    }

    fun applyFilter() {
        filterEventsSubject.onNext(FilterFeature.Wish.Apply)
    }

    fun clearFilter() {
        filterEventsSubject.onNext(FilterFeature.Wish.Clear)
    }

    private fun setupBindings() {
        binder.bind(filterEventsSubject to filterFeature)
        binder.bind(filterDataEventsSubject to filterDataFeature)
        binder.bind(
            combineLatest(filterFeature, filterDataFeature) to this
                using uiStateTransformer
        )
        binder.bind(filterFeature.news to ::reportFilterNews.toConsumer())
    }

    override fun accept(state: FilterDataUiState<Field, UiDataType>) {
        mutableState.postValue(state)
    }

    private fun reportFilterNews(news: FilterFeature.News) {
        when (news) {
            FilterFeature.News.FilterApplied -> filterAppliedLiveData.postValue(Unit)
        }
    }
}