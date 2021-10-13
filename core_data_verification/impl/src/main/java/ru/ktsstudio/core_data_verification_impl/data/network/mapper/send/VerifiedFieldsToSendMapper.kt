package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.VerifiedFieldsMapping
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getConveyorType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getIndexes
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getPressType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getSeparatorType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isAdditionalKind
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isOtherConveyorType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isOtherPress
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isOtherSeparator
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResources
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
class VerifiedFieldsToSendMapper @Inject constructor() {

    fun map(
        equipment: List<RemoteEquipment>,
        production: RemoteProductsInfo?,
        secondaryResources: RemoteSecondaryResources?,
        wastePlacement: List<RemoteWastePlacementMap>,
        receivedWasteTypePositions: List<Int>,
        checkedSurvey: CheckedSurvey,
        infrastructureSurvey: InfrastructureSurvey,
        techSurvey: TechnicalSurvey
    ): List<String> {
        return VerifiedFieldsMapping.GeneralInformation.getVerifiedValues(checkedSurvey.generalCheckedSurvey) +
            VerifiedFieldsMapping.WorkSchedule.getVerifiedValues(checkedSurvey.workScheduleCheckedSurvey) +
            getTechnicalVerifiedValues(
                techSurvey, checkedSurvey.technicalCheckedSurvey,
                wastePlacement,
                receivedWasteTypePositions,
                equipment
            ) +
            getInfrastructureValues(checkedSurvey, infrastructureSurvey) +
            getEquipmentValues(equipment, checkedSurvey) +
            getSecondaryResourceValues(checkedSurvey, secondaryResources) +
            getProductionSurveyValues(checkedSurvey, production)
    }

    private fun getTechnicalVerifiedValues(
        techSurvey: TechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey,
        wastePlacement: List<RemoteWastePlacementMap>,
        receivedWasteTypePositions: List<Int>,
        equipment: List<RemoteEquipment>
    ): List<String> {
        return when (checkedSurvey) {
            is TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey -> {
                techSurvey as TechnicalSurvey.WasteTreatmentTechnicalSurvey
                VerifiedFieldsMapping.Technical.getTreatmentVerifiedValues(
                    checkedSurvey,
                    techSurvey.hasCompostArea,
                    techSurvey.hasRdfArea
                )
            }
            is TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey -> {
                techSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey
                VerifiedFieldsMapping.Technical.getPlacementVerifiedValues(
                    checkedSurvey,
                    wastePlacement,
                    techSurvey.hasCompostArea,
                    techSurvey.hasRdfArea,
                    techSurvey.hasWasteSealant
                )
            }
            is TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey -> {
                techSurvey as TechnicalSurvey.WasteRecyclingTechnicalSurvey
                VerifiedFieldsMapping.Technical.getRecyclingVerifiedValues(
                    survey = checkedSurvey,
                    remoteEquipment = equipment,
                    hasMainEquipment = techSurvey.hasMainEquipment,
                    receivedWasteTypePositions = receivedWasteTypePositions,
                    hasSecondaryEquipment = techSurvey.hasSecondaryEquipment,
                    hasTemporaryWasteAccumulation = techSurvey.hasTemporaryWasteAccumulation
                )
            }
            is TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey -> {
                techSurvey as TechnicalSurvey.WasteDisposalTechnicalSurvey
                VerifiedFieldsMapping.Technical.getDisposalVerifiedValues(
                    survey = checkedSurvey,
                    receivedWasteTypePositions = receivedWasteTypePositions,
                    remoteEquipment = equipment,
                    hasTemporaryWasteAccumulation = techSurvey.hasTemporaryWasteAccumulation,
                    hasThermalEnergyProduction = techSurvey.hasThermalEnergyProduction,
                    hasMainEquipment = techSurvey.hasMainEquipment,
                    hasSecondaryEquipment = techSurvey.hasSecondaryEquipment
                )
            }

            else -> emptyList()
        }
    }

    private fun getInfrastructureValues(checkedSurvey: CheckedSurvey, survey: InfrastructureSurvey): List<String> {
        val infrastructureCheckedSurvey = when (checkedSurvey) {
            is CheckedSurvey.WasteTreatmentCheckedSurvey -> checkedSurvey.infrastructureCheckedSurvey
            is CheckedSurvey.WastePlacementCheckedSurvey -> checkedSurvey.infrastructureCheckedSurvey
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> checkedSurvey.infrastructureCheckedSurvey
            is CheckedSurvey.WasteDisposalCheckedSurvey -> checkedSurvey.infrastructureCheckedSurvey
        }
        return VerifiedFieldsMapping.Infrastructure.getVerifiedValues(infrastructureCheckedSurvey, survey)
    }

