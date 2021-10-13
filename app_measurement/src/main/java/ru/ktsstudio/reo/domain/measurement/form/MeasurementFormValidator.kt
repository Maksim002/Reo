package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.common.utils.orDefault
import ru.ktsstudio.form_feature.form_validation.Validator

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
class MeasurementFormValidator : Validator<MeasurementForm, MeasurementValidationResult>() {
    override fun validate(input: MeasurementForm): MeasurementValidationResult {
        val wasteVolume = input.wasteVolume.orDefault(0f)
        val dailyVolume = input.dailyVolume.orDefault(0f)
        val wasteWeight = input.weight.orDefault(0f)
        val dailyWeight = input.dailyWeight.orDefault(0f)

        val minDailyWeight = if (dailyVolume > 0f) 0f else Float.NEGATIVE_INFINITY
        val minDailyVolume = if (dailyWeight > 0f) 0f else Float.NEGATIVE_INFINITY

        return MeasurementValidationResult(
            // объем отходов больше или равен суточному приросту объема отходов
            volume = GTEValidationResult(
                isNotEmpty = input.wasteVolume != null,
                isGreatOrEqual = wasteVolume.gte(dailyVolume)
            ),
            // масса отходов нетто больше или равна суточному приросту массы отходов
            weight = GTEValidationResult(
                isNotEmpty = input.weight != null,
                isGreatOrEqual = wasteWeight.gte(dailyWeight),
            ),
            // Значение суточного прироста массы должно быть строго > 0, если суточный прирост объема отходов > 0
            dailyWeight = GTValidationResult(
                isGreatThen = dailyWeight.gt(minDailyWeight),
                isNotEmpty = input.dailyWeight != null
            ),
            // Для поля "Суточный прирост объема отходов": Значение должно быть строго > 0, если суточный прирост массы отходов нетто > 0
            dailyVolume = GTValidationResult(
                isGreatThen = dailyVolume.gt(minDailyVolume),
                isNotEmpty = input.dailyVolume != null
            )
        )
    }

    private fun Float.gte(other: Float): Boolean {
        return this - other > -FLOAT_DELTA
    }

    private fun Float.gt(other: Float): Boolean {
        return this - other > FLOAT_DELTA
    }

    companion object {
        private const val FLOAT_DELTA = 0.00001
    }
}
