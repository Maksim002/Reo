package ru.ktsstudio.core_data_verfication_api.di

import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.RegisterRepository
import ru.ktsstudio.core_data_verfication_api.data.SyncRepository
import ru.ktsstudio.core_data_verfication_api.data.WasteManagementTypeRepository

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
interface CoreVerificationDataApi {
    fun registerRepository(): RegisterRepository
    fun settingsRepository(): SettingsRepository
    fun objectRepository(): ObjectRepository
    fun syncRepository(): SyncRepository
    fun mediaRepository(): MediaRepository
    fun wasteManagementTypeRepository(): WasteManagementTypeRepository

    fun databaseCleaner(): DatabaseCleaner
    fun fileManager(): FileManager
}