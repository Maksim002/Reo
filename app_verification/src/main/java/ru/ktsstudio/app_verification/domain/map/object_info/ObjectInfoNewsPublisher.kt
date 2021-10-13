package ru.ktsstudio.app_verification.domain.map.object_info

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Ovchinnikov on 14.11.2020.
 */

internal class ObjectInfoNewsPublisher : NewsPublisher<
    ObjectInfoFeature.Wish,
    ObjectInfoFeature.Effect,
    ObjectInfoFeature.State,
    ObjectInfoFeature.News
    > {

    override fun invoke(
        action: ObjectInfoFeature.Wish,
        effect: ObjectInfoFeature.Effect,
        state: ObjectInfoFeature.State
    ): ObjectInfoFeature.News? {
        return when (effect) {
            is ObjectInfoFeature.Effect.NavigateToMapWithRoute -> ObjectInfoFeature.News.NavigateToMapWithRoute(
                effect.route
            )
            is ObjectInfoFeature.Effect.GpsAvailabilityState -> ObjectInfoFeature.News.GpsAvailabilityState(
                effect.gpsState
            )
            is ObjectInfoFeature.Effect.LocationNotAvailable -> ObjectInfoFeature.News.ShowLocationNotAvailableToast
            else -> null
        }
    }
}
