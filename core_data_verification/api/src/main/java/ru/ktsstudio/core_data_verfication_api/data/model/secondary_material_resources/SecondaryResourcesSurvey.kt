package ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources

import arrow.core.MapK
import arrow.optics.optics

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.12.2020.
 */
@optics
data class SecondaryResourcesSurvey(
    val extractPercent: Float?,
    val types: MapK<String, SecondaryResourceType>
) {
    companion object
}