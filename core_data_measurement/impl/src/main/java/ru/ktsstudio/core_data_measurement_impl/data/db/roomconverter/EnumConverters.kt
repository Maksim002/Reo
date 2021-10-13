package ru.ktsstudio.core_data_measurement_impl.data.db.roomconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.gson.deserializeEnum
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.MediaType
import javax.inject.Inject

@ProvidedTypeConverter
class EnumConverters @Inject constructor(
    private val gson: Gson
) {

    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String = gson.toJson(mediaType)

    @TypeConverter
    fun toMediaType(name: String): MediaType = gson.deserializeEnum(name)

    @TypeConverter
    fun fromMediaCategory(mediaCategory: MeasurementMediaCategory): String = gson.toJson(mediaCategory)

    @TypeConverter
    fun toMediaCategory(name: String): MeasurementMediaCategory = gson.deserializeEnum(name)

    @TypeConverter
    fun fromDraftState(draftState: DraftState): String = gson.toJson(draftState)

    @TypeConverter
    fun toDraftState(name: String): DraftState = gson.deserializeEnum(name)

    @TypeConverter
    fun fromModelState(state: LocalModelState) = gson.toJson(state)

    @TypeConverter
    fun toModelState(name: String): LocalModelState = gson.deserializeEnum(name)
}