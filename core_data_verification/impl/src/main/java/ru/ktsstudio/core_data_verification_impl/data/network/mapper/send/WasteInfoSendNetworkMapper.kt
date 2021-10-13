package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWasteInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWasteType
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 17.02.2021.
 */
class WasteInfoSendNetworkMapper @Inject constructor(
    private val referenceMapper: Mapper<Reference, RemoteReference>
) {

    fun map(
        wasteTypes: List<Reference>,
        technicalSurvey: TechnicalSurvey
    ): RemoteWasteInfo {
        return RemoteWasteInfo(
            wasteTypes = wasteTypes.map(referenceMapper::map)
                .map(::RemoteWasteType),
            receivedWastesWeightThisYear = (technicalSurvey as? TechnicalSurvey.WasteRecyclingTechnicalSurvey)?.receivedWastesWeightThisYear,
            receivedWastesWeightLastYear = (technicalSurvey as? TechnicalSurvey.WasteRecyclingTechnicalSurvey)?.receivedWastesWeightLastYear
        )
    }
}