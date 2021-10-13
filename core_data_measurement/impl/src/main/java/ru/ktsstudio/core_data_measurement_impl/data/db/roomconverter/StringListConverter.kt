package ru.ktsstudio.core_data_measurement_impl.data.db.roomconverter

import androidx.room.TypeConverter

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.10.2020.
 */
class StringListConverter {

    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        return stringList.joinToString(DELIMITER)
    }

    @TypeConverter
    fun toStringList(serialized: String): List<String> {
        return serialized.split(DELIMITER)
    }

    companion object {
        private const val DELIMITER = "\n\r"
    }

}