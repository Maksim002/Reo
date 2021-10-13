package ru.ktsstudio.feature_mno_list.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.feature_mno_list.domain.list.MnoListFeature
import ru.ktsstudio.feature_mno_list.navigation.MnoNavigator
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal class MnoListViewModel @Inject constructor(
    private val mnoListFeature: Feature<MnoListFeature.Wish, MnoListFeature.State, Nothing>,
    private val binder: Binder,
    private val navigator: MnoNavigator,
    private val schedulers: SchedulerProvider,
    private val filterProvider: FilterProvider
) : RxViewModel(), Consumer<MnoListUiState> {

    private val actionSubject = Rx2PublishSubject.create<MnoListFeature.Wish>()
    private val searchQuerySubject = PublishSubject.create<String>()

    private val stateLiveData = MutableLiveData<MnoListUiState>()

    val state: LiveData<MnoListUiState>
        get() = stateLiveData

    init {
        setupBindings()
        actionSubject.onNext(MnoListFeature.Wish.Load)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: MnoListUiState) {
        stateLiveData.postValue(state)
    }

    fun retry() {
        actionSubject.onNext(MnoListFeature.Wish.Load)
    }

    fun updateSearchQuery(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun openDetails(id: String) {
        navigator.openMnoDetails(id)
    }

    fun openFilter() {
        navigator.openMnoFilter()
    }

    private fun setupBindings() {
        binder.bind(actionSubject to mnoListFeature)
        binder.bind(mnoListFeature to this using MnoListUiStateTransformer())

        filterProvider.observeFilter()
            .map(MnoListFeature.Wish::ChangeFilter)
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()

        searchQuerySubject.debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { MnoListFeature.Wish.ChangeSearchQuery(it) }
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}