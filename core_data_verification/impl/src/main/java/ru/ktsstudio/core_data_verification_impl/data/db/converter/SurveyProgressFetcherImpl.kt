package ru.ktsstudio.core_data_verification_impl.data.db.converter

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

class SurveyProgressFetcherImpl @Inject constructor(
    private val gson: Gson
) : SurveyProgressFetcher {
    override fun fetchSurveyProgress(
        surveyString: String
    ): Map<SurveySubtype, Progress> {
        val surveyJson = JsonParser.parseString(surveyString).asJsonObject
        val allMembers = surveyJson.entrySet()
        val surveys = allMembers.filter { (_, memberJson) ->
            memberJson.isJsonObject && memberJson.asJsonObject.has(SURVEY_TYPE)
        }

        return surveys.associateBy(
            keySelector = { (_, survey) ->
                survey.getSurveySubtype()
            },
            valueTransform = { (_, surveyJson) ->
                when(surveyJson.getSurveySubtype()) {
                    SurveySubtype.EQUIPMENT -> getEquipmentProgress(surveyJson)
                    else -> getSimpleProgress(surveyJson)
                }
            }
        )
    }

    private fun JsonElement.getSurveySubtype(): SurveySubtype {
        val typeString = asJsonObject.get(SURVEY_TYPE)
            .asString
        return gson.fromJson<SurveySubtype>(typeString).requireNotNull()
    }

    private fun getSimpleProgress(surveyJson: JsonElement): Progress {
        val surveyFields = surveyJson.asJsonObject.entrySet()
        val verifiedFields = surveyFields.filter { (_, field) ->
            field.isBoolean()
        }
        val checked = verifiedFields.count { (_, isCheckedJson) ->
            isCheckedJson.asBoolean
        }
        return Progress(
            current = checked,
            max = verifiedFields.size
        )
    }

    private fun getEquipmentProgress(surveyJson: JsonElement): Progress {
        val simpleProgress = getSimpleProgress(surveyJson)

        val separatorsCount = surveyJson.asJsonObject.get(SEPARATORS).asJsonArray.size()
        val separatorsMaxCount = SeparatorType.values().size
        val separatorsProgress = Progress(separatorsCount, separatorsMaxCount)

        val pressesCount = surveyJson.asJsonObject.get(PRESSES).asJsonArray.size()
        val pressesMaxCount = PressType.values().size
        val pressesProgress = Progress(pressesCount, pressesMaxCount)

        return simpleProgress + separatorsProgress + pressesProgress
    }

    private fun JsonElement.isBoolean(): Boolean {
        return try {
            asJsonPrimitive.isBoolean
        } catch (ex: IllegalStateException) {
            false
        }
    }

    companion object {
        private const val SURVEY_TYPE = "surveySubtype"
        private const val SEPARATORS = "separators"
        private const val PRESSES = "presses"
    }
}