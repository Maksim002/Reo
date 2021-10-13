package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import android.net.Uri
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableEquipmentSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableInfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableProductionSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSecondaryResourcesSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSurvey
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class SerializableSurveyDbMapper @Inject constructor(
    private val serializableInfrastructureSurveyMapper: Mapper<
        InfrastructureSurvey,
        SerializableInfrastructureSurvey
        >
) : Mapper<Survey, SerializableSurvey> {
    override fun map(item: Survey): SerializableSurvey {
        return when (item) {
            is Survey.WasteTreatmentSurvey -> {
                SerializableSurvey.SerializableWasteTreatmentSurvey(
                    technicalSurvey = SerializableSurvey.SerializableWasteTreatmentSurvey.SerializableTechnicalSurvey(
                        tkoWeightForLastYear = item.technicalSurvey.tkoWeightForLastYear,
                        otherWastesWeightForLastYear = item.technicalSurvey.otherWastesWeightForLastYear,
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(::mapMedia),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(::mapMedia),
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
                        techSchema = item.technicalSurvey.techSchema.map(::mapMedia),
                        generalSchema = item.technicalSurvey.generalSchema.map(::mapMedia),
                        productionSchema = item.technicalSurvey.productionSchema.map(::mapMedia)
                    ),
                    infrastructureSurvey = serializableInfrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    equipmentSurvey = SerializableEquipmentSurvey(
                        servingConveyors = item.equipmentSurvey.servingConveyors.values.toList(),
                        sortConveyors = item.equipmentSurvey.sortConveyors.values.toList(),
                        reverseConveyors = item.equipmentSurvey.reverseConveyors.values.toList(),
                        pressConveyors = item.equipmentSurvey.pressConveyors.values.toList(),
                        otherConveyors = item.equipmentSurvey.otherConveyors.values.toList(),
                        bagBreakers = item.equipmentSurvey.bagBreakers.values.toList(),
                        separators = item.equipmentSurvey.separators.values.toList(),
                        presses = item.equipmentSurvey.presses.values.toList(),
                        additionalEquipment = item.equipmentSurvey.additionalEquipment.values.toList()
                    ),
                    secondaryResourcesSurvey = SerializableSecondaryResourcesSurvey(
                        extractPercent = item.secondaryResourcesSurvey.extractPercent,
                        types = item.secondaryResourcesSurvey.types.values.toList()
                    )
                )
            }
            is Survey.WastePlacementSurvey -> {
                SerializableSurvey.SerializableWastePlacementSurvey(
                    technicalSurvey = SerializableSurvey.SerializableWastePlacementSurvey.SerializableTechnicalSurvey(
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
                        objectPhotos = item.technicalSurvey.objectPhotos.map(::mapMedia),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(::mapMedia),
                        objectBodyArea = item.technicalSurvey.objectBodyArea,
                        polygonHeight = item.technicalSurvey.polygonHeight,
                        waterproofingType = item.technicalSurvey.waterproofingType,
                        relief = item.technicalSurvey.relief,
                        groundwaterDepth = item.technicalSurvey.groundWaterDepth,
                        wastePlacementMap = item.technicalSurvey.wastePlacementMaps.values.toList(),
                        canFlooding = item.technicalSurvey.canFlooding,
                        hasWasteSealant = item.technicalSurvey.hasWasteSealant,
                        sealantType = item.technicalSurvey.sealantType,
                        sealantWeight = item.technicalSurvey.sealantWeight,
                        sealantPhotos = item.technicalSurvey.sealantPhotos.map(::mapMedia),
                        techSchema = item.technicalSurvey.techSchema.map(::mapMedia),
                        generalSchema = item.technicalSurvey.generalSchema.map(::mapMedia),
                        productionSchema = item.technicalSurvey.productionSchema.map(::mapMedia)
                    ),
                    infrastructureSurvey = serializableInfrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    )
                )
            }
            is Survey.WasteRecyclingSurvey -> {
                SerializableSurvey.SerializableWasteRecyclingSurvey(
                    technicalSurvey = SerializableSurvey.SerializableWasteRecyclingSurvey.SerializableTechnicalSurvey(
                        recyclingType = item.technicalSurvey.recyclingType,
                        techProcessComment = item.technicalSurvey.techProcessComment,
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(::mapMedia),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(::mapMedia),
                        wasteUnloadingArea = item.technicalSurvey.wasteUnloadingArea,
                        hasTemporaryWasteAccumulation = item.technicalSurvey.hasTemporaryWasteAccumulation,
                        temporaryWasteArea = item.technicalSurvey.temporaryWasteArea,
                        temporaryWastes = item.technicalSurvey.temporaryWastes,
                        techSchema = item.technicalSurvey.techSchema.map(::mapMedia),
                        generalSchema = item.technicalSurvey.generalSchema.map(::mapMedia),
                        productionSchema = item.technicalSurvey.productionSchema.map(::mapMedia),
                        receivedWastes = item.technicalSurvey.receivedWastes,
                        receivedWastesWeightThisYear = item.technicalSurvey.receivedWastesWeightThisYear,
                        receivedWastesWeightLastYear = item.technicalSurvey.receivedWastesWeightLastYear,
                        hasMainEquipment = item.technicalSurvey.hasMainEquipment,
                        mainEquipment = item.technicalSurvey.mainEquipment.values.toList(),
                        hasSecondaryEquipment = item.technicalSurvey.hasSecondaryEquipment,
                        secondaryEquipment = item.technicalSurvey.secondaryEquipment.values.toList()
                    ),
                    infrastructureSurvey = serializableInfrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    productionSurvey = mapProductionSurvey(item.productionSurvey)
                )
            }
            is Survey.WasteDisposalSurvey -> {
                SerializableSurvey.SerializableWasteDisposalSurvey(
                    technicalSurvey = SerializableSurvey.SerializableWasteDisposalSurvey.SerializableTechnicalSurvey(
                        objectArea = item.technicalSurvey.objectArea,
                        objectPhotos = item.technicalSurvey.objectPhotos.map(::mapMedia),
                        productionArea = item.technicalSurvey.productionArea,
                        productionPhotos = item.technicalSurvey.productionPhotos.map(::mapMedia),
                        techProcessComment = item.technicalSurvey.techProcessComment,
                        wasteUnloadingArea = item.technicalSurvey.wasteUnloadingArea,
                        hasThermalEnergyProduction = item.technicalSurvey.hasThermalEnergyProduction,
                        thermalEnergyProductionPower = item.technicalSurvey.thermalEnergyProductionPower,
                        hasTemporaryWasteAccumulation = item.technicalSurvey.hasTemporaryWasteAccumulation,
                        temporaryWasteArea = item.technicalSurvey.temporaryWasteArea,
                        temporaryWastes = item.technicalSurvey.temporaryWastes,
                        techSchema = item.technicalSurvey.techSchema.map(::mapMedia),
                        generalSchema = item.technicalSurvey.generalSchema.map(::mapMedia),
                        productionSchema = item.technicalSurvey.productionSchema.map(::mapMedia),
                        receivedWastes = item.technicalSurvey.receivedWastes,
                        hasMainEquipment = item.technicalSurvey.hasMainEquipment,
                        mainEquipment = item.technicalSurvey.mainEquipment.values.toList(),
                        hasSecondaryEquipment = item.technicalSurvey.hasSecondaryEquipment,
                        secondaryEquipment = item.technicalSurvey.secondaryEquipment.values.toList(),
                    ),
                    infrastructureSurvey = serializableInfrastructureSurveyMapper.map(
                        item.infrastructureSurvey
                    ),
                    productionSurvey = mapProductionSurvey(item.productionSurvey)
                )
            }
        }
    }

    private fun mapProductionSurvey(
        productionSurvey: ProductionSurvey
    ): SerializableProductionSurvey = with(productionSurvey) {
        return SerializableProductionSurvey(
            id = id,
            productCapacity = productCapacity,
            products = products.values.toList(),
            services = services.values.toList()
        )
    }

    private fun mapMedia(media: Media): SerializableMedia = with(media) {
        return SerializableMedia(
            remoteId = remoteId,
            localPath = cachedFile?.let { Uri.fromFile(it) }?.path,
            latitude = gpsPoint.lat.toString(),
            longitude = gpsPoint.lng.toString(),
            date = date.toEpochMilli()
        )
    }
}