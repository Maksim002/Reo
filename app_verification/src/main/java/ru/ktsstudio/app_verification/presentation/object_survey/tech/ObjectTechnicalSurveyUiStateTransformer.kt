package ru.ktsstudio.app_verification.presentation.object_survey.tech

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDataType
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.technicalCheckedSurvey
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.technicalSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.presentation.object_survey.tech.card_mappers.WastePlacementMapUiMapper
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledCommentWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledSelectorWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.mainEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.receivedWastes
import ru.ktsstudio.core_data_verfication_api.data.model.technical.secondaryEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.wasteDisposalTechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.wasteDisposalTechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.wasteRecyclingTechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.wasteRecyclingTechnicalSurvey
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * @author Maxim Ovchinnikov on 07.12.2020.
 */
internal class ObjectTechnicalSurveyUiStateTransformer(
    private val resources: ResourceManager
) : (
    Pair<ObjectSurveyFeature.State<TechnicalSurveyDraft>, ReferenceFeature.State>
) -> ObjectTechnicalSurveyUiState {

    private val wastePlacementMapUiMapper = WastePlacementMapUiMapper(resources)

    override fun invoke(
        surveyAndReferences: Pair<ObjectSurveyFeature.State<TechnicalSurveyDraft>, ReferenceFeature.State>
    ): ObjectTechnicalSurveyUiState {
        val surveyState: ObjectSurveyFeature.State<TechnicalSurveyDraft> = surveyAndReferences.first
        val referenceFeatureState = surveyAndReferences.second
        val references = referenceFeatureState.references.groupBy { it.type }

        return ObjectTechnicalSurveyUiState(
            loading = surveyState.loading || referenceFeatureState.loading,
            error = surveyState.error ?: referenceFeatureState.error,
            data = surveyState.draft?.let { surveyDraft ->
                createAdapterItems(surveyDraft, references)
            } ?: emptyList()
        )
    }

    private fun createAdapterItems(
        surveyDraft: TechnicalSurveyDraft,
        references: Map<ReferenceType, List<Reference>>
    ): List<Any> {
        return listOf(LargeTitleItem(resources.getString(R.string.survey_technical_title))) +
            when (surveyDraft.technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> getWasteTreatmentTechnicalSurveyAdapterItems(
                    surveyDraft.technicalSurvey,
                    surveyDraft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> getWastePlacementTechnicalSurveyAdapterItems(
                    surveyDraft.technicalSurvey,
                    surveyDraft.technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey,
                    references
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> getWasteRecyclingTechnicalSurveyAdapterItems(
                    surveyDraft,
                    surveyDraft.technicalSurvey,
                    surveyDraft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey,
                    references
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> getWasteDisposalTechnicalSurveyAdapterItems(
                    surveyDraft,
                    surveyDraft.technicalSurvey,
                    surveyDraft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey,
                    references
                )
            }
    }

    private fun getWasteTreatmentTechnicalSurveyAdapterItems(
        technicalSurvey: TechnicalSurvey.WasteTreatmentTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey
    ): List<Any> {
        return listOf(
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_tko_weight_for_last_year),
                checkableValueConsumer = TechnicalSurveyDataType.TkoWeightForLastYearType(
                    isChecked = checkedSurvey.tkoWeightForLastYear,
                    value = technicalSurvey.tkoWeightForLastYear?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_weight_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_other_wastes_weight_for_last_year),
                checkableValueConsumer = TechnicalSurveyDataType.OtherWastesWeightForLastYearType(
                    isChecked = checkedSurvey.otherWastesWeightForLastYear,
                    value = technicalSurvey.otherWastesWeightForLastYear?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_weight_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_object_area),
                checkableValueConsumer = TechnicalSurveyDataType.ObjectAreaType(
                    isChecked = checkedSurvey.objectArea,
                    value = technicalSurvey.objectArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_object_photos),
                valueConsumer = TechnicalSurveyDataType.ObjectPhotosType(
                    value = technicalSurvey.objectPhotos
                ),
                identifier = TechnicalSurveyDataType.ObjectPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_production_area),
                checkableValueConsumer = TechnicalSurveyDataType.ProductionAreaType(
                    isChecked = checkedSurvey.productionArea,
                    value = technicalSurvey.productionArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_photos),
                valueConsumer = TechnicalSurveyDataType.ProductionPhotosType(
                    value = technicalSurvey.productionPhotos
                ),
                identifier = TechnicalSurveyDataType.ProductionPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_waste_unloading_area),
                checkableValueConsumer = TechnicalSurveyDataType.WasteUnloadingAreaType(
                    isChecked = checkedSurvey.wasteUnloadingArea,
                    value = technicalSurvey.wasteUnloadingArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_sort_department_area),
                checkableValueConsumer = TechnicalSurveyDataType.SortDepartmentAreaType(
                    isChecked = checkedSurvey.sortDepartmentArea,
                    value = technicalSurvey.sortDepartmentArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            )
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_has_compost_area),
            checkableValueConsumer = TechnicalSurveyDataType.HasCompostAreaType(
                isChecked = checkedSurvey.compostArea,
                value = technicalSurvey.hasCompostArea
            ),
            positiveItems = listOf(
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_area_power),
                    valueConsumer = TechnicalSurveyDataType.CompostAreaPowerType(
                        value = technicalSurvey.compostAreaPower?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_power_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_material),
                    valueConsumer = TechnicalSurveyDataType.CompostMaterialType(
                        value = technicalSurvey.compostMaterial
                    ),
                    editHint = resources.getString(R.string.survey_material_hint),
                    inputFormat = TextFormat.Text
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_purpose),
                    valueConsumer = TechnicalSurveyDataType.CompostPurposeType(
                        value = technicalSurvey.compostPurpose
                    ),
                    editHint = resources.getString(R.string.survey_technical_compost_purpose),
                    inputFormat = TextFormat.Text
                )
            ),
            negativeItems = listOf(
                InnerLabeledComment(
                    label = resources.getString(R.string.survey_reason_label),
                    valueConsumer = TechnicalSurveyDataType.NoCompostAreaReasonType(
                        value = technicalSurvey.noCompostAreaReason
                    ),
                    editHint = resources.getString(R.string.survey_reason_hint),
                    inputFormat = TextFormat.Text
                )
            )
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_has_rdf_area),
            checkableValueConsumer = TechnicalSurveyDataType.HasRdfAreaType(
                isChecked = checkedSurvey.rdfArea,
                value = technicalSurvey.hasRdfArea
            ),
            positiveItems = listOf(
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_rdf_power),
                    valueConsumer = TechnicalSurveyDataType.RdfPowerType(
                        value = technicalSurvey.rdfPower?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_power_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_rdf_purpose),
                    valueConsumer = TechnicalSurveyDataType.RdfPurposeType(
                        value = technicalSurvey.rdfPurpose
                    ),
                    editHint = resources.getString(R.string.survey_purpose_hint),
                    inputFormat = TextFormat.Text
                )
            )
        ) + listOf(
            SubtitleItemWithCheck(
                title = resources.getString(R.string.survey_technical_scheme_photos),
                isNested = false,
                withAccent = true,
                bottomPadding = null,
                checkableValueConsumer = TechnicalSurveyDataType.SchemePhotosType(checkedSurvey.schemePhotos)
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_tech_schema),
                valueConsumer = TechnicalSurveyDataType.TechSchemaType(
                    value = technicalSurvey.techSchema
                ),
                identifier = TechnicalSurveyDataType.TechSchemaType::class.qualifiedName
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_general_schema),
                valueConsumer = TechnicalSurveyDataType.GeneralSchemaType(
                    value = technicalSurvey.generalSchema
                ),
                identifier = TechnicalSurveyDataType.GeneralSchemaType::class.qualifiedName
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_schema),
                valueConsumer = TechnicalSurveyDataType.ProductionSchemaType(
                    value = technicalSurvey.productionSchema
                ),
                identifier = TechnicalSurveyDataType.ProductionSchemaType::class.qualifiedName
            )
        )
    }

    private fun getWastePlacementTechnicalSurveyAdapterItems(
        technicalSurvey: TechnicalSurvey.WastePlacementTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey,
        references: Map<ReferenceType, List<Reference>>
    ): List<Any> {
        val waterproofingTypeReferences = references[ReferenceType.WATERPROOFING_TYPE].orEmpty()
        return listOf(
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_tko_weight_for_last_year),
                checkableValueConsumer = TechnicalSurveyDataType.TkoWeightForLastYearType(
                    isChecked = checkedSurvey.tkoWeightForLastYear,
                    value = technicalSurvey.tkoWeightForLastYear?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_weight_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_other_wastes_weight_for_last_year),
                checkableValueConsumer = TechnicalSurveyDataType.OtherWastesWeightForLastYearType(
                    isChecked = checkedSurvey.otherWastesWeightForLastYear,
                    value = technicalSurvey.otherWastesWeightForLastYear?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_weight_hint),
                inputFormat = TextFormat.NumberDecimal()
            )
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_has_compost_area),
            checkableValueConsumer = TechnicalSurveyDataType.HasCompostAreaType(
                isChecked = checkedSurvey.compostArea,
                value = technicalSurvey.hasCompostArea
            ),
            positiveItems = listOf(
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_area_power),
                    valueConsumer = TechnicalSurveyDataType.CompostAreaPowerType(
                        value = technicalSurvey.compostAreaPower?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_power_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_material),
                    valueConsumer = TechnicalSurveyDataType.CompostMaterialType(
                        value = technicalSurvey.compostMaterial
                    ),
                    editHint = resources.getString(R.string.survey_material_hint),
                    inputFormat = TextFormat.Text
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_compost_purpose),
                    valueConsumer = TechnicalSurveyDataType.CompostPurposeType(
                        value = technicalSurvey.compostPurpose
                    ),
                    editHint = resources.getString(R.string.survey_technical_compost_purpose),
                    inputFormat = TextFormat.Text
                )
            ),
            negativeItems = listOf(
                InnerLabeledComment(
                    label = resources.getString(R.string.survey_reason_label),
                    valueConsumer = TechnicalSurveyDataType.NoCompostAreaReasonType(
                        value = technicalSurvey.noCompostAreaReason
                    ),
                    editHint = resources.getString(R.string.survey_reason_hint),
                    inputFormat = TextFormat.Text
                )
            )
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_has_rdf_area),
            checkableValueConsumer = TechnicalSurveyDataType.HasRdfAreaType(
                isChecked = checkedSurvey.rdfArea,
                value = technicalSurvey.hasRdfArea
            ),
            positiveItems = listOf(
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_rdf_power),
                    valueConsumer = TechnicalSurveyDataType.RdfPowerType(
                        value = technicalSurvey.rdfPower?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_power_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_rdf_purpose),
                    valueConsumer = TechnicalSurveyDataType.RdfPurposeType(
                        value = technicalSurvey.rdfPurpose
                    ),
                    editHint = resources.getString(R.string.survey_purpose_hint),
                    inputFormat = TextFormat.Text
                )
            )
        ) + listOf(
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_object_area),
                checkableValueConsumer = TechnicalSurveyDataType.ObjectAreaType(
                    isChecked = checkedSurvey.objectArea,
                    value = technicalSurvey.objectArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_object_photos),
                valueConsumer = TechnicalSurveyDataType.ObjectPhotosType(
                    value = technicalSurvey.objectPhotos
                ),
                identifier = TechnicalSurveyDataType.ObjectPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_production_area),
                checkableValueConsumer = TechnicalSurveyDataType.ProductionAreaType(
                    isChecked = checkedSurvey.productionArea,
                    value = technicalSurvey.productionArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_photos),
                valueConsumer = TechnicalSurveyDataType.ProductionPhotosType(
                    value = technicalSurvey.productionPhotos
                ),
                identifier = TechnicalSurveyDataType.ProductionPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_object_body_area),
                checkableValueConsumer = TechnicalSurveyDataType.ObjectBodyAreaType(
                    isChecked = checkedSurvey.objectBodyArea,
                    value = technicalSurvey.objectBodyArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_polygon_height),
                checkableValueConsumer = TechnicalSurveyDataType.PolygonHeightType(
                    isChecked = checkedSurvey.polygonHeight,
                    value = technicalSurvey.polygonHeight?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_height_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            getSelectorItem(
                label = resources.getString(R.string.survey_technical_waterproofing_type),
                hint = resources.getString(R.string.survey_technical_waterproofing_type_hint),
                selectedReference = technicalSurvey.waterproofingType,
                references = waterproofingTypeReferences,
                checkableValueConsumer = TechnicalSurveyDataType.WaterproofingTypeType(
                    value = technicalSurvey.waterproofingType?.id,
                    isChecked = checkedSurvey.waterproofingType,
                    getReference = { selectedTypeId ->
                        waterproofingTypeReferences.find { it.id == selectedTypeId }
                    }
                )
            ),
            LabeledCommentWithCheck(
                label = resources.getString(R.string.survey_technical_relief),
                checkableValueConsumer = TechnicalSurveyDataType.ReliefType(
                    isChecked = checkedSurvey.relief,
                    value = technicalSurvey.relief
                ),
                editHint = resources.getString(R.string.survey_relief_hint),
                inputFormat = TextFormat.Text
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_groundwater_depth),
                checkableValueConsumer = TechnicalSurveyDataType.GroundwaterDepthType(
                    isChecked = checkedSurvey.groundwaterDepth,
                    value = technicalSurvey.groundWaterDepth?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_technical_groundwater_depth_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            EmptySpace(isNested = false)
        ) + wastePlacementMapUiMapper.map(
            isChecked = checkedSurvey.wastePlacementMap,
            items = technicalSurvey.wastePlacementMaps.values.toList()
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_can_flooding),
            checkableValueConsumer = TechnicalSurveyDataType.CanFloodingType(
                isChecked = checkedSurvey.canFlooding,
                value = technicalSurvey.canFlooding
            )
        ) + getInnerSurveyItem(
            label = resources.getString(R.string.survey_technical_has_waste_sealant),
            checkableValueConsumer = TechnicalSurveyDataType.HasWasteSealantType(
                isChecked = checkedSurvey.wasteSealant,
                value = technicalSurvey.hasWasteSealant
            ),
            positiveItems = listOf(
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_sealant_type),
                    valueConsumer = TechnicalSurveyDataType.SealantType(
                        value = technicalSurvey.sealantType
                    ),
                    editHint = resources.getString(R.string.survey_technical_sealant_type_hint),
                    inputFormat = TextFormat.Text
                ),
                InnerLabeledEditItem(
                    label = resources.getString(R.string.survey_technical_sealant_weight),
                    valueConsumer = TechnicalSurveyDataType.SealantWeight(
                        value = technicalSurvey.sealantWeight?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_technical_sealant_weight_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_sealant_photos),
                    valueConsumer = TechnicalSurveyDataType.SealantPhotos(
                        value = technicalSurvey.sealantPhotos
                    ),
                    identifier = TechnicalSurveyDataType.SealantPhotos::class.qualifiedName
                )
            )
        ) + listOf(
            SubtitleItemWithCheck(
                title = resources.getString(R.string.survey_technical_scheme_photos),
                isNested = false,
                withAccent = true,
                checkableValueConsumer = TechnicalSurveyDataType.SchemePhotosType(checkedSurvey.schemePhotos)
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_tech_schema),
                valueConsumer = TechnicalSurveyDataType.TechSchemaType(
                    value = technicalSurvey.techSchema
                ),
                identifier = TechnicalSurveyDataType.TechSchemaType::class.qualifiedName
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_general_schema),
                valueConsumer = TechnicalSurveyDataType.GeneralSchemaType(
                    value = technicalSurvey.generalSchema
                ),
                identifier = TechnicalSurveyDataType.GeneralSchemaType::class.qualifiedName
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_schema),
                valueConsumer = TechnicalSurveyDataType.ProductionSchemaType(
                    value = technicalSurvey.productionSchema
                ),
                identifier = TechnicalSurveyDataType.ProductionSchemaType::class.qualifiedName
            )
        )
    }

    private fun getWasteRecyclingTechnicalSurveyAdapterItems(
        draft: TechnicalSurveyDraft,
        technicalSurvey: TechnicalSurvey.WasteRecyclingTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey,
        references: Map<ReferenceType, List<Reference>>
    ): List<Any> {
        val recyclingTypeReferences = references[ReferenceType.UTILIZATION_TYPE].orEmpty()
        return listOf(
            getSelectorItem(
                label = resources.getString(R.string.survey_technical_recycling_type),
                hint = resources.getString(R.string.survey_technical_recycling_type_hint),
                selectedReference = technicalSurvey.recyclingType,
                references = recyclingTypeReferences,
                checkableValueConsumer = TechnicalSurveyDataType.RecyclingTypeType(
                    value = technicalSurvey.recyclingType?.id,
                    isChecked = checkedSurvey.recyclingType,
                    getReference = { selectedTypeId ->
                        recyclingTypeReferences.find { it.id == selectedTypeId }
                    }
                )
            ),
            LabeledCommentWithCheck(
                label = resources.getString(R.string.survey_technical_tech_process_comment),
                checkableValueConsumer = TechnicalSurveyDataType.TechProcessCommentType(
                    isChecked = checkedSurvey.techProcessComment,
                    value = technicalSurvey.techProcessComment
                ),
                editHint = resources.getString(R.string.survey_technical_tech_process_comment),
                inputFormat = TextFormat.Text
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_object_area),
                checkableValueConsumer = TechnicalSurveyDataType.ObjectAreaType(
                    isChecked = checkedSurvey.objectArea,
                    value = technicalSurvey.objectArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_technical_object_area),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_object_photos),
                valueConsumer = TechnicalSurveyDataType.ObjectPhotosType(
                    value = technicalSurvey.objectPhotos
                ),
                identifier = TechnicalSurveyDataType.ObjectPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_production_area),
                checkableValueConsumer = TechnicalSurveyDataType.ProductionAreaType(
                    isChecked = checkedSurvey.productionArea,
                    value = technicalSurvey.productionArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_photos),
                valueConsumer = TechnicalSurveyDataType.ProductionPhotosType(
                    value = technicalSurvey.productionPhotos
                ),
                identifier = TechnicalSurveyDataType.ProductionPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_waste_unloading_area),
                checkableValueConsumer = TechnicalSurveyDataType.WasteUnloadingAreaType(
                    isChecked = checkedSurvey.wasteUnloadingArea,
                    value = technicalSurvey.wasteUnloadingArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            )
        ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_main_production_equipment),
                checkableValueConsumer = TechnicalSurveyDataType.HasMainEquipment(
                    isChecked = checkedSurvey.mainEquipment,
                    value = technicalSurvey.hasMainEquipment
                ),
                positiveItems = addTechnicalEquipment(
                    TechnicalCardType.MAIN_EQUIPMENT,
                    technicalSurvey.mainEquipment.values,
                    TechnicalSurveyDraft.technicalSurvey.wasteRecyclingTechnicalSurvey.mainEquipment,
                    resources,
                    references[ReferenceType.MAIN_EQUIPMENT_TYPE]
                )
            ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_support_production_equipment),
                checkableValueConsumer = TechnicalSurveyDataType.HasSecondaryEquipment(
                    isChecked = checkedSurvey.secondaryEquipment,
                    value = technicalSurvey.hasSecondaryEquipment
                ),
                positiveItems = addTechnicalEquipment(
                    TechnicalCardType.SECONDARY_EQUIPMENT,
                    technicalSurvey.secondaryEquipment.values,
                    TechnicalSurveyDraft.technicalSurvey.wasteRecyclingTechnicalSurvey.secondaryEquipment,
                    resources,
                    references[ReferenceType.SECONDARY_EQUIPMENT_TYPE]
                )
            ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_temporary_waste_accumulation),
                checkableValueConsumer = TechnicalSurveyDataType.HasTemporaryWasteAccumulationType(
                    isChecked = checkedSurvey.temporaryWasteAccumulation,
                    value = technicalSurvey.hasTemporaryWasteAccumulation
                ),
                positiveItems = listOf(
                    InnerLabeledEditItem(
                        label = resources.getString(R.string.survey_technical_temporary_waste_area),
                        valueConsumer = TechnicalSurveyDataType.TemporaryWasteAreaType(
                            value = technicalSurvey.temporaryWasteArea?.stringValue()
                        ),
                        editHint = resources.getString(R.string.survey_area_hint),
                        inputFormat = TextFormat.NumberDecimal()
                    ),
                    InnerLabeledComment(
                        label = resources.getString(R.string.survey_technical_temporary_wastes),
                        valueConsumer = TechnicalSurveyDataType.TemporaryWastesType(
                            value = technicalSurvey.temporaryWastes
                        ),
                        editHint = resources.getString(R.string.survey_area_wastes_hint),
                        inputFormat = TextFormat.Text
                    )
                )
            ) +
            listOf(
                SubtitleItemWithCheck(
                    title = resources.getString(R.string.survey_technical_scheme_photos),
                    isNested = false,
                    withAccent = true,
                    checkableValueConsumer = TechnicalSurveyDataType.SchemePhotosType(checkedSurvey.schemePhotos)
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_tech_schema),
                    valueConsumer = TechnicalSurveyDataType.TechSchemaType(
                        value = technicalSurvey.techSchema
                    ),
                    identifier = TechnicalSurveyDataType.TechSchemaType::class.qualifiedName
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_general_schema),
                    valueConsumer = TechnicalSurveyDataType.GeneralSchemaType(
                        value = technicalSurvey.generalSchema
                    ),
                    identifier = TechnicalSurveyDataType.GeneralSchemaType::class.qualifiedName
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_production_schema),
                    valueConsumer = TechnicalSurveyDataType.ProductionSchemaType(
                        value = technicalSurvey.productionSchema
                    ),
                    identifier = TechnicalSurveyDataType.ProductionSchemaType::class.qualifiedName
                ),
                EmptySpace(isNested = false)
            ) + addNestedReceivedWasteTypes(
                initCheckedState = TechnicalSurveyDraft.technicalCheckedSurvey
                    .wasteRecyclingTechnicalCheckedSurvey
                    .receivedWastes
                    .getOption(draft)
                    .orNull()
                    .orFalse(),
                selectedTypes = TechnicalSurveyDraft.technicalSurvey
                    .wasteRecyclingTechnicalSurvey
                    .receivedWastes
                    .getOption(draft)
                    .orNull()
                    .orEmpty(),
                listUpdaterOptics = TechnicalSurveyDraft.technicalSurvey
                    .wasteRecyclingTechnicalSurvey
                    .receivedWastes,
                checkUpdaterOptics = TechnicalSurveyDraft.technicalCheckedSurvey
                    .wasteRecyclingTechnicalCheckedSurvey
                    .receivedWastes,
                types = references[ReferenceType.WASTE_CATEGORY].orEmpty(),
                resources = resources
            ) +
            listOf(
                EmptySpace(isNested = false),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_technical_received_wastes_weight_this_year),
                    checkableValueConsumer = TechnicalSurveyDataType.ReceivedWastesWeightThisYearType(
                        isChecked = checkedSurvey.receivedWastesWeightThisYear,
                        value = technicalSurvey.receivedWastesWeightThisYear?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_weight_hint),
                    inputFormat = TextFormat.NumberDecimal()
                ),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_technical_received_wastes_weight_last_year),
                    checkableValueConsumer = TechnicalSurveyDataType.ReceivedWastesWeightLastYearType(
                        isChecked = checkedSurvey.receivedWastesWeightLastYear,
                        value = technicalSurvey.receivedWastesWeightLastYear?.stringValue()
                    ),
                    editHint = resources.getString(R.string.survey_weight_hint),
                    inputFormat = TextFormat.NumberDecimal()
                )
            )
    }

    private fun getWasteDisposalTechnicalSurveyAdapterItems(
        draft: TechnicalSurveyDraft,
        technicalSurvey: TechnicalSurvey.WasteDisposalTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey,
        references: Map<ReferenceType, List<Reference>>
    ): List<Any> {
        return listOf(
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_object_area),
                checkableValueConsumer = TechnicalSurveyDataType.ObjectAreaType(
                    isChecked = checkedSurvey.objectArea,
                    value = technicalSurvey.objectArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_object_photos),
                valueConsumer = TechnicalSurveyDataType.ObjectPhotosType(
                    value = technicalSurvey.objectPhotos
                ),
                identifier = TechnicalSurveyDataType.ObjectPhotosType::class.qualifiedName
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_production_area),
                checkableValueConsumer = TechnicalSurveyDataType.ProductionAreaType(
                    isChecked = checkedSurvey.productionArea,
                    value = technicalSurvey.productionArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            ),
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_technical_production_photos),
                valueConsumer = TechnicalSurveyDataType.ProductionPhotosType(
                    value = technicalSurvey.productionPhotos
                ),
                identifier = TechnicalSurveyDataType.ProductionPhotosType::class.qualifiedName
            ),
            LabeledCommentWithCheck(
                label = resources.getString(R.string.survey_technical_tech_process_comment),
                checkableValueConsumer = TechnicalSurveyDataType.TechProcessCommentType(
                    isChecked = checkedSurvey.techProcessComment,
                    value = technicalSurvey.techProcessComment
                ),
                editHint = resources.getString(R.string.survey_tech_process_hint),
                inputFormat = TextFormat.Text
            ),
            LabeledEditItemWithCheck(
                label = resources.getString(R.string.survey_technical_waste_unloading_area),
                checkableValueConsumer = TechnicalSurveyDataType.WasteUnloadingAreaType(
                    isChecked = checkedSurvey.wasteUnloadingArea,
                    value = technicalSurvey.wasteUnloadingArea?.stringValue()
                ),
                editHint = resources.getString(R.string.survey_area_hint),
                inputFormat = TextFormat.NumberDecimal()
            )
        ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_main_production_equipment),
                checkableValueConsumer = TechnicalSurveyDataType.HasMainEquipment(
                    isChecked = checkedSurvey.mainEquipment,
                    value = technicalSurvey.hasMainEquipment
                ),
                positiveItems = addTechnicalEquipment(
                    TechnicalCardType.MAIN_EQUIPMENT,
                    technicalSurvey.mainEquipment.values,
                    TechnicalSurveyDraft.technicalSurvey.wasteDisposalTechnicalSurvey.mainEquipment,
                    resources,
                    references[ReferenceType.MAIN_EQUIPMENT_TYPE]
                )
            ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_support_production_equipment),
                checkableValueConsumer = TechnicalSurveyDataType.HasSecondaryEquipment(
                    isChecked = checkedSurvey.secondaryEquipment,
                    value = technicalSurvey.hasSecondaryEquipment
                ),
                positiveItems = addTechnicalEquipment(
                    TechnicalCardType.SECONDARY_EQUIPMENT,
                    technicalSurvey.secondaryEquipment.values,
                    TechnicalSurveyDraft.technicalSurvey.wasteDisposalTechnicalSurvey.secondaryEquipment,
                    resources,
                    references[ReferenceType.SECONDARY_EQUIPMENT_TYPE]
                )
            ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_thermal_energy_production),
                checkableValueConsumer = TechnicalSurveyDataType.HasThermalEnergyProductionType(
                    isChecked = checkedSurvey.thermalEnergyProduction,
                    value = technicalSurvey.hasThermalEnergyProduction
                ),
                positiveItems = listOf(
                    InnerLabeledEditItem(
                        label = resources.getString(R.string.survey_technical_thermal_energy_production_power),
                        valueConsumer = TechnicalSurveyDataType.ThermalEnergyProductionPowerType(
                            value = technicalSurvey.thermalEnergyProductionPower?.stringValue()
                        ),
                        editHint = resources.getString(R.string.survey_power_hint),
                        inputFormat = TextFormat.NumberDecimal()
                    )
                )
            ) +
            getInnerSurveyItem(
                label = resources.getString(R.string.survey_technical_has_temporary_waste_accumulation),
                checkableValueConsumer = TechnicalSurveyDataType.HasTemporaryWasteAccumulationType(
                    isChecked = checkedSurvey.temporaryWasteAccumulation,
                    value = technicalSurvey.hasTemporaryWasteAccumulation
                ),
                positiveItems = listOf(
                    InnerLabeledEditItem(
                        label = resources.getString(R.string.survey_technical_temporary_waste_area),
                        valueConsumer = TechnicalSurveyDataType.TemporaryWasteAreaType(
                            value = technicalSurvey.temporaryWasteArea?.stringValue()
                        ),
                        editHint = resources.getString(R.string.survey_area_hint),
                        inputFormat = TextFormat.NumberDecimal()
                    ),
                    InnerLabeledComment(
                        label = resources.getString(R.string.survey_technical_temporary_wastes),
                        valueConsumer = TechnicalSurveyDataType.TemporaryWastesType(
                            value = technicalSurvey.temporaryWastes
                        ),
                        editHint = resources.getString(R.string.survey_area_wastes_hint),
                        inputFormat = TextFormat.Text
                    )
                )
            ) +
            listOf(
                SubtitleItemWithCheck(
                    title = resources.getString(R.string.survey_technical_scheme_photos),
                    isNested = false,
                    withAccent = true,
                    checkableValueConsumer = TechnicalSurveyDataType.SchemePhotosType(checkedSurvey.schemePhotos)
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_tech_schema),
                    valueConsumer = TechnicalSurveyDataType.TechSchemaType(
                        value = technicalSurvey.techSchema
                    ),
                    identifier = TechnicalSurveyDataType.TechSchemaType::class.qualifiedName
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_general_schema),
                    valueConsumer = TechnicalSurveyDataType.GeneralSchemaType(
                        value = technicalSurvey.generalSchema
                    ),
                    identifier = TechnicalSurveyDataType.GeneralSchemaType::class.qualifiedName
                ),
                LabeledMediaListItem(
                    label = resources.getString(R.string.survey_technical_production_schema),
                    valueConsumer = TechnicalSurveyDataType.ProductionSchemaType(
                        value = technicalSurvey.productionSchema
                    ),
                    identifier = TechnicalSurveyDataType.ProductionSchemaType::class.qualifiedName
                ),
                EmptySpace(isNested = false)
            ) + addNestedReceivedWasteTypes(
                initCheckedState = TechnicalSurveyDraft.technicalCheckedSurvey
                    .wasteDisposalTechnicalCheckedSurvey
                    .receivedWastes
                    .getOption(draft)
                    .orNull()
                    .orFalse(),
                selectedTypes = TechnicalSurveyDraft.technicalSurvey
                    .wasteDisposalTechnicalSurvey
                    .receivedWastes
                    .getOption(draft)
                    .orNull()
                    .orEmpty(),
                listUpdaterOptics = TechnicalSurveyDraft.technicalSurvey
                    .wasteDisposalTechnicalSurvey
                    .receivedWastes,
                checkUpdaterOptics = TechnicalSurveyDraft.technicalCheckedSurvey
                    .wasteDisposalTechnicalCheckedSurvey
                    .receivedWastes,
                types = references[ReferenceType.WASTE_CATEGORY].orEmpty(),
                resources = resources
            )
    }

    private fun getSelectorItem(
        label: String,
        hint: String,
        selectedReference: Reference?,
        references: List<Reference>,
        checkableValueConsumer: CheckableValueConsumer<String?, *>
    ): Any {
        val selectedType = references.find { it.id == selectedReference?.id }
        return LabeledSelectorWithCheck(
            label = label,
            hint = hint,
            selectedTitle = selectedType?.name,
            items = references.map {
                ReferenceUi(
                    id = it.id,
                    title = it.name,
                    isSelected = it.id == selectedType?.id
                )
            },
            valueConsumer = checkableValueConsumer
        )
    }

    private fun getInnerSurveyItem(
        label: String,
        checkableValueConsumer: CheckableValueConsumer<Boolean, *>,
        positiveItems: List<Any> = emptyList(),
        negativeItems: List<Any> = emptyList()
    ): List<Any> {
        return listOf(
            InnerLabeledSurveyWithCheck(
                label = label,
                checkableValueConsumer = checkableValueConsumer,
                backgroundColor = if (positiveItems.isEmpty() && negativeItems.isEmpty()) {
                    android.R.color.white
                } else {
                    R.color.background_100
                }
            )
        ) + checkableValueConsumer.get().let { showPositiveItems ->
            val items = if (showPositiveItems) positiveItems else negativeItems
            items.takeIfNotEmpty()?.let {
                it + EmptySpace(isNested = true)
            }
        }
    }

    private operator fun List<Any>.plus(element: Any?): List<Any> {
        return element?.let {
            if (it is List<*>) {
                ArrayList<Any>(this).apply {
                    addAll(it as List<Any>)
                }
            } else {
                this.plusElement(it)
            }
        } ?: this
    }
}
