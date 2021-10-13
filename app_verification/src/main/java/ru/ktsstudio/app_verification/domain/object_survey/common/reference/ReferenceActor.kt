package ru.ktsstudio.app_verification.domain.object_survey.common.reference

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository

internal class ReferenceActor(
    private val objectRepository: ObjectRepository,
    private val schedulers: SchedulerProvider,
) : Actor<ReferenceFeature.State,
    ReferenceFeature.Wish,
    ReferenceFeature.Effect> {

    override fun invoke(
        state: ReferenceFeature.State,
        action: ReferenceFeature.Wish
    ): Observable<ReferenceFeature.Effect> {
        return when (action) {
            is ReferenceFeature.Wish.Load -> {
                objectRepository.getAllReferences()
                    .toObservable()
                    .map<ReferenceFeature.Effect>(ReferenceFeature.Effect::Success)
                    .onErrorReturn(ReferenceFeature.Effect::Error)
                    .startWithItem(ReferenceFeature.Effect.Loading)
                    .observeOn(schedulers.ui)
                    .toRx2Observable()
            }
        }
    }
}
