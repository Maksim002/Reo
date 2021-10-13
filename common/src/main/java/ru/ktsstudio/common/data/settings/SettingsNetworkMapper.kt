package ru.ktsstudio.common.data.settings

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.models.RemoteSettings
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
class SettingsNetworkMapper @Inject constructor() : Mapper<RemoteSettings, Settings> {

    override fun map(item: RemoteSettings): Settings = with(item) {
        Settings(
            supportEmail = supportEmail,
            supportPhoneNumber = supportPhoneNumber
        )
    }
}