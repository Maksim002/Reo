package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import arrow.core.k
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResourceType
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResources
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.12.2020.
 */
class SecondaryResourceMapper @Inject constructor(
    private val referenceMapper: Mapper<Reference, RemoteReference>
) : Mapper<SecondaryResourcesSurvey, RemoteSecondaryResources> {
    override fun map(item: SecondaryResourcesSurvey): RemoteSecondaryResources {
        return RemoteSecondaryResources(
            extractPercent = item.extractPercent,
            types = item.nullifyOtherTypes().types.values.map {
                RemoteSecondaryResourceType(
                    id = it.id,
                    type = it.reference?.let(referenceMapper::map),
                    percent = it.percent,
                    otherName = it.otherName
                )
            }
        )
    }

    private fun SecondaryResourcesSurvey.nullifyOtherTypes(): SecondaryResourcesSurvey {
        return this.copy(
            types = types.map {
                if (it.reference?.type == ReferenceType.OTHER) {
                    it.copy(reference = null)
                } else {
                    it
                }
            }.k()
        )
    }
}