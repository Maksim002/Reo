package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteIndicator
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class TechnicalSurveySendNetworkMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>,
    private val referenceMapper: Mapper<Reference, RemoteReference>,
    private val wastePlacementMapMapper: Mapper<WastePlacementMap, RemoteWastePlacementMap>
) {

    fun map(
        item: TechnicalSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteIndicator {
        return when (item) {
            is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> mapWasteTreatmentSurvey(item, localPathToMediaMap)
            is TechnicalSurvey.WastePlacementTechnicalSurvey -> mapWastePlacementSurvey(item, localPathToMediaMap)
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> mapWasteRecyclingSurvey(item, localPathToMediaMap)
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> mapWasteDisposalSurvey(item, localPathToMediaMap)
        }
    }

    private fun mapWasteTreatmentSurvey(
        item: TechnicalSurvey.WasteTreatmentTechnicalSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteIndicator {
        return with(item) {
            RemoteIndicator(
                tkoWeightForLastYear = tkoWeightForLastYear,
                otherWastesWeightForLastYear = otherWastesWeightForLastYear,
                objectArea = objectArea,
                objectPhotos = mediaMapper.map(objectPhotos, localPathToMediaMap),
                productionArea = productionArea,
                productionPhotos = mediaMapper.map(productionPhotos, localPathToMediaMap),
                wasteUnloadingArea = wasteUnloadingArea,
                sortDepartmentArea = sortDepartmentArea,
                hasCompostArea = hasCompostArea,
                noCompostAreaReason = noCompostAreaReason,
                compostAreaPower = compostAreaPower,
                compostMaterial = compostMaterial,
                compostPurpose = compostPurpose,
                hasRdfArea = hasRdfArea,
                rdfPower = rdfPower,
                rdfPurpose = rdfPurpose,
                techSchema = mediaMapper.map(techSchema, localPathToMediaMap),
                generalSchema = mediaMapper.map(generalSchema, localPathToMediaMap),
                productionSchema = mediaMapper.map(productionSchema, localPathToMediaMap),
                objectBodyArea = null,
                polygonHeight = null,
                waterproofingType = null,
                relief = null,
                groundwaterDepth = null,
                wastePlacementMaps = null,
                canFlooding = null,
                hasWasteSealant = null,
                sealantType = null,
                sealantWeight = null,
                sealantPhotos = null,
                recyclingType = null,
                techProcessComment = null,
                hasThermalRecycling = null,
                thermalRecyclingPower = null,
                hasTemporaryWasteAccumulation = null,
                temporaryWasteArea = null,
                temporaryWastes = null
            )
        }
    }

    private fun mapWastePlacementSurvey(
        item: TechnicalSurvey.WastePlacementTechnicalSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteIndicator {
        return with(item) {
            RemoteIndicator(
                tkoWeightForLastYear = tkoWeightForLastYear,
                otherWastesWeightForLastYear = otherWastesWeightForLastYear,
                objectArea = objectArea,
                objectPhotos = mediaMapper.map(objectPhotos, localPathToMediaMap),
                productionArea = productionArea,
                productionPhotos = mediaMapper.map(productionPhotos, localPathToMediaMap),
                wasteUnloadingArea = null,
                sortDepartmentArea = null,
                hasCompostArea = hasCompostArea,
                noCompostAreaReason = noCompostAreaReason,
                compostAreaPower = compostAreaPower,
                compostMaterial = compostMaterial,
                compostPurpose = compostPurpose,
                hasRdfArea = hasRdfArea,
                rdfPower = rdfPower,
                rdfPurpose = rdfPurpose,
                techSchema = mediaMapper.map(techSchema, localPathToMediaMap),
                generalSchema = mediaMapper.map(generalSchema, localPathToMediaMap),
                productionSchema = mediaMapper.map(productionSchema, localPathToMediaMap),
                objectBodyArea = objectBodyArea,
                polygonHeight = polygonHeight,
                waterproofingType = waterproofingType?.let(referenceMapper::map),
                relief = relief,
                groundwaterDepth = groundWaterDepth,
                wastePlacementMaps = wastePlacementMaps.values.toList()
                    .let(wastePlacementMapMapper::map),
                canFlooding = canFlooding,
                hasWasteSealant = hasWasteSealant,
                sealantType = sealantType,
                sealantWeight = sealantWeight,
                sealantPhotos = mediaMapper.map(sealantPhotos, localPathToMediaMap),
                recyclingType = null,
                techProcessComment = null,
                hasThermalRecycling = null,
                thermalRecyclingPower = null,
                hasTemporaryWasteAccumulation = null,
                temporaryWasteArea = null,
                temporaryWastes = null
            )
        }
    }

    private fun mapWasteRecyclingSurvey(
        item: TechnicalSurvey.WasteRecyclingTechnicalSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteIndicator {
        return with(item) {
            RemoteIndicator(
                tkoWeightForLastYear = null,
                otherWastesWeightForLastYear = null,
                objectArea = objectArea,
                objectPhotos = mediaMapper.map(objectPhotos, localPathToMediaMap),
                productionArea = productionArea,
                productionPhotos =  mediaMapper.map(productionSchema, localPathToMediaMap),
                wasteUnloadingArea = wasteUnloadingArea,
                sortDepartmentArea = null,
                hasCompostArea = null,
                noCompostAreaReason = null,
                compostAreaPower = null,
                compostMaterial = null,
                compostPurpose = null,
                hasRdfArea = null,
                rdfPower = null,
                rdfPurpose = null,
                techSchema = mediaMapper.map(techSchema, localPathToMediaMap),
                generalSchema = mediaMapper.map(generalSchema, localPathToMediaMap),
                productionSchema = mediaMapper.map(productionSchema, localPathToMediaMap),
                objectBodyArea = null,
                polygonHeight = null,
                waterproofingType = null,
                relief = null,
                groundwaterDepth = null,
                wastePlacementMaps = null,
                canFlooding = null,
                hasWasteSealant = null,
                sealantType = null,
                sealantWeight = null,
                sealantPhotos = null,
                recyclingType = recyclingType?.let(referenceMapper::map),
                techProcessComment = techProcessComment,
                hasThermalRecycling = null,
                thermalRecyclingPower = null,
                hasTemporaryWasteAccumulation = hasTemporaryWasteAccumulation,
                temporaryWasteArea = temporaryWasteArea,
                temporaryWastes = temporaryWastes
            )
        }
    }

    private fun mapWasteDisposalSurvey(
        item: TechnicalSurvey.WasteDisposalTechnicalSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteIndicator {
        return with(item) {
            RemoteIndicator(
                tkoWeightForLastYear = null,
                otherWastesWeightForLastYear = null,
                objectArea = objectArea,
                objectPhotos = mediaMapper.map(objectPhotos, localPathToMediaMap),
                productionArea = productionArea,
                productionPhotos = mediaMapper.map(productionPhotos, localPathToMediaMap),
                wasteUnloadingArea = wasteUnloadingArea,
                sortDepartmentArea = null,
                hasCompostArea = null,
                noCompostAreaReason = null,
                compostAreaPower = null,
                compostMaterial = null,
                compostPurpose = null,
                hasRdfArea = null,
                rdfPower = null,
                rdfPurpose = null,
                techSchema = mediaMapper.map(techSchema, localPathToMediaMap),
                generalSchema = mediaMapper.map(generalSchema, localPathToMediaMap),
                productionSchema = mediaMapper.map(productionSchema, localPathToMediaMap),
                recyclingType = null,
                techProcessComment = techProcessComment,
                hasThermalRecycling = hasThermalEnergyProduction,
                thermalRecyclingPower = thermalEnergyProductionPower,
                hasTemporaryWasteAccumulation = hasTemporaryWasteAccumulation,
                temporaryWasteArea = temporaryWasteArea,
                temporaryWastes = temporaryWastes,
                objectBodyArea = null,
                polygonHeight = null,
                waterproofingType = null,
                relief = null,
                groundwaterDepth = null,
                wastePlacementMaps = null,
                canFlooding = null,
                hasWasteSealant = null,
                sealantType = null,
                sealantWeight = null,
                sealantPhotos = null
            )
        }
    }
}