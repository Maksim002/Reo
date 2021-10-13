package ru.ktsstudio.reo.domain.measurement.edit_mixed_container

import com.badoo.mvicore.element.NewsPublisher

class EditMixedContainerNewsPublisher : NewsPublisher<
    EditMixedContainerFeature.Wish,
    EditMixedContainerFeature.Effect,
    EditMixedContainerFeature.State,
    EditMixedContainerFeature.News
    > {

    override fun invoke(
        action: EditMixedContainerFeature.Wish,
        effect: EditMixedContainerFeature.Effect,
        state: EditMixedContainerFeature.State
    ): EditMixedContainerFeature.News? {
        return when (effect) {
            is EditMixedContainerFeature.Effect.FormChanged -> {
                EditMixedContainerFeature.News.FormChanged(effect.form)
            }
            is EditMixedContainerFeature.Effect.DataUpdateFailed -> {
                EditMixedContainerFeature.News.DataUpdateFailed(effect.throwable)
            }
            is EditMixedContainerFeature.Effect.DataUpdateCompleted -> {
                EditMixedContainerFeature.News.DataUpdateCompleted
            }
            else -> null
        }
    }
}
