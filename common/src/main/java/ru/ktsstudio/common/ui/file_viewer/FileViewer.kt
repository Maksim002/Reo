package ru.ktsstudio.common.ui.file_viewer

import android.net.Uri

/**
 * Created by Igor Park on 10/07/2020.
 */
interface FileViewer {
    fun viewFile(uri: Uri, mimeType: String)
}
