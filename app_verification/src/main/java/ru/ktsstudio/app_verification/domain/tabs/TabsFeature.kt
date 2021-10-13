package ru.ktsstudio.app_verification.domain.tabs

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
internal class TabsFeature(
    actor: Actor<Unit, Wish, Effect>,
    reducer: Reducer<Unit, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, Unit, News>
) : BaseMviFeature<
    TabsFeature.Wish,
    TabsFeature.Effect,
    Unit,
    TabsFeature.News>(
        initialState = Unit,
        actor = actor,
        reducer = reducer,
        bootstrapper = null,
        newsPublisher = newsPublisher
    ) {

    sealed class Wish {
        object RefreshData : Wish()
    }

    sealed class Effect {
        object SyncData : Effect()
        object NetworkUnavailable : Effect()
    }

    sealed class News {
        object SyncData : News()
        object ShowNetworkUnavailableDialog : News()
    }
}
