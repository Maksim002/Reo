package ru.ktsstudio.reo.domain.measurement.morphology.item_info

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

class EditMorphologyItemNewsPublisher(
    private val draftToFormMapper: Mapper<MorphologyItemDraft, MeasurementForm>
) : NewsPublisher<
    EditMorphologyItemFeature.Wish,
    EditMorphologyItemFeature.Effect,
    EditMorphologyItemFeature.State,
    EditMorphologyItemFeature.News
    > {

    override fun invoke(
        action: EditMorphologyItemFeature.Wish,
        effect: EditMorphologyItemFeature.Effect,
        state: EditMorphologyItemFeature.State
    ): EditMorphologyItemFeature.News? {
        return when (effect) {
            is EditMorphologyItemFeature.Effect.DataUpdateFailed -> {
                EditMorphologyItemFeature.News.DataUpdateFailed(effect.throwable)
            }
            is EditMorphologyItemFeature.Effect.DataUpdateCompleted -> {
                EditMorphologyItemFeature.News.DataUpdateCompleted
            }
            is EditMorphologyItemFeature.Effect.DataInitialized -> {
                EditMorphologyItemFeature.News.FormChanged(draftToFormMapper.map(effect.morphologyItemDraft))
            }
            else -> null
        }
    }
}
