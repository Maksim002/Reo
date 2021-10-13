package ru.ktsstudio.settings.domain

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.common.data.models.Settings

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
internal class SettingsFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    SettingsFeature.Wish,
    SettingsFeature.Effect,
    SettingsFeature.State,
    SettingsFeature.News>(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    bootstrapper = null,
    newsPublisher = newsPublisher
) {

    data class State(
        val loading: Boolean = true,
        val settings: Settings = Settings(supportEmail = null, supportPhoneNumber = null),
        val error: Throwable? = null
    )

    sealed class Wish {
        object Load : Wish()
        object SendSupportEmail : Wish()
        object CallSupportPhoneNumber : Wish()
        object Logout : Wish()
    }

    sealed class Effect {
        object SettingsLoading : Effect()
        data class SettingsSuccess(
            val settings: Settings
        ) : Effect()
        data class SettingsError(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
        data class SupportEmail(
            val emailAddress: String?
        ) : Effect()
        data class SupportPhone(
            val phoneNumber: String?
        ) : Effect()
        object LogoutSuccess : Effect()
        data class LogoutError(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }

    sealed class News {
        data class SendEmail(val emailAddress: String) : News()
        data class CallPhone(val phoneNumber: String) : News()
        object Logout : News()
        object LogoutError : News()
    }
}