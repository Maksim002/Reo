package ru.ktsstudio.core_data_verfication_api.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
interface ObjectRepository {
    fun fetchObjects(): Single<List<VerificationObject>>
    fun observeAllObjects(): Observable<List<VerificationObject>>
    fun observeSurveyProgress(objectId: String): Observable<Map<SurveySubtype, Progress>>
    fun getObjectListByIds(ids: List<String>): Single<List<VerificationObject>>
    fun getObjectWithCheckedSurvey(id: String): Maybe<VerificationObjectWithCheckedSurvey>
    fun saveObjectWithCheckedSurvey(verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey): Completable
    fun deleteObjectMedias(medias: List<Media>): Completable
    fun saveObjectMedias(medias: List<Media>): Completable
    fun uploadUnSyncedObjectMedias(): Completable
    fun uploadUnSyncedObjects(): Completable
    fun getAllReferences(): Single<List<Reference>>
}