package ru.ktsstudio.common.data.media

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.di.qualifiers.Authority
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class FileManagerImpl @Inject constructor(
    @Authority private val fileProviderAuthority: String,
    private val context: Context
) : FileManager {
    override fun copyFileToInternalStorage(uri: Uri): Single<File> {
        return Single.create { emitter ->
            val file = getInputStreamFromUriPath(uri)
                ?.use { input ->
                    createMediaFile(getFileNameWithExtension(uri))
                        .apply {
                            outputStream().use {
                                input.copyTo(it)
                            }
                        }
                }
            file?.let(emitter::onSuccess)
                ?: emitter.tryOnError(IllegalStateException("Could not save file."))
        }
    }

    override fun createMediaFile(fileName: String): File {
        val name = fileName.let(::setCurrentTimePrefix)
        return getFolder(FOLDER_MEDIA)
            .apply { mkdirs() }
            .let { directory ->
                var file: File
                do {
                    file = File(directory, name)
                } while (file.exists())
                file
            }
    }

    override fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(context, fileProviderAuthority, file)
    }

    override fun getMediaBasePath(file: File): String {
        return "$FOLDER_MEDIA/${file.name}"
    }

    override fun clearAll(): Completable {
        return Completable.fromCallable {
            getFolder(FOLDER_MEDIA)
                .takeIf { it.exists() }
                ?.apply { deleteRecursively() }
        }
    }

    private fun getFolder(directory: String): File {
        if (isExternalStorageWritable().not()) error("External storage is not writable.")

        return File(context.getExternalFilesDir(null), directory)
    }

    private fun getInputStreamFromUriPath(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun getFileNameWithExtension(uri: Uri): String {
        val fileName = uri.lastPathSegment ?: DEFAULT_FILE_NAME
        if (fileName.hasExtension()) return fileName
        return "$fileName.${getMimeType(context, uri)}"
    }

    private fun String.hasExtension(): Boolean {
        return this.contains(FILE_EXTENSION_REGEX)
    }

    private fun getMimeType(context: Context, uri: Uri): String {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context.contentResolver.getType(uri))
                .orEmpty()
        } else {
            MimeTypeMap.getFileExtensionFromUrl(
                Uri.fromFile(File(uri.path)).toString()
            )
        }
    }

    private fun setCurrentTimePrefix(fileName: String): String {
        return "${System.currentTimeMillis()}_$fileName"
    }

    companion object {
        private const val FOLDER_MEDIA = "media"
        private const val DEFAULT_FILE_NAME = "file"
        private val FILE_EXTENSION_REGEX = "^\\.[\\w]+\$".toRegex()
    }
}
