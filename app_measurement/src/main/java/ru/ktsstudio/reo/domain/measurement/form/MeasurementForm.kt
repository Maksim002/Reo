package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.form_feature.form.CommonForm

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
data class MeasurementForm(
    val wasteVolume: Float?,
    val dailyWeight: Float?,
    val weight: Float?,
    val dailyVolume: Float?,
) : CommonForm {
    override fun isEmpty(): Boolean {
        return wasteVolume == null &&
            dailyWeight == null &&
            weight == null &&
            dailyVolume == null
    }
}
