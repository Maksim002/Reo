package ru.ktsstudio.app_verification.domain.sync

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.BehaviorProcessor
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.RegisterRepository
import ru.ktsstudio.core_data_verfication_api.data.SyncRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.feature_sync_api.domain.SyncInteractor
import ru.ktsstudio.feature_sync_api.domain.model.SyncState
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 09.11.2020.
 */
class SyncInteractorImpl @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val objectRepository: ObjectRepository,
    private val syncRepository: SyncRepository,
    private val settingsRepository: SettingsRepository
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
        return objectRepository.uploadUnSyncedObjectMedias()
            .andThen(objectRepository.uploadUnSyncedObjects())
    }

    private fun fetchTransactionData(): Completable {
        return Single.zip(
            registerRepository.fetchReferences(),
            objectRepository.fetchObjects(),
            { references: List<Reference>,
                objectList: List<VerificationObject> ->
                SyncData(objectList, references)
            }
        )
            .flatMapCompletable {
                syncRepository.saveSyncData(
                    it.objectList,
                    it.references
                )
            }
    }

    data class SyncData(
        val objectList: List<VerificationObject>,
        val references: List<Reference>
    )
}