    private fun getEquipmentValues(
        equipment: List<RemoteEquipment>,
        checkedSurvey: CheckedSurvey
    ): List<String> {
        if (checkedSurvey !is CheckedSurvey.WasteTreatmentCheckedSurvey) return emptyList()

        val servingConveyorFields = equipment
            .getIndexes { it.type.getConveyorType() == ConveyorType.SERVING }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.ServingConveyorFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.servingConveyors }
            .orEmpty()

        val sortConveyorFields = equipment
            .getIndexes { it.type.getConveyorType() == ConveyorType.SORT }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.SortConveyorFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.sortConveyors }
            .orEmpty()

        val reverseConveyorFields = equipment
            .getIndexes { it.type.getConveyorType() == ConveyorType.REVERSE }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.CommonConveyorFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.reverseConveyors }
            .orEmpty()

        val pressConveyorFields = equipment
            .getIndexes { it.type.getConveyorType() == ConveyorType.PRESS }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.CommonConveyorFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.pressConveyors }
            .orEmpty()

        val otherConveyorFields = equipment
            .getIndexes { it.isOtherConveyorType() }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.CommonConveyorFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.otherConveyors }
            .orEmpty()

        val additionalEquipmentFields = equipment.getIndexes { it.kind.isAdditionalKind() }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.AdditionalEquipmentFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.additionalEquipment }
            .orEmpty()

        val bagBreakersFields = equipment
            .getIndexes { it.type.getConveyorType() == ConveyorType.BAG_BREAKER }
            .let {
                VerifiedFieldsMapping.getRequiredFieldsForPositions(
                    prefix = VerifiedFieldsMapping.Equipment.Prefix,
                    objectFields = VerifiedFieldsMapping.Equipment.BagBreakerFields,
                    positions = it
                )
            }
            .takeIf { checkedSurvey.equipmentCheckedSurvey.bagBreakers }
            .orEmpty()

        val pressFields = PressType.values()
            .flatMap { pressType ->
                equipment.getIndexes {
                    it.isOtherPress() || it.type.getPressType() == pressType
                }
                    .let {
                        VerifiedFieldsMapping.getRequiredFieldsForPositions(
                            prefix = VerifiedFieldsMapping.Equipment.Prefix,
                            objectFields = VerifiedFieldsMapping.Equipment.PressFields,
                            positions = it
                        )
                    }
                    .takeIf { checkedSurvey.equipmentCheckedSurvey.presses.contains(pressType) }
                    .orEmpty()
            }

        val separatorFields = SeparatorType.values()
            .flatMap { separatorType ->
                equipment.getIndexes {
                    it.isOtherSeparator() || it.type.getSeparatorType() == separatorType
                }
                    .let {
                        VerifiedFieldsMapping.getRequiredFieldsForPositions(
                            prefix = VerifiedFieldsMapping.Equipment.Prefix,
                            objectFields = VerifiedFieldsMapping.Equipment.SeparatorFields,
                            positions = it
                        )
                    }
                    .takeIf { checkedSurvey.equipmentCheckedSurvey.separators.contains(separatorType) }
                    .orEmpty()
            }

        return servingConveyorFields +
            sortConveyorFields +
            reverseConveyorFields +
            pressConveyorFields +
            otherConveyorFields +
            bagBreakersFields +
            pressFields +
            separatorFields +
            additionalEquipmentFields
    }

    private fun getSecondaryResourceValues(
        checkedSurvey: CheckedSurvey,
        secondaryResources: RemoteSecondaryResources?
    ): List<String> {
        return when (checkedSurvey) {
            is CheckedSurvey.WasteTreatmentCheckedSurvey -> VerifiedFieldsMapping.SecondaryResources
                .getVerifiedValues(
                    checkedSurvey.secondaryResourcesCheckedSurvey,
                    secondaryResources
                )
            else -> emptyList()
        }
    }

    private fun getProductionSurveyValues(
        checkedSurvey: CheckedSurvey,
        production: RemoteProductsInfo?,
    ): List<String> {
        if (production == null) return emptyList()
        return when (checkedSurvey) {
            is CheckedSurvey.WasteDisposalCheckedSurvey -> VerifiedFieldsMapping.Production
                .getVerifiedValues(
                    production,
                    checkedSurvey.productionCheckedSurvey
                )
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> VerifiedFieldsMapping.Production
                .getVerifiedValues(
                    production,
                    checkedSurvey.productionCheckedSurvey
                )
            else -> emptyList()
        }
    }
}