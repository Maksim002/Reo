package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.WorkScheduleCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableGeneralInformationCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableProductionCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableWorkScheduleCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class SerializableCheckedSurveyDbMapper @Inject constructor(
    private val serializableInfrastructureCheckedSurveyMapper: Mapper<
        InfrastructureCheckedSurvey,
        SerializableInfrastructureCheckedSurvey
        >
) : Mapper<CheckedSurvey, SerializableCheckedSurvey> {
    override fun map(item: CheckedSurvey): SerializableCheckedSurvey {
        return when (item) {
            is CheckedSurvey.WasteTreatmentCheckedSurvey -> {
                SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey(
                    generalInformationCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey.SerializableTechnicalCheckedSurvey(
                        tkoWeightForLastYear = item.technicalCheckedSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalCheckedSurvey.otherWastesWeightForLastYear,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        sortDepartmentArea = item.technicalCheckedSurvey.sortDepartmentArea,
                        hasCompostArea = item.technicalCheckedSurvey.compostArea,
                        hasRdfArea = item.technicalCheckedSurvey.rdfArea,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = serializableInfrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    equipmentCheckedSurvey = SerializableCheckedSurvey.SerializableWasteTreatmentCheckedSurvey.SerializableEquipmentCheckedSurvey(
                        servingConveyors = item.equipmentCheckedSurvey.servingConveyors,
                        sortConveyors = item.equipmentCheckedSurvey.sortConveyors,
                        reverseConveyors = item.equipmentCheckedSurvey.reverseConveyors,
                        pressConveyors = item.equipmentCheckedSurvey.pressConveyors,
                        otherConveyors = item.equipmentCheckedSurvey.otherConveyors,
                        bagBreakers = item.equipmentCheckedSurvey.bagBreakers,
                        separators = item.equipmentCheckedSurvey.separators,
                        presses = item.equipmentCheckedSurvey.presses,
                        additionalEquipment = item.equipmentCheckedSurvey.additionalEquipment
                    ),
                    secondaryResourcesCheckedSurvey = SerializableCheckedSurvey
                        .SerializableWasteTreatmentCheckedSurvey
                        .SerializableSecondaryResourcesCheckedSurvey(
                            extractPercent = item.secondaryResourcesCheckedSurvey.extractPercent,
                            types = item.secondaryResourcesCheckedSurvey.types
                        )
                )
            }
            is CheckedSurvey.WastePlacementCheckedSurvey -> {
                SerializableCheckedSurvey.SerializableWastePlacementCheckedSurvey(
                    generalInformationCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = SerializableCheckedSurvey.SerializableWastePlacementCheckedSurvey.SerializableTechnicalCheckedSurvey(
                        tkoWeightForLastYear = item.technicalCheckedSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalCheckedSurvey.otherWastesWeightForLastYear,
                        hasCompostArea = item.technicalCheckedSurvey.compostArea,
                        hasRdfArea = item.technicalCheckedSurvey.rdfArea,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        objectBodyArea = item.technicalCheckedSurvey.objectBodyArea,
                        polygonHeight = item.technicalCheckedSurvey.polygonHeight,
                        waterproofingType = item.technicalCheckedSurvey.waterproofingType,
                        relief = item.technicalCheckedSurvey.relief,
                        groundwaterDepth = item.technicalCheckedSurvey.groundwaterDepth,
                        wastePlacementMap = item.technicalCheckedSurvey.wastePlacementMap,
                        canFlooding = item.technicalCheckedSurvey.canFlooding,
                        hasWasteSealant = item.technicalCheckedSurvey.wasteSealant,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = serializableInfrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    )
                )
            }
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> {
                SerializableCheckedSurvey.SerializableWasteRecyclingCheckedSurvey(
                    generalInformationCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = SerializableCheckedSurvey.SerializableWasteRecyclingCheckedSurvey.SerializableTechnicalCheckedSurvey(
                        recyclingType = item.technicalCheckedSurvey.recyclingType,
                        techProcessComment = item.technicalCheckedSurvey.techProcessComment,
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        hasTemporaryWasteAccumulation = item.technicalCheckedSurvey.temporaryWasteAccumulation,
                        receivedWastes = item.technicalCheckedSurvey.receivedWastes,
                        receivedWastesWeightThisYear = item.technicalCheckedSurvey.receivedWastesWeightThisYear,
                        receivedWastesWeightLastYear = item.technicalCheckedSurvey.receivedWastesWeightLastYear,
                        hasMainEquipment = item.technicalCheckedSurvey.mainEquipment,
                        hasSecondaryEquipment = item.technicalCheckedSurvey.secondaryEquipment,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = serializableInfrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    productionCheckedSurvey = mapProductionCheckedSurvey(item.productionCheckedSurvey)
                )
            }
            is CheckedSurvey.WasteDisposalCheckedSurvey -> {
                SerializableCheckedSurvey.SerializableWasteDisposalCheckedSurvey(
                    generalInformationCheckedSurvey = mapGeneralInformationCheckedSurvey(item.generalCheckedSurvey),
                    workScheduleCheckedSurvey = mapWorkScheduleCheckedSurvey(item.workScheduleCheckedSurvey),
                    technicalCheckedSurvey = SerializableCheckedSurvey.SerializableWasteDisposalCheckedSurvey.SerializableTechnicalCheckedSurvey(
                        objectArea = item.technicalCheckedSurvey.objectArea,
                        productionArea = item.technicalCheckedSurvey.productionArea,
                        techProcessComment = item.technicalCheckedSurvey.techProcessComment,
                        wasteUnloadingArea = item.technicalCheckedSurvey.wasteUnloadingArea,
                        hasThermalEnergyProduction = item.technicalCheckedSurvey.thermalEnergyProduction,
                        hasTemporaryWasteAccumulation = item.technicalCheckedSurvey.temporaryWasteAccumulation,
                        receivedWastes = item.technicalCheckedSurvey.receivedWastes,
                        hasMainEquipment = item.technicalCheckedSurvey.mainEquipment,
                        hasSecondaryEquipment = item.technicalCheckedSurvey.secondaryEquipment,
                        schemePhotos = item.technicalCheckedSurvey.schemePhotos
                    ),
                    infrastructureCheckedSurvey = serializableInfrastructureCheckedSurveyMapper.map(
                        item.infrastructureCheckedSurvey
                    ),
                    productionCheckedSurvey = mapProductionCheckedSurvey(item.productionCheckedSurvey)
                )
            }
        }
    }

    private fun mapGeneralInformationCheckedSurvey(
        generalCheckedSurvey: GeneralCheckedSurvey
    ): SerializableGeneralInformationCheckedSurvey = with(generalCheckedSurvey) {
        return SerializableGeneralInformationCheckedSurvey(
            objectName = name,
            objectStatus = objectStatus,
            subject = subject,
            fiasAddress = fiasAddress,
            addressDescription = addressDescription
        )
    }

    private fun mapWorkScheduleCheckedSurvey(
        workScheduleCheckedSurvey: WorkScheduleCheckedSurvey
    ): SerializableWorkScheduleCheckedSurvey {
        return SerializableWorkScheduleCheckedSurvey(
            schedule = workScheduleCheckedSurvey.schedule,
            shiftsPerDayCount = workScheduleCheckedSurvey.shiftsPerDayCount,
            daysPerYearCount = workScheduleCheckedSurvey.workDaysPerYearCount,
            workplacesCount = workScheduleCheckedSurvey.workplacesCount,
            managersCount = workScheduleCheckedSurvey.managersCount,
            workersCount = workScheduleCheckedSurvey.workersCount
        )
    }

    private fun mapProductionCheckedSurvey(
        survey: ProductionCheckedSurvey
    ): SerializableProductionCheckedSurvey = with(survey) {
        return SerializableProductionCheckedSurvey(
            productCapacity = productCapacity,
            products = products,
            services = services
        )
    }
}