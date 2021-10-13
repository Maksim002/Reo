package ru.ktsstudio.core_data_verification_impl.data.db.converter

import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

/**
 * Created by Igor Park on 25/06/2020.
 */
interface SurveyProgressFetcher {
    fun fetchSurveyProgress(surveyString: String): Map<SurveySubtype, Progress>
}