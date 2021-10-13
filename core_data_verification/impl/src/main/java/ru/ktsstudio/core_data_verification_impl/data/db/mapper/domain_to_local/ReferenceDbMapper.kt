package ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class ReferenceDbMapper @Inject constructor() : Mapper<Reference, LocalReference> {
    override fun map(item: Reference): LocalReference {
        return LocalReference(
            id = item.id,
            name = item.name,
            type = item.type
        )
    }
}