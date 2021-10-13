package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import javax.inject.Inject

/**
 * Created by Igor Park on 31/10/2020.
 */
class SeparateContainerCompositeMapper @Inject constructor(
    private val containerTypeMapper: Mapper<LocalContainerType, ContainerType>,
    private val mnoContainerMapper: Mapper<LocalMnoContainerWithRelations, MnoContainer>,
    private val wasteTypeMapper: Mapper<LocalContainerWasteTypeWithRelation, ContainerWasteType>
) : Mapper<LocalSeparateWasteContainerWithRelations, SeparateContainerComposite> {
    override fun map(item: LocalSeparateWasteContainerWithRelations): SeparateContainerComposite =
        with(item) {
            SeparateContainerComposite(
                containerType = containerType.let(containerTypeMapper::map),
                isUnique = container.isUnique,
                localId = container.localId,
                mnoContainer = mnoContainer?.let(mnoContainerMapper::map),
                uniqueVolume = container.containerVolume,
                uniqueName = container.containerName,
                wasteTypes = wasteTypeMapper.map(wasteTypes),
                draftState = container.draftState
            )
        }
}