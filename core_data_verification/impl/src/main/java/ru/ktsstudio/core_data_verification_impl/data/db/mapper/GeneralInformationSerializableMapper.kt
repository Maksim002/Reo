package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableGeneralInformation
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
class GeneralInformationSerializableMapper @Inject constructor(
    private val fiasAddressMapper: Mapper<RemoteFiasAddress, FiasAddress>
) : Mapper<SerializableGeneralInformation, GeneralInformation> {

    override fun map(item: SerializableGeneralInformation): GeneralInformation = with(item) {
        return GeneralInformation(
            subject = subject,
            fiasAddress = fiasAddress?.let (fiasAddressMapper::map),
            addressDescription = addressDescription,
            name = name
        )
    }
}