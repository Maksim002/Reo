package ru.ktsstudio.common.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * Created by Igor Park on 01/02/2021.
 */
class TakePicture : ActivityResultContract<Uri?, TakePictureResult>() {
    private var uri: Uri? = null

    override fun parseResult(resultCode: Int, intent: Intent?): TakePictureResult {
        return if (resultCode == Activity.RESULT_OK) {
            TakePictureResult.Success
        } else {
            TakePictureResult.Fail(uri)
        }
    }

    override fun createIntent(context: Context, input: Uri?): Intent {
        this.uri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }
}