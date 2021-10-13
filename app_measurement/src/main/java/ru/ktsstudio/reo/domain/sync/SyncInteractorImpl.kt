package ru.ktsstudio.reo.domain.sync

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.BehaviorProcessor
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.RegisterRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.Register
import ru.ktsstudio.feature_sync_api.domain.SyncInteractor
import ru.ktsstudio.feature_sync_api.domain.model.SyncState
import ru.ktsstudio.core_data_measurement_api.data.SyncRepository
import javax.inject.Inject

class SyncInteractorImpl @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val mnoRepository: MnoRepository,
    private val measurementRepository: MeasurementRepository,
    private val settingsRepository: SettingsRepository,
    private val syncRepository: SyncRepository
) : SyncInteractor {

    private val progressSubject: BehaviorProcessor<SyncState> = BehaviorProcessor.create()

    override val syncProgress: Flowable<SyncState>
        get() = progressSubject

    override fun sync(): Completable {
        return uploadData()
            .andThen(fetchTransactionData())
            .andThen(settingsRepository.refreshSettings())
            .doOnSubscribe {
                progressSubject.onNext(SyncState.STARTED)
            }
            .doOnError {
                progressSubject.onNext(SyncState.ERROR)
            }
            .doOnComplete {
                progressSubject.onNext(SyncState.FINISHED)
            }
    }

    override fun reset() {
        progressSubject.onNext(SyncState.IDLE)
    }

    private fun uploadData(): Completable {
        return measurementRepository.syncedMeasurements()
    }

    private fun fetchTransactionData(): Completable {
        return Single.zip(
            registerRepository.fetchRegisters(),
            mnoRepository.fetchMnos(),
            measurementRepository.fetchMeasurements(),
            { registers, mnoList, measurementList -> SyncData(registers, mnoList, measurementList) }
        )
            .flatMapCompletable {
                syncRepository.saveSyncData(
                    it.registers,
                    it.mnoList,
                    it.measurementList
                )
            }
    }

    data class SyncData(
        val registers: Register,
        val mnoList: List<Mno>,
        val measurementList: List<Measurement>
    )
}
