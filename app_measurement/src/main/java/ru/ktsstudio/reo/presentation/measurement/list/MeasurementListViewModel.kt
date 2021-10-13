package ru.ktsstudio.reo.presentation.measurement.list

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
import ru.ktsstudio.reo.di.filter.MeasurementFilter
import ru.ktsstudio.reo.domain.measurement.list.MeasurementListFeature
import ru.ktsstudio.reo.domain.measurement.list.MeasurementSort
import ru.ktsstudio.reo.navigation.MeasurementNavigator
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListViewModel @Inject constructor(
    private val measurementListFeature: Feature<MeasurementListFeature.Wish, MeasurementListFeature.State, Nothing>,
    private val binder: Binder,
    private val schedulers: SchedulerProvider,
    @MeasurementFilter private val filterProvider: FilterProvider,
    private val navigator: MeasurementNavigator
) : RxViewModel(), Consumer<MeasurementListUiState> {

    private val actionSubject = Rx2PublishSubject.create<MeasurementListFeature.Wish>()
    private val searchQuerySubject = PublishSubject.create<String>()

    private val stateLiveData = MutableLiveData<MeasurementListUiState>()

    val state: LiveData<MeasurementListUiState>
        get() = stateLiveData

    init {
        setupBindings()
        actionSubject.onNext(MeasurementListFeature.Wish.Load)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun retry() {
        actionSubject.onNext(MeasurementListFeature.Wish.Load)
    }

    fun updateSearchQuery(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun selectSort(sort: MeasurementSort) {
        actionSubject.onNext(MeasurementListFeature.Wish.ChangeSort(sort))
    }

    override fun accept(state: MeasurementListUiState) {
        stateLiveData.postValue(state)
    }

    fun openDetails(localId: Long) {
        navigator.openMeasurementDetails(localId)
    }

    private fun setupBindings() {
        binder.bind(actionSubject to measurementListFeature)
        binder.bind(measurementListFeature to this using MeasurementListUiStateTransformer())

        filterProvider.observeFilter()
            .map(MeasurementListFeature.Wish::ChangeFilter)
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()

        searchQuerySubject.debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { MeasurementListFeature.Wish.ChangeSearchQuery(it) }
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}
