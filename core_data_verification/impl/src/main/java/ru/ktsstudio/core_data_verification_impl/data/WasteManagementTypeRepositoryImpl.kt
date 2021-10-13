package ru.ktsstudio.core_data_verification_impl.data

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.WasteManagementTypeRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verification_impl.data.db.store.VerificationObjectStore
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
class WasteManagementTypeRepositoryImpl @Inject constructor(
    private val verificationObjectStore: VerificationObjectStore,
    private val schedulers: SchedulerProvider
) : WasteManagementTypeRepository {

    override fun observeAllWasteManagementTypes(): Observable<List<VerificationObjectType>> {
        return verificationObjectStore.observeAll()
            .map { localVerificationObjectsWithRelation ->
                localVerificationObjectsWithRelation.map {
                    it.verificationObject.type
                }.distinct()
            }
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }
}