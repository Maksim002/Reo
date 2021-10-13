package ru.ktsstudio.core_data_measurement_impl.data.media

import android.net.Uri
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.toFormData
import ru.ktsstudio.core_data_measurement_impl.data.network.MediaApi
import java.io.File
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val fileManager: FileManager,
    private val mediaApi: MediaApi,
    private val schedulers: SchedulerProvider
) : MediaRepository {
    override fun saveFileFromUriStreamToFile(uri: Uri): Single<File> {
        return fileManager.copyFileToInternalStorage(uri)
            .subscribeOn(schedulers.io)
    }

    override fun createFile(fileName: String): File {
        return fileManager.createMediaFile(fileName)
    }

    override fun getUriForFile(file: File): Uri {
        return fileManager.getUriForFile(file)
    }

    override fun getBasePathForMediaFile(file: File): String {
        return fileManager.getMediaBasePath(file)
    }

    override fun uploadFile(file: File, contentType: String): Maybe<String> {
        return mediaApi.uploadFile(file.toFormData())
            .toMaybe()
            .map { it.id }
            .subscribeOn(schedulers.io)
    }
}