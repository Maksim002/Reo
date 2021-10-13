package ru.ktsstudio.common.ui

import android.net.Uri

/**
 * Created by Igor Park on 01/02/2021.
 */
sealed class TakePictureResult  {
    object Success: TakePictureResult()
    data class Fail(val uri: Uri?): TakePictureResult()
}