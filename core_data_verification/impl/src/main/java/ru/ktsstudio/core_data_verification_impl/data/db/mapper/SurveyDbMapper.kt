package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import arrow.core.k
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableInfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableProductionSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSurvey
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class SurveyDbMapper @Inject constructor(
    private val infrastructureSurveyMapper: Mapper<
        SerializableInfrastructureSurvey,
        InfrastructureSurvey
        >,
    private val mediaMapper: Mapper<SerializableMedia, Media>
) : Mapper<SerializableSurvey, Survey> {
    override fun map(item: SerializableSurvey): Survey {
        return when (item) {
            is SerializableSurvey.SerializableWasteTreatmentSurvey -> {
                Survey.WasteTreatmentSurvey(
                    technicalSurvey = TechnicalSurvey.WasteTreatmentTechnicalSurvey(
                        tkoWeightForLastYear = item.technicalSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalSurvey.otherWastesWeightForLastYear,
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(mediaMapper::map),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(mediaMapper::map),
                        wasteUnloadingArea = item.technicalSurvey.wasteUnloadingArea,
                        sortDepartmentArea = item.technicalSurvey.sortDepartmentArea,
                        hasCompostArea = item.technicalSurvey.hasCompostArea,
                        noCompostAreaReason = item.technicalSurvey.noCompostAreaReason,
                        compostAreaPower = item.technicalSurvey.compostAreaPower,
                        compostMaterial = item.technicalSurvey.compostMaterial,
                        compostPurpose = item.technicalSurvey.compostPurpose,
                        hasRdfArea = item.technicalSurvey.hasRdfArea,
                        rdfPower = item.technicalSurvey.rdfPower,
                        rdfPurpose = item.technicalSurvey.rdfPurpose,
                        techSchema = item.technicalSurvey.techSchema.map(mediaMapper::map),
                        generalSchema = item.technicalSurvey.generalSchema.map(mediaMapper::map),
                        productionSchema = item.technicalSurvey.productionSchema.map(mediaMapper::map)
                    ),
                    infrastructureSurvey = infrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    equipmentSurvey = Survey.WasteTreatmentSurvey.EquipmentSurvey(
                        servingConveyors = item.equipmentSurvey.servingConveyors.associateBy { it.id }.k(),
                        sortConveyors = item.equipmentSurvey.sortConveyors.associateBy { it.id }.k(),
                        reverseConveyors = item.equipmentSurvey.reverseConveyors.associateBy { it.id }.k(),
                        pressConveyors = item.equipmentSurvey.pressConveyors.associateBy { it.id }.k(),
                        otherConveyors = item.equipmentSurvey.otherConveyors.associateBy { it.id }.k(),
                        bagBreakers = item.equipmentSurvey.bagBreakers.associateBy { it.id }.k(),
                        separators = item.equipmentSurvey.separators.associateBy { it.id }.k(),
                        presses = item.equipmentSurvey.presses.associateBy { it.id }.k(),
                        additionalEquipment = item.equipmentSurvey
                            .additionalEquipment
                            .associateBy { it.id }
                            .k()
                    ),
                    secondaryResourcesSurvey = SecondaryResourcesSurvey(
                        extractPercent = item.secondaryResourcesSurvey.extractPercent,
                        types = item.secondaryResourcesSurvey.types.associateBy { it.id }.k()
                    )
                )
            }
            is SerializableSurvey.SerializableWastePlacementSurvey -> {
                Survey.WastePlacementSurvey(
                    technicalSurvey = TechnicalSurvey.WastePlacementTechnicalSurvey(
                        tkoWeightForLastYear = item.technicalSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalSurvey.otherWastesWeightForLastYear,
                        hasCompostArea = item.technicalSurvey.hasCompostArea,
                        noCompostAreaReason = item.technicalSurvey.noCompostAreaReason,
                        compostAreaPower = item.technicalSurvey.compostAreaPower,
                        compostMaterial = item.technicalSurvey.compostMaterial,
                        compostPurpose = item.technicalSurvey.compostPurpose,
                        hasRdfArea = item.technicalSurvey.hasRdfArea,
                        rdfPower = item.technicalSurvey.rdfPower,
                        rdfPurpose = item.technicalSurvey.rdfPurpose,
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(mediaMapper::map),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(mediaMapper::map),
                        objectBodyArea = item.technicalSurvey.objectBodyArea,
                        polygonHeight = item.technicalSurvey.polygonHeight,
                        waterproofingType = item.technicalSurvey.waterproofingType,
                        relief = item.technicalSurvey.relief,
                        groundWaterDepth = item.technicalSurvey.groundwaterDepth,
                        wastePlacementMaps = item.technicalSurvey.wastePlacementMap.associateBy { it.id }.k(),
                        canFlooding = item.technicalSurvey.canFlooding,
                        hasWasteSealant = item.technicalSurvey.hasWasteSealant,
                        sealantType = item.technicalSurvey.sealantType,
                        sealantWeight = item.technicalSurvey.sealantWeight,
                        sealantPhotos = item.technicalSurvey.sealantPhotos.map(mediaMapper::map),
                        techSchema = item.technicalSurvey.techSchema.map(mediaMapper::map),
                        generalSchema = item.technicalSurvey.generalSchema.map(mediaMapper::map),
                        productionSchema = item.technicalSurvey.productionSchema.map(mediaMapper::map)
                    ),
                    infrastructureSurvey = infrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    )
                )
            }
            is SerializableSurvey.SerializableWasteRecyclingSurvey -> {
                Survey.WasteRecyclingSurvey(
                    technicalSurvey = TechnicalSurvey.WasteRecyclingTechnicalSurvey(
                        recyclingType = item.technicalSurvey.recyclingType,
                        techProcessComment = item.technicalSurvey.techProcessComment,
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(mediaMapper::map),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(mediaMapper::map),
                        wasteUnloadingArea = item.technicalSurvey.wasteUnloadingArea,
                        hasTemporaryWasteAccumulation = item.technicalSurvey.hasTemporaryWasteAccumulation,
                        temporaryWasteArea = item.technicalSurvey.temporaryWasteArea,
                        temporaryWastes = item.technicalSurvey.temporaryWastes,
                        techSchema = item.technicalSurvey.techSchema.map(mediaMapper::map),
                        generalSchema = item.technicalSurvey.generalSchema.map(mediaMapper::map),
                        productionSchema = item.technicalSurvey.productionSchema.map(mediaMapper::map),
                        receivedWastes = item.technicalSurvey.receivedWastes.orEmpty(),
                        receivedWastesWeightThisYear = item.technicalSurvey.receivedWastesWeightThisYear,
                        receivedWastesWeightLastYear = item.technicalSurvey.receivedWastesWeightLastYear,
                        hasMainEquipment = item.technicalSurvey.hasMainEquipment,
                        mainEquipment = item.technicalSurvey.mainEquipment.associateBy { it.id }.k(),
                        hasSecondaryEquipment = item.technicalSurvey.hasSecondaryEquipment,
                        secondaryEquipment = item.technicalSurvey.secondaryEquipment.associateBy { it.id }.k()
                    ),
                    infrastructureSurvey = infrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    productionSurvey = mapProductionSurvey(item.productionSurvey)
                )
            }
            is SerializableSurvey.SerializableWasteDisposalSurvey -> {
                Survey.WasteDisposalSurvey(
                    technicalSurvey = TechnicalSurvey.WasteDisposalTechnicalSurvey(
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(mediaMapper::map),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(mediaMapper::map),
                        techProcessComment = item.technicalSurvey.techProcessComment,
                        wasteUnloadingArea = item.technicalSurvey.wasteUnloadingArea,
                        hasThermalEnergyProduction = item.technicalSurvey.hasThermalEnergyProduction,
                        thermalEnergyProductionPower = item.technicalSurvey.thermalEnergyProductionPower,
                        hasTemporaryWasteAccumulation = item.technicalSurvey.hasTemporaryWasteAccumulation,
                        temporaryWasteArea = item.technicalSurvey.temporaryWasteArea,
                        temporaryWastes = item.technicalSurvey.temporaryWastes,
                        techSchema = item.technicalSurvey.techSchema.map(mediaMapper::map),
                        generalSchema = item.technicalSurvey.generalSchema.map(mediaMapper::map),
                        productionSchema = item.technicalSurvey.productionSchema.map(mediaMapper::map),
                        receivedWastes = item.technicalSurvey.receivedWastes.orEmpty(),
                        hasMainEquipment = item.technicalSurvey.hasMainEquipment,
                        mainEquipment = item.technicalSurvey.mainEquipment.associateBy { it.id }.k(),
                        hasSecondaryEquipment = item.technicalSurvey.hasSecondaryEquipment,
                        secondaryEquipment = item.technicalSurvey.secondaryEquipment.associateBy { it.id }.k(),
                    ),
                    infrastructureSurvey = infrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    productionSurvey = mapProductionSurvey(item.productionSurvey)
                )
            }
        }
    }

    private fun mapProductionSurvey(
        serializableProductionSurvey: SerializableProductionSurvey
    ): ProductionSurvey = with(serializableProductionSurvey) {
        ProductionSurvey(
            id = id,
            productCapacity = productCapacity,
            products = products.associateBy { it.id }.k(),
            services = services.associateBy { it.id }.k()
        )
    }
}