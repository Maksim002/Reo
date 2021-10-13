package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import com.google.gson.Gson
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyProgressFetcher
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableCheckedSurvey
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
class SurveyProgressMapper @Inject constructor(
    private val gson: Gson,
    private val serializableCheckedSurveyMapper: Mapper<CheckedSurvey, SerializableCheckedSurvey>,
    private val surveyProgressFetcher: SurveyProgressFetcher
) : Mapper<LocalCheckedSurvey, @JvmSuppressWildcards Map<SurveySubtype, Progress>> {
    override fun map(item: LocalCheckedSurvey): Map<SurveySubtype, Progress> {
        val surveyJsonString = serializableCheckedSurveyMapper.map(item.checkedSurvey)
            .let { gson.toJson(it) }
        return surveyProgressFetcher.fetchSurveyProgress(surveyJsonString)
    }
}