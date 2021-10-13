package ru.ktsstudio.core_data_verification_impl.data.db.store

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.VerificationDb
import ru.ktsstudio.core_data_verification_impl.data.db.dao.CheckedSurveyDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.ReferenceDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.VerificationObjectDao
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
class RegisterStore @Inject constructor(
    private val db: VerificationDb,
    private val verificationObjectDao: VerificationObjectDao,
    private val checkedSurveyDao: CheckedSurveyDao,
    private val referenceDao: ReferenceDao
) {

    fun clear(): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                checkedSurveyDao.clear()
                    .andThen(verificationObjectDao.clearObjects())
                    .blockingAwait()
            }
        }
    }

    fun saveReferences(references: List<LocalReference>): Completable {
        return referenceDao.insert(references)
    }

    fun getAllReferences(): Single<List<LocalReference>> {
        return referenceDao.getAllReferences()
    }
}