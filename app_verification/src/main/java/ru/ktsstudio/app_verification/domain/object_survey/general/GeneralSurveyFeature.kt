package ru.ktsstudio.app_verification.domain.object_survey.general

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.app_verification.domain.object_survey.general.models.GeneralSurveyDraft
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    GeneralSurveyFeature.Wish,
    GeneralSurveyFeature.Effect,
    GeneralSurveyFeature.State,
    GeneralSurveyFeature.News>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {

    data class State(
        val surveyDraft: GeneralSurveyDraft? = null,
        val draftUpdated: Boolean = false,
        val loading: Boolean = true,
        val error: Throwable? = null
    )

    sealed class Wish {
        data class Load(val objectId: String) : Wish()
        data class UpdateSurvey(val updater: Updater<GeneralSurveyDraft>) : Wish()
        data class SaveSurvey(val objectId: String) : Wish()
        object Exit : Wish()
    }

    sealed class Effect {
        object Loading : Effect()

        object Exit : Effect()
        object ShowExitDialog : Effect()

        data class Success(val generalInformationDraft: GeneralSurveyDraft) : Effect()
        data class SurveyUpdated(val updatedDraft: GeneralSurveyDraft) : Effect()
        object SurveySaved : Effect()

        data class Error(override val throwable: Throwable) : Effect(), ErrorEffect
    }

    sealed class News {
        object Exit : News()
        object ShowExitDialog : News()
    }
}
