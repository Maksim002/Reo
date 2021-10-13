package ru.ktsstudio.common.data.media

import android.net.Uri
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File

interface FileManager {
    fun copyFileToInternalStorage(uri: Uri): Single<File>
    fun createMediaFile(fileName: String): File
    fun getUriForFile(file: File): Uri
    fun getMediaBasePath(file: File): String
    fun clearAll(): Completable
}
