package ru.ktsstudio.app_verification.presentation.object_inspection

import androidx.annotation.StringRes
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionFeature
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
class ObjectInspectionUiStateTransformer(
    private val resources: ResourceManager
) : (ObjectInspectionFeature.State) -> ObjectInspectionUiState {
    override fun invoke(state: ObjectInspectionFeature.State): ObjectInspectionUiState =
        with(state) {
            val title = listOf(
                LargeTitleItem(resources.getString(R.string.object_inspection_fill_label))
            )

            val progressCards = surveyProgress.map { (surveyType, progress) ->
                createProgressUiCard(surveyType, progress)
            }
                .sortedBy { it.surveySubtype.ordinal }
            return ObjectInspectionUiState(
                error = error,
                isLoading = isLoading,
                progressList = progressCards.takeIfNotEmpty()
                    ?.let { title + progressCards }
                    .orEmpty(),
                isReady = surveyProgress.isNotEmpty() &&
                    surveyProgress.all { (_, progress) ->
                        progress.isDone()
                    } &&
                    isSending.not(),
                isSending = isSending
            )
        }

    private fun createProgressUiCard(
        surveySubtype: SurveySubtype,
        progress: Progress
    ): ProgressCardUi {
        return ProgressCardUi(
            surveySubtype = surveySubtype,
            title = getSurveyTitle(surveySubtype),
            icon = if (progress.isDone()) R.drawable.ic_done_circle else R.drawable.ic_edit_circle,
            progressText = resources.getString(
                R.string.object_inspection_card_progress,
                progress.current,
                progress.max
            )
        )
    }

    @StringRes
    private fun getSurveyTitle(type: SurveySubtype): Int {
        return when (type) {
            SurveySubtype.TECHNICAL -> R.string.object_inspection_card_label_tech
            SurveySubtype.GENERAL -> R.string.object_inspection_card_label_main_info
            SurveySubtype.SCHEDULE -> R.string.object_inspection_card_label_schedule
            SurveySubtype.INFRASTRUCTURAL -> R.string.object_inspection_card_label_infrastructure
            SurveySubtype.PRODUCTION -> R.string.object_inspection_card_label_product_info
            SurveySubtype.EQUIPMENT -> R.string.object_inspection_card_label_equipment
            SurveySubtype.SECONDARY_RESOURCE -> R.string.object_inspection_card_label_secondary_resources
        }
    }
}
