package ru.ktsstudio.app_verification.presentation.object_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.app_verification.domain.object_list.ObjectListFeature
import ru.ktsstudio.app_verification.domain.object_list.ObjectSort
import ru.ktsstudio.app_verification.navigation.ObjectNavigator
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
internal class ObjectListViewModel @Inject constructor(
    private val objectListFeature: Feature<ObjectListFeature.Wish, ObjectListFeature.State, Nothing>,
    private val binder: Binder,
    private val navigator: ObjectNavigator,
    private val schedulers: SchedulerProvider,
    private val filterProvider: FilterProvider,
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>
) : RxViewModel(), Consumer<ObjectListUiState> {

    private val actionSubject = Rx2PublishSubject.create<ObjectListFeature.Wish>()
    private val searchQuerySubject = PublishSubject.create<String>()

    private val stateLiveData = MutableLiveData<ObjectListUiState>()

    val state: LiveData<ObjectListUiState>
        get() = stateLiveData

    init {
        setupBindings()
        actionSubject.onNext(ObjectListFeature.Wish.Load)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: ObjectListUiState) {
        stateLiveData.postValue(state)
    }

    fun retry() {
        actionSubject.onNext(ObjectListFeature.Wish.Load)
    }

    fun updateSearchQuery(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun selectSort(sort: ObjectSort) {
        actionSubject.onNext(ObjectListFeature.Wish.ChangeSort(sort))
    }

    fun openInspection(id: String) {
        navigator.openObjectInspection(id)
    }

    fun openFilter() {
        navigator.openObjectFilter()
    }

    private fun setupBindings() {
        binder.bind(actionSubject to objectListFeature)
        binder.bind(objectListFeature to this using ObjectListUiStateTransformer(objectTypeUiMapper))

        filterProvider.observeFilter()
            .map(ObjectListFeature.Wish::ChangeFilter)
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()

        searchQuerySubject.debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { ObjectListFeature.Wish.ChangeSearchQuery(it) }
            .observeOn(schedulers.ui)
            .subscribe(actionSubject::onNext, Timber::e)
            .store()
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}
