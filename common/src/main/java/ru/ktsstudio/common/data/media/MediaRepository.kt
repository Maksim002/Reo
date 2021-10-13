package ru.ktsstudio.common.data.media

import android.net.Uri
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import java.io.File

interface MediaRepository {
    fun saveFileFromUriStreamToFile(uri: Uri): Single<File>

    fun createFile(fileName: String): File

    fun getUriForFile(file: File): Uri

    fun getBasePathForMediaFile(file: File): String

    fun uploadFile(file: File, contentType: String): Maybe<String>
}