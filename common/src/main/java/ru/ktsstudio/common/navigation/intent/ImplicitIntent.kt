package ru.ktsstudio.common.navigation.intent

import android.content.Intent
import androidx.core.net.toUri

/**
 * @author Maxim Ovchinnikov on 21.10.2020.
 */
sealed class ImplicitIntent {

    abstract fun getIntent(): Intent

    data class SendEmail(
        private val emailAddress: String
    ) : ImplicitIntent() {
        override fun getIntent(): Intent {
            return Intent(Intent.ACTION_SENDTO)
                .apply {
                    data = "mailto:$emailAddress".toUri()
                }
        }
    }

    data class CallPhone(
        private val phoneNumber: String
    ) : ImplicitIntent() {
        override fun getIntent(): Intent {
            return Intent(Intent.ACTION_DIAL)
                .apply {
                    data = "tel:$phoneNumber".toUri()
                }
        }
    }
}