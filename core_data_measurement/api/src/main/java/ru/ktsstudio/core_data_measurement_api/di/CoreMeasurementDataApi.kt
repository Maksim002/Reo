package ru.ktsstudio.core_data_measurement_api.di

import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.core_data_measurement_api.data.CategoryRepository
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.RegisterRepository
import ru.ktsstudio.core_data_measurement_api.data.SyncRepository

interface CoreMeasurementDataApi {

    fun registerRepository(): RegisterRepository
    fun syncRepository(): SyncRepository
    fun mnoRepository(): MnoRepository
    fun measurementRepository(): MeasurementRepository
    fun categoryRepository(): CategoryRepository
    fun settingsRepository(): SettingsRepository
    fun mediaRepository(): MediaRepository

    fun databaseCleaner(): DatabaseCleaner
    fun fileManager(): FileManager
}