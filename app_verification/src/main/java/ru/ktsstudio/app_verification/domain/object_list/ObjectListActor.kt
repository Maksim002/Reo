package ru.ktsstudio.app_verification.domain.object_list

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.domain.filter.doIfFilterChange
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectFilterApplier

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
class ObjectListActor(
    private val objectRepository: ObjectRepository,
    private val schedulers: SchedulerProvider,
    private val filterUpdater: FilterUpdater,
    private val objectFilterApplier: VerificationObjectFilterApplier
) : Actor<ObjectListFeature.State, ObjectListFeature.Wish, ObjectListFeature.Effect> {

    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: ObjectListFeature.State,
        action: ObjectListFeature.Wish
    ): Observable<out ObjectListFeature.Effect> {
        return when (action) {
            is ObjectListFeature.Wish.Load -> observeObjectList(state.currentFilter, state.sort)
            is ObjectListFeature.Wish.ChangeSearchQuery -> {
                val newFilter = state.currentFilter.copy(searchQuery = action.searchQuery)
                doIfFilterChange(
                    newFilter,
                    state.currentFilter,
                    ObjectListFeature.Effect.UpdatingFilter(newFilter)
                ) {
                    filterUpdater.updateFilter(newFilter)
                    observeObjectList(newFilter, state.sort)
                }
            }
            is ObjectListFeature.Wish.ChangeFilter -> {
                doIfFilterChange(
                    action.newFilter,
                    state.currentFilter,
                    ObjectListFeature.Effect.UpdatingFilter(action.newFilter)
                ) {
                    observeObjectList(action.newFilter, state.sort)
                }
            }
            is ObjectListFeature.Wish.ChangeSort -> {
                observeObjectList(state.currentFilter, action.newSort)
                    .startWith(ObjectListFeature.Effect.UpdatingSort(action.newSort))
            }
        }
    }

    private fun observeObjectList(
        filter: Filter,
        sort: ObjectSort
    ): Observable<ObjectListFeature.Effect> {
        interruptSignal.onNext(Unit)

        return objectRepository.observeAllObjects()
            .map<ObjectListFeature.Effect> {
                ObjectListFeature.Effect.Success(
                    objectList = it.sortObjects(sort)
                        .filter { verificationObject ->
                            objectFilterApplier.applyFilter(verificationObject, filter)
                        }
                )
            }
            .startWithItem(ObjectListFeature.Effect.Loading)
            .onErrorReturn { ObjectListFeature.Effect.Error(it) }
            .takeUntil(interruptSignal)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun List<VerificationObject>.sortObjects(sort: ObjectSort): List<VerificationObject> {
        return when (sort) {
            ObjectSort.NAME_DESCENDING -> sortedByDescending { it.generalInformation.name }
            ObjectSort.NAME_ASCENDING -> sortedBy { it.generalInformation.name }
            ObjectSort.CREATED_AT_DESCENDING -> sortedByDescending { it.date }
            ObjectSort.CREATED_AT_ASCENDING -> sortedBy { it.date }
        }
    }
}
