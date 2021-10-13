package ru.ktsstudio.settings.domain

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Ovchinnikov on 20.10.2020.
 */
internal class SettingsNewsPublisher : NewsPublisher<
    SettingsFeature.Wish,
    SettingsFeature.Effect,
    SettingsFeature.State,
    SettingsFeature.News
    > {

    override fun invoke(
        action: SettingsFeature.Wish,
        effect: SettingsFeature.Effect,
        state: SettingsFeature.State
    ): SettingsFeature.News? {
        return when (effect) {
            is SettingsFeature.Effect.SupportEmail -> {
                if (effect.emailAddress == null) return null
                SettingsFeature.News.SendEmail(effect.emailAddress)
            }
            is SettingsFeature.Effect.SupportPhone -> {
                if (effect.phoneNumber == null) return null
                SettingsFeature.News.CallPhone(effect.phoneNumber)
            }
            is SettingsFeature.Effect.LogoutSuccess -> SettingsFeature.News.Logout
            is SettingsFeature.Effect.LogoutError -> SettingsFeature.News.LogoutError
            else -> null
        }
    }
}