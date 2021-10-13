package ru.ktsstudio.app_verification.domain.object_survey.general

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.app_verification.domain.object_survey.general.models.GeneralSurveyDraft
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.Rx3Single
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.utilities.extensions.requireNotNull
import ru.ktsstudio.utilities.extensions.zipWithTimer

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyActor(
    private val objectRepository: ObjectRepository,
    private val schedulerProvider: SchedulerProvider
) : Actor<GeneralSurveyFeature.State, GeneralSurveyFeature.Wish, GeneralSurveyFeature.Effect> {

    override fun invoke(
        state: GeneralSurveyFeature.State,
        action: GeneralSurveyFeature.Wish
    ): Observable<GeneralSurveyFeature.Effect> {
        return when (action) {
            is GeneralSurveyFeature.Wish.Load -> observeGeneralSurvey(action.objectId)
            is GeneralSurveyFeature.Wish.UpdateSurvey -> {
                val currentDraft = state.surveyDraft
                    ?: error("Action: $action, draft is not initialized!")
                Observable.just(
                    GeneralSurveyFeature.Effect.SurveyUpdated(
                        action.updater.update(currentDraft)
                    )
                )
            }

            is GeneralSurveyFeature.Wish.SaveSurvey -> {
                saveSurvey(
                    objectId = action.objectId,
                    generalInformationDraft = state.surveyDraft.requireNotNull()
                )
            }

            is GeneralSurveyFeature.Wish.Exit -> {
                if (state.draftUpdated) {
                    Observable.just(GeneralSurveyFeature.Effect.ShowExitDialog)
                } else {
                    Observable.just(GeneralSurveyFeature.Effect.Exit)
                }
            }
        }
    }

    private fun observeGeneralSurvey(objectId: String): Observable<GeneralSurveyFeature.Effect> {
        // todo fias address
        return Rx3Single.zip(
            objectRepository.getObjectWithCheckedSurvey(objectId)
                .toSingle(),
            objectRepository.getAllReferences(),
            BiFunction<
                VerificationObjectWithCheckedSurvey,
                List<Reference>,
                GeneralSurveyDraft
                > { (verificationObject: VerificationObject, checkedSurvey: CheckedSurvey),
                references: List<Reference> ->
                val typeToReferenceMap = references.groupBy { it.type }
                GeneralSurveyDraft(
                    information = verificationObject.generalInformation,
                    objectStatus = verificationObject.objectStatus,
                    otherStatusName = verificationObject.otherObjectStatusName,
                    generalCheckedSurvey = checkedSurvey.generalCheckedSurvey,
                    hasOtherObjectStatus = verificationObject.objectStatus?.type == ReferenceType.OTHER &&
                        verificationObject.otherObjectStatusName.isNullOrBlank().not(),
                    objectStatuses = typeToReferenceMap[ReferenceType.OBJECT_STATUS].orEmpty(),
                    subjects = typeToReferenceMap[ReferenceType.REGION].orEmpty(),
                    type = verificationObject.type
                )
            }
        )
            .zipWithTimer(DELAY)
            .toObservable()
            .map<GeneralSurveyFeature.Effect>(GeneralSurveyFeature.Effect::Success)
            .startWithItem(GeneralSurveyFeature.Effect.Loading)
            .onErrorReturn(GeneralSurveyFeature.Effect::Error)
            .observeOn(schedulerProvider.ui)
            .toRx2Observable()
    }

    private fun saveSurvey(
        objectId: String,
        generalInformationDraft: GeneralSurveyDraft
    ): Observable<GeneralSurveyFeature.Effect> {
        return objectRepository.getObjectWithCheckedSurvey(objectId)
            .map {
                it.copy(
                    verificationObject = it.verificationObject.copy(
                        generalInformation = generalInformationDraft.information,
                        otherObjectStatusName = generalInformationDraft.otherStatusName,
                        objectStatus = generalInformationDraft.objectStatus
                    ),
                    checkedSurvey = it.checkedSurvey.copyGeneralSurvey(generalInformationDraft.generalCheckedSurvey)
                )
            }
            .flatMapCompletable { objectRepository.saveObjectWithCheckedSurvey(it) }
            .andThen(
                Rx3Observable.just<GeneralSurveyFeature.Effect>(
                    GeneralSurveyFeature.Effect.SurveySaved
                )
            )
            .onErrorReturn(GeneralSurveyFeature.Effect::Error)
            .observeOn(schedulerProvider.ui)
            .toRx2Observable()
    }

    companion object {
        private const val DELAY = 500L
    }
}
