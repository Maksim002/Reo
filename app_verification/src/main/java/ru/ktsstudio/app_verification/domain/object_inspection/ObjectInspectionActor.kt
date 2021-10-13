package ru.ktsstudio.app_verification.domain.object_inspection

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
class ObjectInspectionActor(
    private val objectRepository: ObjectRepository,
    private val schedulers: SchedulerProvider
) : Actor<ObjectInspectionFeature.State, ObjectInspectionFeature.Wish, ObjectInspectionFeature.Effect> {

    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: ObjectInspectionFeature.State,
        action: ObjectInspectionFeature.Wish
    ): Observable<ObjectInspectionFeature.Effect> {
        return when (action) {
            is ObjectInspectionFeature.Wish.Load -> observeInspectionProgress(action.objectId)
            is ObjectInspectionFeature.Wish.SendSurvey -> Observable.just(ObjectInspectionFeature.Effect.SendComplete)
        }
    }

    private fun observeInspectionProgress(objectId: String): Observable<ObjectInspectionFeature.Effect> {
        interruptSignal.onNext(Unit)

        return objectRepository.observeSurveyProgress(objectId)
            .map<ObjectInspectionFeature.Effect>(ObjectInspectionFeature.Effect::Success)
            .startWithItem(ObjectInspectionFeature.Effect.Loading)
            .onErrorReturn { ObjectInspectionFeature.Effect.Error(it) }
            .takeUntil(interruptSignal)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}
