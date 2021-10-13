package ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.WasteTypeDraft
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
class WasteTypeDraftToMeasurementFormMapper @Inject constructor() : Mapper<WasteTypeDraft, MeasurementForm> {
    override fun map(item: WasteTypeDraft): MeasurementForm = with(item) {
        MeasurementForm(
            wasteVolume,
            dailyGainNetWeight,
            netWeight,
            dailyGainVolume
        )
    }
}
