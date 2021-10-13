package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.core_data_verfication_api.data.model.SurveyStatus
import ru.ktsstudio.utilities.extensions.fromJson
import javax.inject.Inject

@ProvidedTypeConverter
class SurveyStatusConverter @Inject constructor(
    private val gson: Gson
) {

    @TypeConverter
    fun convertToString(surveyStatus: SurveyStatus?): String? {
        return surveyStatus?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun convertFromString(surveyStatus: String?): SurveyStatus? {
        return surveyStatus?.let { gson.fromJson<SurveyStatus>(surveyStatus) }
    }
}