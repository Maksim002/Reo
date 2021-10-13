package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import javax.inject.Inject

/**
 * Created by Igor Park on 15/10/2020.
 */
class MixedContainerCompositeMapper @Inject constructor(
    private val containerTypeMapper: Mapper<LocalContainerType, ContainerType>,
    private val mnoContainerMapper: Mapper<LocalMnoContainerWithRelations, MnoContainer>
) : Mapper<
    LocalMixedWasteContainerWithRelations,
    MixedWasteContainerComposite
    > {
    override fun map(item: LocalMixedWasteContainerWithRelations): MixedWasteContainerComposite =
        with(item.container) {
            return MixedWasteContainerComposite(
                localId = localId,
                isUnique = isUnique,
                containerType = containerTypeMapper.map(item.containerType),
                mnoContainer = item.mnoContainer?.let(mnoContainerMapper::map),
                uniqueName = containerName,
                uniqueVolume = containerVolume,
                containerFullness = containerFullness,
                wasteVolume = wasteVolume,
                dailyGainVolume = dailyGainVolume,
                netWeight = netWeight,
                dailyGainNetWeight = dailyGainNetWeight,
                emptyContainerWeight = emptyContainerWeight,
                filledContainerWeight = filledContainerWeight,
                draftState = draftState
            )
        }
}