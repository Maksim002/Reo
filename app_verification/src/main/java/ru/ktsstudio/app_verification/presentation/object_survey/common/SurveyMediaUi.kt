package ru.ktsstudio.app_verification.presentation.object_survey.common

import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.Identifier
import ru.ktsstudio.core_data_verfication_api.data.model.Media

/**
 * @author Maxim Ovchinnikov on 11.12.2020.
 */
sealed class SurveyMediaUi {
    data class LoadingMedia(
        val media: Media
    ) : SurveyMediaUi() {
        val isLoading: Boolean
            get() {
                val mediaReady = media.remoteId != null ||
                    (media.cachedFile != null && media.gpsPoint.isEmpty().not())

                return mediaReady.not()
            }
    }

    data class AddItem(
        val identifier: Identifier
    ) : SurveyMediaUi()
}
