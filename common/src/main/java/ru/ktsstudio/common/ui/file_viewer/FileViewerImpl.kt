package ru.ktsstudio.common.ui.file_viewer

import android.content.Intent
import android.net.Uri
import ru.ktsstudio.common.R
import ru.ktsstudio.common.app.ActivityProvider
import ru.ktsstudio.common.ui.resource_manager.ResourceManager

/**
 * @author Maxim Myalkin (MaxMyalkin) on 08.09.2020.
 */
internal class FileViewerImpl(
    private val resourceManager: ResourceManager,
    private val activityProvider: ActivityProvider
) : FileViewer {
    override fun viewFile(uri: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, mimeType)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent = Intent.createChooser(
            intent,
            resourceManager.getString(R.string.file_open_chooser)
        )
        activityProvider.getCurrentActivity()?.startActivity(chooserIntent)
    }
}
