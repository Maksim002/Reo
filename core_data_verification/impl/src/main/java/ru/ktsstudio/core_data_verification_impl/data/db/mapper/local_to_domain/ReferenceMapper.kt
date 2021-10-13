package ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class ReferenceMapper @Inject constructor() : Mapper<LocalReference, Reference> {
    override fun map(item: LocalReference): Reference {
        return Reference(
            id = item.id,
            name = item.name,
            type = item.type
        )
    }
}