package ru.ktsstudio.app_verification.domain.object_inspection

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
class ObjectInspectionFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    ObjectInspectionFeature.Wish,
    ObjectInspectionFeature.Effect,
    ObjectInspectionFeature.State,
    ObjectInspectionFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val surveyProgress: Map<SurveySubtype, Progress> = emptyMap(),
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val isSending: Boolean = false
    )

    sealed class Wish {
        data class Load(val objectId: String) : Wish()
        data class SendSurvey(val objectId: String) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        object Sending : Effect()
        object SendComplete : Effect()

        data class Success(val surveyProgress: Map<SurveySubtype, Progress>) : Effect()
        data class SendFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        data class Error(override val throwable: Throwable) : Effect(), ErrorEffect
    }

    sealed class News {
        data class SendFailed(val throwable: Throwable) : News()
        object SendComplete : News()
    }
}
