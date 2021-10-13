package ru.ktsstudio.reo.domain.measurement.morphology.item_info

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMorphologyItemFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    EditMorphologyItemFeature.Wish,
    EditMorphologyItemFeature.Effect,
    EditMorphologyItemFeature.State,
    EditMorphologyItemFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val morphologyItemDraft: MorphologyItemDraft,
        val requiredDataTypes: Set<ContainerDataType>,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class InitData(val categoryId: Long?) : Wish()

        data class UpdateField(
            val dataType: ContainerDataType,
            val value: String
        ) : Wish()

        data class SaveData(val measurementId: Long, val categoryId: Long?) : Wish()
        data class DeleteData(val categoryId: Long) : Wish()
    }

    sealed class Effect {
        object DataLoading : Effect()
        data class DataLoadError(override val throwable: Throwable) : Effect(), ErrorEffect
        data class DataInitialized(val morphologyItemDraft: MorphologyItemDraft) : Effect()
        object DataUpdateCompleted : Effect()
        data class DataUpdateFailed(override val throwable: Throwable) : Effect(), ErrorEffect
    }

    sealed class News {
        data class DataUpdateFailed(val throwable: Throwable) : News()
        object DataUpdateCompleted : News()
        data class FormChanged(
            val form: MeasurementForm
        ) : News()
    }
}
