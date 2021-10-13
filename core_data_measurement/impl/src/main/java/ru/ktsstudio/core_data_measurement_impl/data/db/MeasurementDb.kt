package ru.ktsstudio.core_data_measurement_impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ktsstudio.common.utils.db.converters.GpsPointRoomConverter
import ru.ktsstudio.common.utils.db.converters.InstantDateConverter
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.CategoryDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.ContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDraftDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MnoDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MorphologyDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.WasteGroupDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.db.roomconverter.EnumConverters
import ru.ktsstudio.core_data_measurement_impl.data.db.roomconverter.StringListConverter

@Database(
    entities = [
        LocalMno::class,
        LocalMnoContainer::class,
        LocalContainerType::class,
        LocalWasteCategory::class,
        LocalContainerWasteType::class,
        LocalMeasurement::class,
        LocalMeasurementMedia::class,
        LocalMeasurementStatus::class,
        LocalMedia::class,
        LocalMixedWasteContainer::class,
        LocalSeparateWasteContainer::class,
        LocalCategory::class,
        LocalMorphology::class,
        LocalWasteGroup::class,
        LocalWasteSubgroup::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    EnumConverters::class,
    GpsPointRoomConverter::class,
    StringListConverter::class,
    InstantDateConverter::class
)
abstract class MeasurementDb : RoomDatabase() {

    abstract fun mnoDao(): MnoDao
    abstract fun containerDao(): ContainerDao
    abstract fun categoryDao(): CategoryDao
    abstract fun measurementContainerDao(): MeasurementContainerDao
    abstract fun measurementDraftDao(): MeasurementDraftDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun mediaDao(): MediaDao
    abstract fun morphologyDao(): MorphologyDao
    abstract fun wasteGroupDao(): WasteGroupDao

    companion object {
        const val dbName = "MeasurementDb"
    }
}