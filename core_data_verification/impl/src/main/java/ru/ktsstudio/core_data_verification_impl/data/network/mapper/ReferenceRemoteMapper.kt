package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class ReferenceRemoteMapper @Inject constructor() : Mapper<RemoteReference, Reference> {
    override fun map(item: RemoteReference): Reference {
        return Reference(
            id = item.id,
            type = item?.type?.name ?: run { error("reference type is null for item = $item") },
            name = item.value
        )
    }
}