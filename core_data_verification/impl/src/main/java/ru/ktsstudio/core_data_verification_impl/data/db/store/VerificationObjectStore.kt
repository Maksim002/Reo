package ru.ktsstudio.core_data_verification_impl.data.db.store

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.db.batchedQuerySingle
import ru.ktsstudio.core_data_verification_impl.data.db.VerificationDb
import ru.ktsstudio.core_data_verification_impl.data.db.dao.CheckedSurveyDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.VerificationObjectDao
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
class VerificationObjectStore @Inject constructor(
    private val verificationObjectDao: VerificationObjectDao,
    private val checkedSurveyStore: CheckedSurveyDao,
    private val db: VerificationDb
) {
    fun replace(objects: List<LocalVerificationObject>, checkedSurveys: List<LocalCheckedSurvey>): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                checkedSurveyStore.clear()
                    .andThen(verificationObjectDao.clearObjects())
                    .andThen(verificationObjectDao.insertLocalVerificationObject(objects))
                    .andThen(
                        checkedSurveyStore.insertLocalCheckSurveys(checkedSurveys)
                    )
                    .blockingAwait()
            }
        }
    }

    fun save(verificationObject: LocalVerificationObject): Completable {
        return verificationObjectDao.insertLocalVerificationObject(listOf(verificationObject))
    }

    fun observeAll(): Observable<List<LocalVerificationObjectWithRelation>> {
        return verificationObjectDao.observeAllObjects()
    }

    fun getObjectListByIds(ids: List<String>): Single<List<LocalVerificationObjectWithRelation>> {
        return ids.batchedQuerySingle(verificationObjectDao::getByIds)
    }

    fun getObjectById(id: String): Maybe<LocalVerificationObjectWithRelation> {
        return verificationObjectDao.getById(id)
    }

    fun getPendingObjects(): Single<List<LocalVerificationObjectWithRelation>> {
        return verificationObjectDao.getNotSyncedObjects()
    }
}