package ru.ktsstudio.app_verification.domain.map.object_info

import com.badoo.mvicore.element.Reducer

class ObjectInfoReducer : Reducer<ObjectInfoFeature.State, ObjectInfoFeature.Effect> {
    override fun invoke(
        state: ObjectInfoFeature.State,
        effect: ObjectInfoFeature.Effect
    ): ObjectInfoFeature.State {
        return when (effect) {
            ObjectInfoFeature.Effect.Loading -> state.copy(
                error = null,
                isLoading = true
            )
            is ObjectInfoFeature.Effect.Error -> state.copy(
                isLoading = false,
                error = effect.throwable
            )
            is ObjectInfoFeature.Effect.Success -> state.copy(
                isLoading = false,
                error = null,
                infoList = effect.infoList
            )
            is ObjectInfoFeature.Effect.GpsAvailabilityState -> state.copy(
                destination = effect.destination
            )
            is ObjectInfoFeature.Effect.NavigateToMapWithRoute,
            is ObjectInfoFeature.Effect.LocationNotAvailable -> state
        }
    }
}
