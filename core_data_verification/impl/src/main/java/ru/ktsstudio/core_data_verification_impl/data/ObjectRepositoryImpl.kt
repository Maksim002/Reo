package ru.ktsstudio.core_data_verification_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.toFormData
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation
import ru.ktsstudio.core_data_verification_impl.data.db.store.CheckedSurveyStore
import ru.ktsstudio.core_data_verification_impl.data.db.store.MediaStore
import ru.ktsstudio.core_data_verification_impl.data.db.store.RegisterStore
import ru.ktsstudio.core_data_verification_impl.data.db.store.VerificationObjectStore
import ru.ktsstudio.core_data_verification_impl.data.network.FileNetworkApi
import ru.ktsstudio.core_data_verification_impl.data.network.RegisterNetworkApi
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.ObjectSendNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.utilities.extensions.requireNotNull
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
class ObjectRepositoryImpl @Inject constructor(
    private val registerApi: RegisterNetworkApi,
    private val fileApi: FileNetworkApi,
    private val verificationObjectStore: VerificationObjectStore,
    private val checkedSurveyStore: CheckedSurveyStore,
    private val registerStore: RegisterStore,
    private val mediaStore: MediaStore,
    private val progressMapper: Mapper<LocalCheckedSurvey, Map<SurveySubtype, Progress>>,
    private val objectRemoteMapper: Mapper<RemoteVerificationObject, VerificationObject?>,
    private val objectToSendMapper: ObjectSendNetworkMapper,
    private val objectMapper: Mapper<VerificationObject, LocalVerificationObject>,
    private val objectLocalMapper: Mapper<LocalVerificationObjectWithRelation, VerificationObject>,
    private val referenceMapper: Mapper<LocalReference, Reference>,
    private val mediaDbMapper: Mapper<Media, LocalMedia>,
    private val schedulers: SchedulerProvider
) : ObjectRepository {

    override fun fetchObjects(): Single<List<VerificationObject>> {
        return registerApi.getVerificationObjectList()
            .map { it.mapNotNull { objectRemoteMapper.map(it) } }
            .subscribeOn(schedulers.io)
    }

    override fun observeAllObjects(): Observable<List<VerificationObject>> {
        return verificationObjectStore.observeAll()
            .map(objectLocalMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeSurveyProgress(objectId: String): Observable<Map<SurveySubtype, Progress>> {
        return checkedSurveyStore.observeCheckedSurveyByObjectId(objectId)
            .map(progressMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun getObjectListByIds(ids: List<String>): Single<List<VerificationObject>> {
        return verificationObjectStore.getObjectListByIds(ids)
            .map(objectLocalMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getObjectWithCheckedSurvey(id: String): Maybe<VerificationObjectWithCheckedSurvey> {
        return Maybe.zip(
            verificationObjectStore.getObjectById(id).map(objectLocalMapper::map),
            getCheckedSurveyByObjectId(id),
            BiFunction { verificationObject, checkedSurvey ->
                VerificationObjectWithCheckedSurvey(
                    verificationObject = verificationObject,
                    checkedSurvey = checkedSurvey.checkedSurvey
                )
            }
        )
            .subscribeOn(schedulers.io)
    }

    override fun saveObjectWithCheckedSurvey(verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey): Completable {

        fun saveVerificationObject(verificationObject: VerificationObject): Completable {
            val localVerificationObject = objectMapper.map(verificationObject).copy(
                state = LocalModelState.PENDING
            )
            return verificationObjectStore.save(localVerificationObject)
        }

        fun saveCheckedSurvey(
            objectId: String,
            checkedSurvey: CheckedSurvey
        ): Completable {
            val localCheckedSurvey = LocalCheckedSurvey(objectId, checkedSurvey)
            return checkedSurveyStore.save(listOf(localCheckedSurvey))
        }

        return saveVerificationObject(verificationObjectWithCheckedSurvey.verificationObject)
            .andThen(
                saveCheckedSurvey(
                    objectId = verificationObjectWithCheckedSurvey.verificationObject.id,
                    checkedSurvey = verificationObjectWithCheckedSurvey.checkedSurvey
                )
            )
    }

    override fun deleteObjectMedias(medias: List<Media>): Completable {
        val localMediaFilePaths = medias.map(mediaDbMapper::map)
            .mapNotNull { it.localFilePath }
        return mediaStore.deleteMediasByLocalPath(localMediaFilePaths)
            .subscribeOn(schedulers.io)
    }

    override fun saveObjectMedias(medias: List<Media>): Completable {
        val localMedias = medias.map(mediaDbMapper::map)
        return mediaStore.saveMedias(localMedias)
    }

    override fun uploadUnSyncedObjectMedias(): Completable {
        return mediaStore.getAllUnSyncedMedias()
            .flatMapCompletable { medias ->
                Completable.merge(medias.map(::uploadMedia))
            }
            .subscribeOn(schedulers.io)
    }

    override fun uploadUnSyncedObjects(): Completable {

        fun uploadObject(remoteObject: RemoteVerificationObject): Completable {
            fun endSurveyIfNeeded(): Completable {
                return observeSurveyProgress(remoteObject.id)
                    .firstOrError()
                    .map {
                        it.values.all {
                            it.isDone()
                        }
                    }
                    .flatMapCompletable { isDone ->
                        if (isDone.not()) {
                            Completable.complete()
                        } else {
                            registerApi.endSurvey(remoteObject.id)
                        }
                    }
            }

            return registerApi.uploadObject(remoteObject.id, remoteObject)
                .andThen(endSurveyIfNeeded())
                .subscribeOn(schedulers.io)
        }

        return Single.zip(
            verificationObjectStore.getPendingObjects(),
            getAllReferences(),
            mediaStore.getAllMedia().map { it.filter { it.localFilePath != null } },
            Function3 { objects: List<LocalVerificationObjectWithRelation>,
                references: List<Reference>,
                medias: List<LocalMedia> ->
                objectToSendMapper.map(objects, references, medias.associateBy { it.localFilePath.requireNotNull() })
            }
        )
            .flatMapCompletable { verificationObjects ->
                Completable.merge(verificationObjects.map(::uploadObject))
            }
            .subscribeOn(schedulers.io)
    }

    override fun getAllReferences(): Single<List<Reference>> {
        return registerStore.getAllReferences()
            .map(referenceMapper::map)
            .subscribeOn(schedulers.io)
    }

    private fun getCheckedSurveyByObjectId(objectId: String): Maybe<LocalCheckedSurvey> {
        return checkedSurveyStore.getCheckedSurveyByObjectId(objectId)
            .switchIfEmpty(
                verificationObjectStore.getObjectById(objectId)
                    .toSingle()
                    .map {
                        LocalCheckedSurvey(
                            objectId = objectId,
                            checkedSurvey = CheckedSurvey.getEmptyCheckedSurveyByType(it.verificationObject.type)
                        )
                    }
            )
            .toMaybe()
    }

    private fun uploadMedia(localMedia: LocalMedia): Completable {
        val file = localMedia.localFilePath
            ?.let(::File)
            ?: return Completable.complete()
        if (file.exists().not()) {
            Timber.e("${file.path} not exist")
            return Completable.complete()
        }

        return fileApi.uploadFile(file.toFormData())
            .flatMapCompletable { remoteMedia ->
                mediaStore.updateMedia(
                    mediaLocalId = localMedia.id,
                    remoteId = remoteMedia.id
                )
            }
    }
}