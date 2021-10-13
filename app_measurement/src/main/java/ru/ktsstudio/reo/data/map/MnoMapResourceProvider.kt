package ru.ktsstudio.reo.data.map

import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.reo.R
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
class MnoMapResourceProvider @Inject constructor(
    private val resourceManager: ResourceManager
) : MapResourceProvider {

    override fun getMapTitle(): String {
        return resourceManager.getString(R.string.mno_map_title)
    }

    override fun getMapSearchHint(): String {
        return resourceManager.getString(R.string.mno_map_search_hint)
    }
}
