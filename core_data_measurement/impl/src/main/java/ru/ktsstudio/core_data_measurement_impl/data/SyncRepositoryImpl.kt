package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.SyncRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.Register
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.db.ContainerStore
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementDb
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementStore
import ru.ktsstudio.core_data_measurement_impl.data.db.MnoStore
import ru.ktsstudio.core_data_measurement_impl.data.db.WasteGroupStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 18.11.2020.
 */
class SyncRepositoryImpl @Inject constructor(
    private val db: MeasurementDb,
    private val containerTypeDbMapper: Mapper<ContainerType, LocalContainerType>,
    private val wasteCategoryDbMapper: Mapper<WasteCategory, LocalWasteCategory>,
    private val wasteGroupDbMapper: Mapper<WasteGroup, LocalWasteGroup>,
    private val wasteSubgroupDbMapper: Mapper<WasteSubgroup, LocalWasteSubgroup>,
    private val measurementStatusDbMapper: Mapper<MeasurementStatus, LocalMeasurementStatus>,
    private val mnoDbMapper: Mapper<Mno, LocalMno>,
    private val containerDbMapper: Mapper<Mno, List<@JvmSuppressWildcards LocalMnoContainer>>,
    private val containerStore: ContainerStore,
    private val mnoStore: MnoStore,
    private val wasteGroupStore: WasteGroupStore,
    private val measurementStore: MeasurementStore,
    private val measurementSaveDelegate: MeasurementSaveDelegate,
    private val schedulerProvider: SchedulerProvider
) : SyncRepository {

    override fun saveSyncData(
        registers: Register,
        mnoList: List<Mno>,
        measurementList: List<Measurement>
    ): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                clearAll()
                    .andThen(saveRegisters(registers))
                    .andThen(saveMnos(mnoList))
                    .andThen(measurementSaveDelegate.saveMeasurements(measurementList))
                    .blockingAwait()
            }
        }
            .subscribeOn(schedulerProvider.io)
    }

    private fun clearAll(): Completable {
        return wasteGroupStore.clear()
            .andThen(measurementStore.clear())
            .andThen(containerStore.clear())
            .andThen(mnoStore.clear())
    }

    private fun saveRegisters(register: Register): Completable {
        return containerStore.saveRegister(
            containerTypeDbMapper.map(register.containerTypes),
            wasteCategoryDbMapper.map(register.wasteCategories),
            measurementStatusDbMapper.map(register.measurementStatuses)
        ).andThen(
            wasteGroupStore.saveRegister(
                wasteGroupDbMapper.map(register.wasteGroups),
                wasteSubgroupDbMapper.map(register.wasteSubgroups)
            )
        )
    }

    private fun saveMnos(mnos: List<Mno>): Completable {
        val localMnoList = mnoDbMapper.map(mnos)
        val localContainers = containerDbMapper.map(mnos).flatten()
        val localCategories = mnos.map {
            LocalCategory(
                id = it.source.category.id,
                name = it.source.category.name
            )
        }
            .distinctBy { it.id }
        return mnoStore.saveMnosWitRelations(
            localMnoList,
            localContainers,
            localCategories
        )
    }
}