package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteType
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.12.2020.
 */
class ReferenceSendRemoteMapper @Inject constructor() : Mapper<Reference, RemoteReference> {
    override fun map(item: Reference): RemoteReference {
        return RemoteReference(
            id = item.id,
            type = RemoteType(
                name = item.type,
                displayName = ""
            ),
            value = item.name
        )
    }
}