package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import javax.inject.Inject

class SeparateContainerDbMerger @Inject constructor() :
    Mapper2<SeparateContainerComposite, LocalSeparateWasteContainer, LocalSeparateWasteContainer> {
    override fun map(
        item1: SeparateContainerComposite,
        item2: LocalSeparateWasteContainer
    ): LocalSeparateWasteContainer = with(item1) {
        return item2.copy(
            isUnique = isUnique,
            containerName = uniqueName,
            containerVolume = uniqueVolume
        )
    }
}