package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.gson.deserializeEnum
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import javax.inject.Inject

@ProvidedTypeConverter
class EnumConverters @Inject constructor(
    private val gson: Gson
) {
    @TypeConverter
    fun fromObjectVerificationType(objectType: VerificationObjectType): String {
        return gson.toJson(objectType)
    }

    @TypeConverter
    fun toObjectVerificationType(name: String): VerificationObjectType {
        return gson.deserializeEnum(name)
    }

    @TypeConverter
    fun fromReferenceType(type: ReferenceType): String {
        return gson.toJson(type)
    }

    @TypeConverter
    fun toReferenceType(name: String): ReferenceType {
        return gson.deserializeEnum(name)
    }

    @TypeConverter
    fun fromModelState(state: LocalModelState) = gson.toJson(state)

    @TypeConverter
    fun toModelState(name: String): LocalModelState = gson.deserializeEnum(name)
}