package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.MnoStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.network.MnoNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMno
import javax.inject.Inject

class MnoRepositoryImpl @Inject constructor(
    private val mnoStore: MnoStore,
    private val mnoNetworkApi: MnoNetworkApi,
    private val schedulerProvider: SchedulerProvider,
    private val mnoMapper: Mapper<LocalMnoWithRelations, Mno>,
    private val mnoContainerMapper: Mapper<LocalMnoContainerWithRelations, MnoContainer>,
    private val mnoApiMapper: Mapper<RemoteMno, Mno>
) : MnoRepository {

    override fun fetchMnos(): Single<List<Mno>> {
        return mnoNetworkApi.getMnoList()
            .map(mnoApiMapper::map)
            .subscribeOn(schedulerProvider.io)
    }

    override fun observeMnoList(): Observable<List<Mno>> {
        return mnoStore.observeAllMno()
            .map(mnoMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io)
    }

    override fun getMnoListByIds(ids: List<String>): Single<List<Mno>> {
        return mnoStore.getMnoListByIds(ids)
            .map { mnoMapper.map(it) }
            .subscribeOn(schedulerProvider.io)
    }

    override fun observeMnoById(mnoId: String): Observable<Mno> {
        return mnoStore.observeMnoById(mnoId)
            .map(mnoMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io)
    }

    override fun observeMnoListByIds(mnoIds: List<String>): Observable<List<Mno>> {
        return mnoStore.observeMnoListByIds(mnoIds)
            .map(mnoMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io)
    }

    override fun getMnoContainersByMnoId(mnoId: String): Single<List<MnoContainer>> {
        return mnoStore.getMnoContainersByMnoId(mnoId)
            .map(mnoContainerMapper::map)
            .subscribeOn(schedulerProvider.io)
    }

    override fun getMnoContainerById(containerId: String): Maybe<MnoContainer> {
        return mnoStore.getMnoContainerById(containerId)
            .map(mnoContainerMapper::map)
            .subscribeOn(schedulerProvider.io)
    }

    override fun getMnoById(id: String): Maybe<Mno> {
        return mnoStore.getMnoById(id)
            .map(mnoMapper::map)
    }
}