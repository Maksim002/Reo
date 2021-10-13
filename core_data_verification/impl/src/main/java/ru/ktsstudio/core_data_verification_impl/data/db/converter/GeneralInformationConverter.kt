package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableGeneralInformation
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
@ProvidedTypeConverter
class GeneralInformationConverter @Inject constructor(
    private val gson: Gson,
    private val generalInformationMapper: Mapper<SerializableGeneralInformation, GeneralInformation>,
    private val fiasAddressToRemoteMapper: Mapper<FiasAddress, RemoteFiasAddress>
) {
    @TypeConverter
    fun convertToString(generalInformation: GeneralInformation): String = with(generalInformation) {
        return SerializableGeneralInformation(
            subject = subject,
            fiasAddress = fiasAddress?.let(fiasAddressToRemoteMapper::map),
            addressDescription = addressDescription,
            name = name
        )
            .let { gson.toJson(it) }
    }

    @TypeConverter
    fun convertFromString(infoString: String): GeneralInformation {
        return gson.fromJson<SerializableGeneralInformation>(infoString)
            .requireNotNull()
            .let(generalInformationMapper::map)
    }
}