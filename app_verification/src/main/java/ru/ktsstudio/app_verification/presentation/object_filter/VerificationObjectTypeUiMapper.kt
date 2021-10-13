package ru.ktsstudio.app_verification.presentation.object_filter

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 04.12.2020.
 */
class VerificationObjectTypeUiMapper @Inject constructor(
    private val resourceManager: ResourceManager
) : Mapper<VerificationObjectType, String> {
    override fun map(item: VerificationObjectType): String {
        val resId = when (item) {
            VerificationObjectType.WASTE_TREATMENT -> R.string.object_type_treatment
            VerificationObjectType.WASTE_PLACEMENT -> R.string.object_type_placement
            VerificationObjectType.WASTE_RECYCLING -> R.string.object_type_recycling
            VerificationObjectType.WASTE_DISPOSAL -> R.string.object_type_disposal
        }
        return resourceManager.getString(resId)
    }
}
