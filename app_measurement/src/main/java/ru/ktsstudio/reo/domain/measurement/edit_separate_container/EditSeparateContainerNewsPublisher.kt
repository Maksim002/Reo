package ru.ktsstudio.reo.domain.measurement.edit_separate_container

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.News
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.Wish

class EditSeparateContainerNewsPublisher : NewsPublisher<Wish, Effect, State, News> {
    override fun invoke(
        action: Wish,
        effect: Effect,
        state: State
    ): News? {
        return when (effect) {
            is Effect.DataUpdateFailed -> News.DataUpdateFailed(effect.throwable)
            is Effect.DataUpdated -> News.DataUpdated
            is Effect.DataCleared -> News.DataCleared
            else -> null
        }
    }
}
