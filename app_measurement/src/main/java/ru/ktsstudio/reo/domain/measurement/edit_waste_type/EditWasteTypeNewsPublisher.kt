package ru.ktsstudio.reo.domain.measurement.edit_waste_type

import com.badoo.mvicore.element.NewsPublisher

class EditWasteTypeNewsPublisher : NewsPublisher<
    EditWasteTypeFeature.Wish,
    EditWasteTypeFeature.Effect,
    EditWasteTypeFeature.State,
    EditWasteTypeFeature.News
    > {

    override fun invoke(
        action: EditWasteTypeFeature.Wish,
        effect: EditWasteTypeFeature.Effect,
        state: EditWasteTypeFeature.State
    ): EditWasteTypeFeature.News? {
        return when (effect) {
            is EditWasteTypeFeature.Effect.FormChanged -> {
                EditWasteTypeFeature.News.FormChanged(effect.form)
            }
            is EditWasteTypeFeature.Effect.DataUpdateFailed -> {
                EditWasteTypeFeature.News.DataUpdateFailed(effect.throwable)
            }
            is EditWasteTypeFeature.Effect.DataUpdateCompleted -> {
                EditWasteTypeFeature.News.DataUpdateCompleted
            }
            else -> null
        }
    }
}
