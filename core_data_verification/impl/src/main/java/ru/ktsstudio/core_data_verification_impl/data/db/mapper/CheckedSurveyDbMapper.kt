package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.WorkScheduleCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableGeneralInformationCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableProductionCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableWorkScheduleCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class CheckedSurveyDbMapper @Inject constructor(
    private val infrastructureCheckedSurveyMapper: Mapper<SerializableInfrastructureCheckedSurvey, InfrastructureCheckedSurvey>
) : Mapper<SerializableCheckedSurvey, CheckedSurvey> {

    override fun map(item: SerializableCheckedSurvey): CheckedSurvey {
        return when (item) {
            is SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey -> {
                CheckedSurvey.WasteTreatmentCheckedSurvey(
                    generalCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalInformationCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey(
                        tkoWeightForLastYear = item.technicalCheckedSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalCheckedSurvey.otherWastesWeightForLastYear,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        sortDepartmentArea = item.technicalCheckedSurvey.sortDepartmentArea,
                        compostArea = item.technicalCheckedSurvey.hasCompostArea,
                        rdfArea = item.technicalCheckedSurvey.hasRdfArea,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = infrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    equipmentCheckedSurvey = CheckedSurvey.WasteTreatmentCheckedSurvey.EquipmentCheckedSurvey(
                        servingConveyors = item.equipmentCheckedSurvey.servingConveyors,
                        sortConveyors = item.equipmentCheckedSurvey.sortConveyors,
                        reverseConveyors = item.equipmentCheckedSurvey.reverseConveyors,
                        pressConveyors = item.equipmentCheckedSurvey.pressConveyors,
                        otherConveyors = item.equipmentCheckedSurvey.otherConveyors,
                        bagBreakers = item.equipmentCheckedSurvey.bagBreakers,
                        separators = item.equipmentCheckedSurvey.separators,
                        additionalEquipment = item.equipmentCheckedSurvey.additionalEquipment,
                        presses = item.equipmentCheckedSurvey.presses
                    ),
                    secondaryResourcesCheckedSurvey = SecondaryResourcesCheckedSurvey(
                        extractPercent = item.secondaryResourcesCheckedSurvey.extractPercent,
                        types = item.secondaryResourcesCheckedSurvey.types
                    )
                )
            }
            is SerializableCheckedSurvey.SerializableWastePlacementCheckedSurvey -> {
                CheckedSurvey.WastePlacementCheckedSurvey(
                    generalCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalInformationCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey(
                        tkoWeightForLastYear = item.technicalCheckedSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalCheckedSurvey.otherWastesWeightForLastYear,
                        compostArea = item.technicalCheckedSurvey.hasCompostArea,
                        rdfArea = item.technicalCheckedSurvey.hasRdfArea,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        objectBodyArea = item.technicalCheckedSurvey.objectBodyArea,
                        polygonHeight = item.technicalCheckedSurvey.polygonHeight,
                        waterproofingType = item.technicalCheckedSurvey.waterproofingType,
                        relief = item.technicalCheckedSurvey.relief,
                        groundwaterDepth = item.technicalCheckedSurvey.groundwaterDepth,
                        wastePlacementMap = item.technicalCheckedSurvey.wastePlacementMap,
                        canFlooding = item.technicalCheckedSurvey.canFlooding,
                        wasteSealant = item.technicalCheckedSurvey.hasWasteSealant,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = infrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    )
                )
            }
            is SerializableCheckedSurvey.SerializableWasteRecyclingCheckedSurvey -> {
                CheckedSurvey.WasteRecyclingCheckedSurvey(
                    generalCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalInformationCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey(
                        recyclingType = item.technicalCheckedSurvey.recyclingType,
                        techProcessComment = item.technicalCheckedSurvey.techProcessComment,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        temporaryWasteAccumulation = item.technicalCheckedSurvey.hasTemporaryWasteAccumulation,
                        receivedWastes = item.technicalCheckedSurvey.receivedWastes,
                        receivedWastesWeightThisYear = item.technicalCheckedSurvey.receivedWastesWeightThisYear,
                        receivedWastesWeightLastYear = item.technicalCheckedSurvey.receivedWastesWeightLastYear,
                        mainEquipment = item.technicalCheckedSurvey.hasMainEquipment,
                        secondaryEquipment = item.technicalCheckedSurvey.hasSecondaryEquipment,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = infrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    productionCheckedSurvey = mapProductionCheckedSurvey(item.productionCheckedSurvey)
                )
            }
            is SerializableCheckedSurvey.SerializableWasteDisposalCheckedSurvey -> {
                CheckedSurvey.WasteDisposalCheckedSurvey(
                    generalCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalInformationCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey(
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        techProcessComment = item.technicalCheckedSurvey.techProcessComment,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        thermalEnergyProduction = item.technicalCheckedSurvey.hasThermalEnergyProduction,
                        temporaryWasteAccumulation = item.technicalCheckedSurvey.hasTemporaryWasteAccumulation,
                        receivedWastes = item.technicalCheckedSurvey.receivedWastes,
                        mainEquipment = item.technicalCheckedSurvey.hasMainEquipment,
                        secondaryEquipment = item.technicalCheckedSurvey.hasSecondaryEquipment,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = infrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    productionCheckedSurvey = mapProductionCheckedSurvey(item.productionCheckedSurvey)
                )
            }
        }
    }

    private fun mapGeneralInformationCheckedSurvey(
        generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey
    ): GeneralCheckedSurvey = with(generalInformationCheckedSurvey) {
        return GeneralCheckedSurvey(
            name = objectName,
            objectStatus = objectStatus,
            subject = subject,
            fiasAddress = fiasAddress,
            addressDescription = addressDescription
        )
    }

    private fun mapWorkScheduleCheckedSurvey(
        serializableWorkScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey
    ): WorkScheduleCheckedSurvey {
        return WorkScheduleCheckedSurvey(
            schedule = serializableWorkScheduleCheckedSurvey.schedule,
            shiftsPerDayCount = serializableWorkScheduleCheckedSurvey.shiftsPerDayCount,
            workDaysPerYearCount = serializableWorkScheduleCheckedSurvey.daysPerYearCount,
            workplacesCount = serializableWorkScheduleCheckedSurvey.workplacesCount,
            managersCount = serializableWorkScheduleCheckedSurvey.managersCount,
            workersCount = serializableWorkScheduleCheckedSurvey.workersCount
        )
    }

    private fun mapProductionCheckedSurvey(
        survey: SerializableProductionCheckedSurvey
    ): ProductionCheckedSurvey = with(survey){
        return ProductionCheckedSurvey(
            productCapacity, products, services
        )
    }
}