package ru.ktsstudio.app_verification.domain.object_inspection

import com.badoo.mvicore.element.NewsPublisher

/**
 * Created by Igor Park on 04/12/2020.
 */
class ObjectInspectionNewsPublisher : NewsPublisher<
    ObjectInspectionFeature.Wish,
    ObjectInspectionFeature.Effect,
    ObjectInspectionFeature.State,
    ObjectInspectionFeature.News
    > {
    override fun invoke(
        action: ObjectInspectionFeature.Wish,
        effect: ObjectInspectionFeature.Effect,
        state: ObjectInspectionFeature.State
    ): ObjectInspectionFeature.News? {
        return when (effect) {
            is ObjectInspectionFeature.Effect.SendFailed -> {
                ObjectInspectionFeature.News.SendFailed(effect.throwable)
            }
            is ObjectInspectionFeature.Effect.SendComplete -> {
                ObjectInspectionFeature.News.SendComplete
            }
            else -> null
        }
    }
}
