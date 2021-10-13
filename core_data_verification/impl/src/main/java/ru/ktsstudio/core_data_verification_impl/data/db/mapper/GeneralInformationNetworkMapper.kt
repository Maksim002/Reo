package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
class GeneralInformationNetworkMapper @Inject constructor(
    private val fiasAddressMapper: Mapper<RemoteFiasAddress, FiasAddress>,
    private val referenceRemoteMapper: Mapper<RemoteReference, Reference>
) : Mapper<RemoteVerificationObject, GeneralInformation> {
    override fun map(item: RemoteVerificationObject): GeneralInformation = with(item) {
        return GeneralInformation(
            subject = referenceRemoteMapper.map(item.subject),
            fiasAddress = item.fiasAddress?.let(fiasAddressMapper::map),
            addressDescription = item.addressDescription,
            name = item.name
        )
    }
}