package ru.ktsstudio.core_data_verification_impl.data

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.SyncRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.VerificationDb
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.store.RegisterStore
import ru.ktsstudio.core_data_verification_impl.data.db.store.VerificationObjectStore
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.11.2020.
 */
class SyncRepositoryImpl @Inject constructor(
    private val db: VerificationDb,
    private val referenceDbMapper: Mapper<Reference, LocalReference>,
    private val verificationObjectDbMapper: Mapper<VerificationObject, LocalVerificationObject>,
    private val registerStore: RegisterStore,
    private val verificationObjectStore: VerificationObjectStore,
    private val schedulerProvider: SchedulerProvider
) : SyncRepository {

    override fun saveSyncData(
        objectList: List<VerificationObject>,
        references: List<Reference>
    ): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                replaceReferences(references)
                    .andThen(replaceObjects(objectList))
                    .blockingAwait()
            }
        }
            .subscribeOn(schedulerProvider.io)
    }

    private fun replaceReferences(references: List<Reference>): Completable {
        return registerStore.clear()
            .andThen(registerStore.saveReferences(referenceDbMapper.map(references)))
    }

    private fun replaceObjects(verificationObjectList: List<VerificationObject>): Completable {
        val verificationObjects  = verificationObjectDbMapper.map(verificationObjectList)
        val checkedSurveys = verificationObjectList.map { verificationObject ->
            LocalCheckedSurvey(
                objectId = verificationObject.id,
                checkedSurvey = verificationObject.checkedSurvey
            )
        }
        return verificationObjectStore.replace(verificationObjects, checkedSurveys)
    }
}