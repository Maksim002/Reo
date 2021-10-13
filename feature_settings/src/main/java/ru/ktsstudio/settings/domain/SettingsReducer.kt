package ru.ktsstudio.settings.domain

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
internal class SettingsReducer : Reducer<SettingsFeature.State, SettingsFeature.Effect> {
    override fun invoke(
        state: SettingsFeature.State,
        effect: SettingsFeature.Effect
    ): SettingsFeature.State {
        return when (effect) {
            is SettingsFeature.Effect.SettingsLoading -> {
                state.copy(
                    loading = true,
                    error = null
                )
            }
            is SettingsFeature.Effect.SettingsSuccess -> {
                state.copy(
                    loading = false,
                    settings = effect.settings,
                    error = null
                )
            }
            is SettingsFeature.Effect.SettingsError -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is SettingsFeature.Effect.SupportEmail,
            is SettingsFeature.Effect.SupportPhone,
            is SettingsFeature.Effect.LogoutSuccess,
            is SettingsFeature.Effect.LogoutError -> state
        }
    }
}