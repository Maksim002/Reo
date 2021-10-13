package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.AsuCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.EnvironmentMonitoringCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.FencesCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.LightsCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.RadiationControlCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.RoadsCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.SecurityCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.SewagePlantCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.VideoControlCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.WeightControlCardMapper
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.WheelWashingCardMapper
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
// import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.fences
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.lightSystem
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.radiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.roadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.securityCamera
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.securityStation
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.sewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.weightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.wheelsWashing
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.utilities.extensions.orFalse

internal class InfrastructureSurveyUiStateTransformer(
    private val resources: ResourceManager
) : (
    Pair<ObjectSurveyFeature.State<InfrastructureSurveyDraft>, ReferenceFeature.State>
) -> InfrastructureSurveyUiState {
    private val weightControlCardMapper = WeightControlCardMapper(resources)
    private val wheelWashingCardMapper = WheelWashingCardMapper(resources)
    private val sewageCardMapper = SewagePlantCardMapper(resources)
    private val radiationControlCardMapper = RadiationControlCardMapper(resources)
    private val videoControlCardMapper = VideoControlCardMapper(resources)
    private val roadsCardMapper = RoadsCardMapper(resources)
    private val fencesCardMapper = FencesCardMapper(resources)
    private val lightsCardMapper = LightsCardMapper(resources)
    private val securityCardMapper = SecurityCardMapper(resources)
    private val asuCardMapper = AsuCardMapper(resources)
    private val environmentMonitoringCardMapper = EnvironmentMonitoringCardMapper(resources)

    override fun invoke(
        surveyAndReferences: Pair<
            ObjectSurveyFeature.State<InfrastructureSurveyDraft>,
            ReferenceFeature.State
            >
    ): InfrastructureSurveyUiState {
        val surveyState: ObjectSurveyFeature.State<InfrastructureSurveyDraft> =
            surveyAndReferences.first
        val referencesState: ReferenceFeature.State = surveyAndReferences.second

        return InfrastructureSurveyUiState(
            loading = surveyState.loading || referencesState.loading,
            error = surveyState.error ?: referencesState.error,
            data = surveyState.draft?.let { draft ->
                getSurveyCards(draft, referencesState.references)
            }.orEmpty()
        )
    }

    private fun getSurveyCards(
        draft: InfrastructureSurveyDraft,
        types: List<Reference>
    ): List<Any> {
        val survey = draft.infrastructureSurvey
        val checkedSurvey = draft.infrastructureCheckedSurvey
        val references = types.groupBy { it.type }

        val surveys = listOfNotNull(
            weightControlCardMapper.map(
                weightControl = survey.weightControl,
                isChecked = checkedSurvey.weightControl,
                weightControlOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .weightControl
                    .asOptional()
            ),
            wheelWashingCardMapper.map(
                wheelsWashing = survey.wheelsWashing,
                isChecked = checkedSurvey.wheelsWashing,
                wheelsOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .wheelsWashing
                    .asOptional()
            ),
            sewageCardMapper.map(
                sewagePlant = survey.sewagePlant,
                sewagePlantTypes = references[ReferenceType.TREATMENT_FACILITIES].orEmpty(),
                isChecked = checkedSurvey.sewagePlant,
                sewagePlantOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .sewagePlant
                    .asOptional()
            ),
            radiationControlCardMapper.map(
                radiationControl = survey.radiationControl,
                isChecked = checkedSurvey.radiationControl,
                radiationControlOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .radiationControl
                    .asOptional()
            ),
            videoControlCardMapper.map(
                securityCamera = survey.securityCamera,
                isChecked = checkedSurvey.securityCamera,
                securityCameraOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .securityCamera
                    .asOptional()
            ),
            roadsCardMapper.map(
                roads = survey.roadNetwork,
                roadCoverTypes = references[ReferenceType.ROAD_SURFACE_TYPE].orEmpty(),
                isChecked = checkedSurvey.roadNetwork,
                roadsOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .roadNetwork
                    .asOptional()
            ),
            fencesCardMapper.map(
                fences = survey.fences,
                fenceTypes = references[ReferenceType.FENCE_FUNCTIONAL].orEmpty(),
                isChecked = checkedSurvey.fences,
                fencesOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .fences
                    .asOptional()
            ),
            lightsCardMapper.map(
                lights = survey.lightSystem,
                lightTypes = references[ReferenceType.LIGHTS_FUNCTIONAL_TYPE].orEmpty(),
                isChecked = checkedSurvey.lightSystem,
                lightsOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .lightSystem
                    .asOptional()
            ),
            securityCardMapper.map(
                securityStation = survey.securityStation,
                securitySources = references[ReferenceType.SECURITY_SOURCE].orEmpty(),
                isChecked = checkedSurvey.securityStation,
                securityOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .securityStation
                    .asOptional()
            ),
            asuCardMapper.map(
                asu = survey.asu,
                isChecked = checkedSurvey.asu,
                asuOptics = InfrastructureSurveyDraft.infrastructureSurvey
                    .asu
                    .asOptional()
            ),
            survey.environmentMonitoring?.let { environmentMonitoring ->
                environmentMonitoringCardMapper.map(
                    environmentMonitoring = environmentMonitoring,
                    environmentMonitoringSystemTypes = references[ReferenceType.ENVIRONMENT_MONITORING_SYSTEM]
                        .orEmpty(),
                    environmentOptics = InfrastructureSurveyDraft.infrastructureSurvey
                        .environmentMonitoring,
                    isChecked = checkedSurvey.environmentMonitoring.orFalse()
                )
            }
        ).flatten()

        val title = listOf(
            LargeTitleItem(
                text = resources.getString(R.string.survey_infrastructure_title),
                isNested = true
            )
        )

        return title + surveys
    }
}
