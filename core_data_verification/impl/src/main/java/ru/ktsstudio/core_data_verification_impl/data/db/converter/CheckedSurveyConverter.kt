package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableCheckedSurvey
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
@ProvidedTypeConverter
class CheckedSurveyConverter @Inject constructor(
    private val gson: Gson,
    private val serializableCheckedSurveyMapper: Mapper<CheckedSurvey, SerializableCheckedSurvey>,
    private val checkedSurveyMapper: Mapper<SerializableCheckedSurvey, CheckedSurvey>
) {

    @TypeConverter
    fun convertToString(checkedSurvey: CheckedSurvey): String {
        val serializableCheckedSurvey = serializableCheckedSurveyMapper.map(checkedSurvey)
        return when (serializableCheckedSurvey) {
            is SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey -> {
                gson.toJson(serializableCheckedSurvey)
            }
            is SerializableCheckedSurvey.SerializableWastePlacementCheckedSurvey -> {
                gson.toJson(serializableCheckedSurvey)
            }
            is SerializableCheckedSurvey.SerializableWasteDisposalCheckedSurvey -> {
                gson.toJson(serializableCheckedSurvey)
            }
            is SerializableCheckedSurvey.SerializableWasteRecyclingCheckedSurvey -> {
                gson.toJson(serializableCheckedSurvey)
            }
        }
    }

    @TypeConverter
    fun convertFromString(checkedSurveyString: String): CheckedSurvey {
        val type = JsonParser.parseString(checkedSurveyString).asJsonObject
            .get(TYPE_FIELD_NAME)
            .asString
            .let(VerificationObjectType::valueOf)

        return when (type) {
            VerificationObjectType.WASTE_TREATMENT -> {
                gson.fromJson<SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey>(checkedSurveyString)
            }
            VerificationObjectType.WASTE_PLACEMENT -> {
                gson.fromJson<SerializableCheckedSurvey.SerializableWastePlacementCheckedSurvey>(checkedSurveyString)
            }
            VerificationObjectType.WASTE_RECYCLING -> {
                gson.fromJson<SerializableCheckedSurvey.SerializableWasteRecyclingCheckedSurvey>(checkedSurveyString)
            }
            VerificationObjectType.WASTE_DISPOSAL -> {
                gson.fromJson<SerializableCheckedSurvey.SerializableWasteDisposalCheckedSurvey>(checkedSurveyString)
            }
        }
            .requireNotNull()
            .let(checkedSurveyMapper::map)
    }

    companion object {
        private const val TYPE_FIELD_NAME = "type"
    }
}