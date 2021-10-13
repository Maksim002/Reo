package ru.ktsstudio.common.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * Created by Igor Park on 27/11/2020.
 */
class OpenSettingsContract(
    private val settings: String
) : ActivityResultContract<Unit, Int>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(settings)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}
