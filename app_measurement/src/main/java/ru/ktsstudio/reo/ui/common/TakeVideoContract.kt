package ru.ktsstudio.reo.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * Created by Igor Park on 27/11/2020.
 */
class TakeVideoContract(
    private val quality: Int,
    private val duration: Int
) : ActivityResultContract<Uri, Boolean>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        return Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            .apply {
                putExtra(MediaStore.EXTRA_OUTPUT, input)
                putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality)
                putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration)
            }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
