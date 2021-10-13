package ru.ktsstudio.app_verification.data.map

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
class VerificationMapResourceProvider @Inject constructor(
    private val resourceManager: ResourceManager
) : MapResourceProvider {

    override fun getMapTitle(): String {
        return resourceManager.getString(R.string.verification_object_list_title)
    }

    override fun getMapSearchHint(): String {
        return resourceManager.getString(R.string.verification_map_search_hint)
    }
}
