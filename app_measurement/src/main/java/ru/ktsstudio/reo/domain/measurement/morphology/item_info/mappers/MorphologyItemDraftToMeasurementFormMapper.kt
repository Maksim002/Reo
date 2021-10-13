package ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.MorphologyItemDraft
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
class MorphologyItemDraftToMeasurementFormMapper @Inject constructor() : Mapper<MorphologyItemDraft, MeasurementForm> {

    override fun map(item: MorphologyItemDraft): MeasurementForm = with(item) {
        MeasurementForm(
            wasteVolume = null,
            dailyWeight = dailyGainWeight,
            weight = null,
            dailyVolume = dailyGainVolume
        )
    }
}
