package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * Created by Igor Park on 14/12/2020.
 */
@ProvidedTypeConverter
class ReferenceConverter @Inject constructor(
    private val gson: Gson
) {
    @TypeConverter
    fun convertToString(reference: Reference?): String? {
        return reference?.let(gson::toJson)
    }

    @TypeConverter
    fun convertFromString(reference: String?): Reference? {
        return reference?.let{ gson.fromJson<Reference>(reference) }
    }
}