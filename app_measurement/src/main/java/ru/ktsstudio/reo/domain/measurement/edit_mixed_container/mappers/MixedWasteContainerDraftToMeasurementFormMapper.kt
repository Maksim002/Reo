package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 02.12.2020.
 */
class MixedWasteContainerDraftToMeasurementFormMapper @Inject constructor() :
    Mapper<MixedWasteContainerDraft, MeasurementForm> {
    override fun map(item: MixedWasteContainerDraft): MeasurementForm = with(item) {
        MeasurementForm(
            wasteVolume = wasteVolume,
            dailyWeight = dailyGainNetWeight,
            weight = netWeight,
            dailyVolume = dailyGainVolume
        )
    }
}
