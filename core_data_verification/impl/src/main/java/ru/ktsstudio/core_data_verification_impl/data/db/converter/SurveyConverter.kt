package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSurvey
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 25.11.2020.
 */
@ProvidedTypeConverter
class SurveyConverter @Inject constructor(
    private val gson: Gson,
    private val serializableSurveyMapper: Mapper<Survey, SerializableSurvey>,
    private val surveyMapper: Mapper<SerializableSurvey, Survey>
) {

    @TypeConverter
    fun convertToString(survey: Survey): String {
        return serializableSurveyMapper.map(survey)
            .let { gson.toJson(it) }
    }

    @TypeConverter
    fun convertFromString(surveyString: String): Survey {
        val typeString = JsonParser.parseString(surveyString)
            .asJsonObject
            .get(TYPE_FIELD_NAME)
            .asString
        val type = gson.fromJson<VerificationObjectType>(typeString).requireNotNull()


        return when (type) {
            VerificationObjectType.WASTE_TREATMENT -> {
                gson.fromJson<SerializableSurvey.SerializableWasteTreatmentSurvey>(surveyString)
            }
            VerificationObjectType.WASTE_PLACEMENT -> {
                gson.fromJson<SerializableSurvey.SerializableWastePlacementSurvey>(surveyString)
            }
            VerificationObjectType.WASTE_RECYCLING -> {
                gson.fromJson<SerializableSurvey.SerializableWasteRecyclingSurvey>(surveyString)
            }
            VerificationObjectType.WASTE_DISPOSAL -> {
                gson.fromJson<SerializableSurvey.SerializableWasteDisposalSurvey>(surveyString)
            }
        }
            .requireNotNull()
            .let(surveyMapper::map)
    }

    companion object {
        private const val TYPE_FIELD_NAME = "type"
    }
}