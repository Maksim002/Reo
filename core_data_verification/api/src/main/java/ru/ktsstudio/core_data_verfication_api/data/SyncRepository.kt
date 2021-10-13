package ru.ktsstudio.core_data_verfication_api.data

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.11.2020.
 */
interface SyncRepository {
    fun saveSyncData(
        objectList: List<VerificationObject>,
        references: List<Reference>
    ): Completable
}