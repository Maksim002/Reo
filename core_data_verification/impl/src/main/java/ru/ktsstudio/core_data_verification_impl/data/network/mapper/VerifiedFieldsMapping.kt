package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.core_data_verfication_api.data.model.GeneralCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.WorkScheduleCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.EquipmentKind
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResources
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
object VerifiedFieldsMapping {

    object GeneralInformation {
        private const val ObjectName = "name"
        private const val ObjectStatus = "status"
        private const val SubjectArea = "subjectAreaName"
        private const val AddressDescription = "addressDescription"
        private const val Lat = "latitude"
        private const val Lng = "longitude"
        private const val FiasId = "fiasAddress.id"
        private const val FiasOktmo = "fiasAddress.oktmo"

        fun getFromVerifiedValues(values: Set<String>): GeneralCheckedSurvey {
            return GeneralCheckedSurvey(
                objectStatus = values.contains(ObjectStatus),
                name = values.contains(ObjectName),
                subject = values.contains(SubjectArea),
                fiasAddress = values.containsAll(listOf(Lat, Lng, FiasId, FiasOktmo)),
                addressDescription = values.contains(AddressDescription)
            )
        }

        fun getVerifiedValues(checkedSurvey: GeneralCheckedSurvey): List<String> {
            return listOfNotNull(
                ObjectName.takeIf { checkedSurvey.name },
                ObjectStatus.takeIf { checkedSurvey.objectStatus },
                SubjectArea.takeIf { checkedSurvey.subject },
                AddressDescription.takeIf { checkedSurvey.addressDescription },
                Lat.takeIf { checkedSurvey.fiasAddress },
                Lng.takeIf { checkedSurvey.fiasAddress },
                FiasId.takeIf { checkedSurvey.fiasAddress },
                FiasOktmo.takeIf { checkedSurvey.fiasAddress }
            )
        }
    }

    object WorkSchedule {
        private const val Schedule = "workSchedule.scheduleJson"
        private const val ShiftsPerDayCount = "workSchedule.shiftsPerDayCount"
        private const val DaysPerYearCount = "workSchedule.daysPerYearCount"
        private const val WorkplacesCount = "workSchedule.workplacesCount"
        private const val ManagersCount = "workSchedule.managersCount"
        private const val WorkersCount = "workSchedule.workersCount"

        fun getFromVerifiedValues(values: Set<String>): WorkScheduleCheckedSurvey {
            return WorkScheduleCheckedSurvey(
                schedule = values.contains(Schedule),
                shiftsPerDayCount = values.contains(ShiftsPerDayCount),
                workDaysPerYearCount = values.contains(DaysPerYearCount),
                workplacesCount = values.contains(WorkplacesCount),
                managersCount = values.contains(ManagersCount),
                workersCount = values.contains(WorkersCount)
            )
        }

        fun getVerifiedValues(survey: WorkScheduleCheckedSurvey): List<String> {
            return listOfNotNull(
                Schedule.takeIf { survey.schedule },
                ShiftsPerDayCount.takeIf { survey.shiftsPerDayCount },
                DaysPerYearCount.takeIf { survey.workDaysPerYearCount },
                WorkplacesCount.takeIf { survey.workplacesCount },
                ManagersCount.takeIf { survey.managersCount },
                WorkersCount.takeIf { survey.workersCount }
            )
        }
    }

    object Technical {

        private const val MapsPrefix = "indicator.maps"
        private const val WasteTypesPrefix = "wasteInfo.wasteTypes"

        private const val Period = "period"
        private const val Size = "size"

        private const val WastePlacementMapsSize = "indicator.mapCount"
        private val WastePlacementFields = listOf(
            Period,
            Size
        )

        private const val EquipmentPrefix = "equipment"
        private const val HasMainEquipment = "hasCoreEquipment"
        private const val HasSecondaryEquipment = "hasAuxiliaryEquipment"
        private const val Brand = "brand"
        private const val Manufacturer = "manufacturer"
        private const val Count = "count"
        private const val ProjectPower = "projectPower"
        private const val ProcessDescription = "processDescription"
        private const val Production = "production"
        private const val Photos = "photos"
        private const val PassportPhotos = "passportPhotos"
        private const val Type = "type"
        private const val Kind = "kind"
        private const val Created = "created"

        private val TechnicalEquipmentFields = listOf(
            Brand,
            Manufacturer,
            Count,
            ProjectPower,
            ProcessDescription,
            Production,
            Photos,
            PassportPhotos,
            Type,
            Kind,
            Created
        )

        private const val ObjectPhotos = "indicator.photos"
        private const val ProductionPhotos = "indicator.productionPartPhotos"
        private const val RecyclingType = "indicator.utilizationType"
        private const val TechProcessComment = "indicator.techProcessDescription"
        private const val TkoWeightForLastYear = "indicator.previousYearTkoWeight"
        private const val OtherWastesWeightForLastYear = "indicator.previousYearNonTkoWeight"
        private const val ObjectArea = "indicator.area"
        private const val ProductionArea = "indicator.productionPartArea"
        private const val ObjectBodyArea = "indicator.placingArea"
        private const val PolygonHeight = "indicator.projectHeight"
        private const val WaterproofingType = "indicator.waterproofingType"
        private const val Relief = "indicator.relief"
        private const val GroundwaterDepth = "indicator.groundwaterDepth"
        private const val WasteUnloadingArea = "indicator.unloadingZoneArea"
        private const val HasThermalEnergyProduction = "indicator.hasThermalRecycling"
        private const val ThermalRecyclingPower = "indicator.thermalRecyclingPower"
        private const val HasTemporaryWasteAccumulation = "indicator.hasTemporaryWasteStorage"
        private const val TemporaryWasteStorageArea = "indicator.temporaryWasteStorageArea"
        private const val TemporaryWasteStorageWasteTypes = "indicator.temporaryWasteStorageWasteTypes"
        private const val SortDepartmentArea = "indicator.sortingZoneArea"
        private const val HasCompostArea = "indicator.hasCompostingZone"
        private const val CompostingZoneCapacityPerYear = "indicator.compostingZoneCapacityPerYear"
        private const val CompostingMaterialName = "indicator.compostingMaterialName"
        private const val CompostPurpose = "indicator.compostPurpose"
        private const val NoCompostingZoneReason = "indicator.noCompostingZoneReason"
        private const val HasRdfArea = "indicator.hasRdfZone"
        private const val RdfZoneCapacityPerYear = "indicator.rdfZoneCapacityPerYear"
        private const val RdfPurpose = "indicator.rdfPurpose"
        private const val CanFlooding = "indicator.floodingPossibility"
        private const val HasWasteSealant = "indicator.hasWasteCompactor"
        private const val WasteCompactorType = "indicator.wasteCompactorType"
        private const val WasteCompactorMass = "indicator.wasteCompactorMass"
        private const val WasteCompactorPhotos = "indicator.wasteCompactorPhotos"
        private const val TechnologicalSchemePhotos = "indicator.technologicalSchemePhotos"
        private const val GeneralSchemePhotos = "indicator.generalSchemePhotos"
        private const val ProductionZoneSchemePhotos = "indicator.productionZoneSchemePhotos"
        private const val WasteType = "wasteType"
        private const val WastesWeightLastYear = "wasteInfo.vmrPreviousYear"
        private const val WastesWeightThisYear = "wasteInfo.vmrThisYear"

        fun getFromTreatmentVerifiedValues(verified: Set<String>): TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey {
            return TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey(
                tkoWeightForLastYear = verified.contains(TkoWeightForLastYear),
                otherWastesWeightForLastYear = verified.contains(OtherWastesWeightForLastYear),
                objectArea = verified.contains(ObjectArea),
                productionArea = verified.contains(ProductionArea),
                wasteUnloadingArea = verified.contains(WasteUnloadingArea),
                sortDepartmentArea = verified.contains(SortDepartmentArea),
                compostArea = verified.contains(HasCompostArea),
                rdfArea = verified.contains(HasRdfArea),
                schemePhotos = verified.containsAll(
                    listOf(TechnologicalSchemePhotos, GeneralSchemePhotos, ProductionZoneSchemePhotos)
                )
            )
        }

        fun getFromPlacementVerifiedValues(
            remoteObject: RemoteVerificationObject,
            verified: Set<String>
        ): TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey {

            fun getWastePlacementCheck(remoteObject: RemoteVerificationObject, verified: Set<String>): Boolean {
                return remoteObject.indicator.wastePlacementMaps
                    ?.getIndexes()
                    ?.let { positions ->
                        verified.checkFieldsForIndexesExists(MapsPrefix, WastePlacementFields, positions) &&
                            verified.contains(WastePlacementMapsSize)
                    }
                    .orFalse()
            }

            return TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey(
                tkoWeightForLastYear = verified.contains(TkoWeightForLastYear),
                otherWastesWeightForLastYear = verified.contains(OtherWastesWeightForLastYear),
                compostArea = verified.contains(HasCompostArea),
                rdfArea = verified.contains(HasRdfArea),
                objectArea = verified.contains(ObjectArea),
                productionArea = verified.contains(ProductionArea),
                objectBodyArea = verified.contains(ObjectBodyArea),
                polygonHeight = verified.contains(PolygonHeight),
                waterproofingType = verified.contains(WaterproofingType),
                relief = verified.contains(Relief),
                groundwaterDepth = verified.contains(GroundwaterDepth),
                wastePlacementMap = getWastePlacementCheck(remoteObject, verified),
                canFlooding = verified.contains(CanFlooding),
                wasteSealant = verified.contains(HasWasteSealant),
                schemePhotos = verified.containsAll(
                    listOf(TechnologicalSchemePhotos, GeneralSchemePhotos, ProductionZoneSchemePhotos)
                )
            )
        }

        fun getFromRecyclingVerifiedValues(
            verified: Set<String>,
            remoteObject: RemoteVerificationObject,
            receivedWastesPositions: List<Int>
        ): TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey {
            return TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey(
                recyclingType = verified.contains(RecyclingType),
                techProcessComment = verified.contains(TechProcessComment),
                objectArea = verified.contains(ObjectArea),
                productionArea = verified.contains(ProductionArea),
                wasteUnloadingArea = verified.contains(WasteUnloadingArea),
                temporaryWasteAccumulation = verified.contains(HasTemporaryWasteAccumulation),
                mainEquipment = getEquipmentCheck(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN, remoteObject, verified),
                secondaryEquipment = getEquipmentCheck(
                    EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY,
                    remoteObject,
                    verified
                ),
                receivedWastes = verified.checkFieldsForIndexesExists(
                    prefix = WasteTypesPrefix,
                    requiredFields = listOf(WasteType),
                    positions = receivedWastesPositions
                ),
                schemePhotos = verified.containsAll(
                    listOf(TechnologicalSchemePhotos, GeneralSchemePhotos, ProductionZoneSchemePhotos)
                ),
                receivedWastesWeightThisYear = verified.contains(WastesWeightThisYear),
                receivedWastesWeightLastYear = verified.contains(WastesWeightLastYear)
            )
        }

        fun getFromDisposalVerifiedValues(
            verified: Set<String>,
            remoteObject: RemoteVerificationObject,
            receivedWastesPositions: List<Int>
        ): TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey {
            return TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey(
                objectArea = verified.contains(ObjectArea),
                productionArea = verified.contains(ProductionArea),
                techProcessComment = verified.contains(TechProcessComment),
                wasteUnloadingArea = verified.contains(WasteUnloadingArea),
                thermalEnergyProduction = verified.contains(HasThermalEnergyProduction),
                temporaryWasteAccumulation = verified.contains(HasTemporaryWasteAccumulation),
                mainEquipment = getEquipmentCheck(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN, remoteObject, verified),
                secondaryEquipment = getEquipmentCheck(
                    EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY,
                    remoteObject,
                    verified
                ),
                receivedWastes = verified.checkFieldsForIndexesExists(
                    prefix = WasteTypesPrefix,
                    requiredFields = listOf(WasteType),
                    positions = receivedWastesPositions
                ),
                schemePhotos = verified.containsAll(
                    listOf(TechnologicalSchemePhotos, GeneralSchemePhotos, ProductionZoneSchemePhotos)
                )
            )
        }

        private fun getEquipmentCheck(
            kind: EquipmentKind,
            remoteObject: RemoteVerificationObject,
            verified: Set<String>
        ): Boolean {

            val hasEquipmentVerified = when (kind) {
                EquipmentKind.TECHNICAL_EQUIPMENT_MAIN -> verified.contains(HasMainEquipment)
                EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY -> verified.contains(HasSecondaryEquipment)
                else -> error("incorrect kind = $kind")
            }

            val equipment = remoteObject.equipment.filter { it.kind.isEquipmentKind(kind) }

            val allEquipmentFieldsChecked = equipment.isEmpty() || remoteObject.equipment
                .getIndexes { it.kind.isEquipmentKind(kind) }
                .let { positions ->
                    verified.checkFieldsForIndexesExists(EquipmentPrefix, TechnicalEquipmentFields, positions)
                }
                .orFalse()

            return hasEquipmentVerified && allEquipmentFieldsChecked
        }

        private fun getEquipmentVerifiedValues(
            kind: EquipmentKind,
            equipment: List<RemoteEquipment>
        ): List<String> {
            return equipment
                .getIndexes { it.kind.isEquipmentKind(kind) }
                .let {
                    getRequiredFieldsForPositions(
                        prefix = EquipmentPrefix,
                        objectFields = TechnicalEquipmentFields,
                        positions = it
                    )
                }
        }

        fun getTreatmentVerifiedValues(
            survey: TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey,
            hasCompostArea: Boolean,
            hasRdfArea: Boolean
        ): List<String> {
            return listOfNotNull(
                TkoWeightForLastYear.takeIf { survey.tkoWeightForLastYear },
                OtherWastesWeightForLastYear.takeIf { survey.otherWastesWeightForLastYear },
                ObjectArea.takeIf { survey.objectArea },
                ObjectPhotos.takeIf { survey.objectArea },
                ProductionArea.takeIf { survey.productionArea },
                ProductionPhotos.takeIf { survey.productionArea },
                WasteUnloadingArea.takeIf { survey.wasteUnloadingArea },
                SortDepartmentArea.takeIf { survey.sortDepartmentArea },
                HasCompostArea.takeIf { survey.compostArea },
                CompostingZoneCapacityPerYear.takeIf { survey.compostArea && hasCompostArea },
                CompostingMaterialName.takeIf { survey.compostArea && hasCompostArea },
                CompostPurpose.takeIf { survey.compostArea && hasCompostArea },
                NoCompostingZoneReason.takeIf { survey.compostArea && hasCompostArea.not() },
                HasRdfArea.takeIf { survey.rdfArea },
                RdfZoneCapacityPerYear.takeIf { survey.rdfArea && hasRdfArea },
                RdfPurpose.takeIf { survey.rdfArea && hasRdfArea },
                TechnologicalSchemePhotos.takeIf { survey.schemePhotos },
                GeneralSchemePhotos.takeIf { survey.schemePhotos },
                ProductionZoneSchemePhotos.takeIf { survey.schemePhotos }
            )
        }

        fun getPlacementVerifiedValues(
            survey: TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey,
            wastePlacement: List<RemoteWastePlacementMap>,
            hasCompostArea: Boolean,
            hasRdfArea: Boolean,
            hasWasteSealant: Boolean
        ): List<String> {
            return listOfNotNull(
                TkoWeightForLastYear.takeIf { survey.tkoWeightForLastYear },
                OtherWastesWeightForLastYear.takeIf { survey.otherWastesWeightForLastYear },
                HasCompostArea.takeIf { survey.compostArea },
                CompostingZoneCapacityPerYear.takeIf { survey.compostArea && hasCompostArea },
                CompostingMaterialName.takeIf { survey.compostArea && hasCompostArea },
                CompostPurpose.takeIf { survey.compostArea && hasCompostArea },
                NoCompostingZoneReason.takeIf { survey.compostArea && hasCompostArea.not() },
                HasRdfArea.takeIf { survey.rdfArea },
                RdfZoneCapacityPerYear.takeIf { survey.rdfArea && hasRdfArea },
                RdfPurpose.takeIf { survey.rdfArea && hasRdfArea },
                ObjectArea.takeIf { survey.objectArea },
                ObjectPhotos.takeIf { survey.objectArea },
                ProductionArea.takeIf { survey.productionArea },
                ProductionPhotos.takeIf { survey.productionArea },
                ObjectBodyArea.takeIf { survey.objectBodyArea },
                PolygonHeight.takeIf { survey.polygonHeight },
                WaterproofingType.takeIf { survey.waterproofingType },
                Relief.takeIf { survey.relief },
                GroundwaterDepth.takeIf { survey.groundwaterDepth },
                CanFlooding.takeIf { survey.canFlooding },
                HasWasteSealant.takeIf { survey.wasteSealant },
                WasteCompactorType.takeIf { survey.wasteSealant && hasWasteSealant },
                WasteCompactorMass.takeIf { survey.wasteSealant && hasWasteSealant },
                WasteCompactorPhotos.takeIf { survey.wasteSealant && hasWasteSealant },
                TechnologicalSchemePhotos.takeIf { survey.schemePhotos },
                GeneralSchemePhotos.takeIf { survey.schemePhotos },
                ProductionZoneSchemePhotos.takeIf { survey.schemePhotos },
                WastePlacementMapsSize.takeIf { survey.wastePlacementMap }
            ) + getRequiredFieldsForPositions(
                prefix = MapsPrefix,
                objectFields = WastePlacementFields,
                positions = wastePlacement.getIndexes()
            )
                .takeIf { survey.wastePlacementMap }
                .orEmpty()
        }

        fun getRecyclingVerifiedValues(
            survey: TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey,
            receivedWasteTypePositions: List<Int>,
            remoteEquipment: List<RemoteEquipment>,
            hasTemporaryWasteAccumulation: Boolean,
            hasMainEquipment: Boolean,
            hasSecondaryEquipment: Boolean
        ): List<String> {
            val mainEquipment = getEquipmentVerifiedValues(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN, remoteEquipment)
                .takeIf { survey.mainEquipment && hasMainEquipment }
                .orEmpty()

            val secondaryEquipment =
                getEquipmentVerifiedValues(EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY, remoteEquipment)
                    .takeIf { survey.secondaryEquipment && hasSecondaryEquipment }
                    .orEmpty()

            return listOfNotNull(
                RecyclingType.takeIf { survey.recyclingType },
                TechProcessComment.takeIf { survey.techProcessComment },
                ObjectArea.takeIf { survey.objectArea },
                ProductionArea.takeIf { survey.productionArea },
                ObjectPhotos.takeIf { survey.objectArea },
                ProductionPhotos.takeIf { survey.productionArea },
                WasteUnloadingArea.takeIf { survey.wasteUnloadingArea },
                HasTemporaryWasteAccumulation.takeIf { survey.temporaryWasteAccumulation },
                TemporaryWasteStorageArea.takeIf {
                    survey.temporaryWasteAccumulation && hasTemporaryWasteAccumulation
                },
                TemporaryWasteStorageWasteTypes.takeIf {
                    survey.temporaryWasteAccumulation && hasTemporaryWasteAccumulation
                },
                HasMainEquipment.takeIf { survey.mainEquipment },
                HasSecondaryEquipment.takeIf { survey.secondaryEquipment },
                TechnologicalSchemePhotos.takeIf { survey.schemePhotos },
                GeneralSchemePhotos.takeIf { survey.schemePhotos },
                ProductionZoneSchemePhotos.takeIf { survey.schemePhotos },
                WastesWeightLastYear.takeIf { survey.receivedWastesWeightLastYear },
                WastesWeightThisYear.takeIf { survey.receivedWastesWeightThisYear }
            ) +
                mainEquipment +
                secondaryEquipment +
                getRequiredFieldsForPositions(WasteTypesPrefix, listOf(WasteType), receivedWasteTypePositions)
                    .takeIf { survey.receivedWastes }
                    .orEmpty()
        }

        fun getDisposalVerifiedValues(
            survey: TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey,
            receivedWasteTypePositions: List<Int>,
            remoteEquipment: List<RemoteEquipment>,
            hasThermalEnergyProduction: Boolean,
            hasTemporaryWasteAccumulation: Boolean,
            hasMainEquipment: Boolean,
            hasSecondaryEquipment: Boolean
        ): List<String> {

            val mainEquipment = getEquipmentVerifiedValues(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN, remoteEquipment)
                .takeIf { survey.mainEquipment && hasMainEquipment }
                .orEmpty()

            val secondaryEquipment =
                getEquipmentVerifiedValues(EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY, remoteEquipment)
                    .takeIf { survey.secondaryEquipment && hasSecondaryEquipment }
                    .orEmpty()

            return listOfNotNull(
                ObjectArea.takeIf { survey.objectArea },
                ProductionArea.takeIf { survey.productionArea },
                ObjectPhotos.takeIf { survey.objectArea },
                ProductionPhotos.takeIf { survey.productionArea },
                TechProcessComment.takeIf { survey.techProcessComment },
                WasteUnloadingArea.takeIf { survey.wasteUnloadingArea },
                HasThermalEnergyProduction.takeIf { survey.thermalEnergyProduction },
                ThermalRecyclingPower.takeIf { survey.thermalEnergyProduction && hasThermalEnergyProduction },
                HasTemporaryWasteAccumulation.takeIf { survey.temporaryWasteAccumulation },
                TemporaryWasteStorageArea.takeIf {
                    survey.temporaryWasteAccumulation && hasTemporaryWasteAccumulation
                },
                TemporaryWasteStorageWasteTypes.takeIf {
                    survey.temporaryWasteAccumulation && hasTemporaryWasteAccumulation
                },
                HasMainEquipment.takeIf { survey.mainEquipment },
                HasSecondaryEquipment.takeIf { survey.secondaryEquipment },
                TechnologicalSchemePhotos.takeIf { survey.schemePhotos },
                GeneralSchemePhotos.takeIf { survey.schemePhotos },
                ProductionZoneSchemePhotos.takeIf { survey.schemePhotos }
            ) +
                mainEquipment +
                secondaryEquipment +
                getRequiredFieldsForPositions(WasteTypesPrefix, listOf(WasteType), receivedWasteTypePositions)
                    .takeIf { survey.receivedWastes }
                    .orEmpty()
        }
    }

    object Infrastructure {
        private const val Type = "type"
        private const val Length = "length"
        private const val Manufacturer = "manufacturer"
        private const val Brand = "brand"
        private const val Photos = "photos"
        private const val Passport = "passportPhotos"
        private const val Capacity = "capacity"

        private val WeightControlFields = listOf(Brand, Manufacturer, Length, Photos)
        private val WheelsWashingFields = listOf(Brand, Manufacturer, Photos)
        private val LocalSewagePlantFields = listOf(Type, Capacity, Photos, Passport)
        private val RadiationControlFields = listOf(Brand, Manufacturer, Photos, Passport)
        private val EnvironmentMonitoringSystemFields = listOf(Type)

        private const val HasWeightControl = "infrastructureObjectInfo.hasWeightControl"
        private const val WeightControlPrefix = "infrastructureObjectInfo.weightControls"
        private const val NoWeightControlReason = "infrastructureObjectInfo.noWeightControlReason"
        private const val WeightControlCount = "infrastructureObjectInfo.weightControlCount"

        private const val HasWheelsWashing = "infrastructureObjectInfo.hasWheelsWashingPoint"
        private const val WheelsWashingPrefix = "infrastructureObjectInfo.wheelsWashingPoints"
        private const val NoWheelsWashingReason = "infrastructureObjectInfo.noWheelsWashingPointReason"
        private const val WheelsWashingCount = "infrastructureObjectInfo.wheelsWashingPointCount"

        private const val HasLocalSewagePlant = "infrastructureObjectInfo.hasLocalTreatmentFacilities"
        private const val LocalSewagePlantPrefix = "infrastructureObjectInfo.localTreatmentFacilities"
        private const val NoLocalSewagePlantReason = "infrastructureObjectInfo.noLocalTreatmentFacilitiesReason"
        private const val LocalSewagePlantCount = "infrastructureObjectInfo.localTreatmentFacilitiesCount"

        private const val HasRadiationControl = "infrastructureObjectInfo.hasRadiationControl"
        private const val RadiationControlPrefix = "infrastructureObjectInfo.radiationControls"
        private const val NoRadiationControlReason = "infrastructureObjectInfo.noRadiationControlReason"
        private const val RadiationControlCount = "infrastructureObjectInfo.radiationControlCount"

        private const val HasRoads = "infrastructureObjectInfo.hasRoads"
        private const val NoRoadsReason = "infrastructureObjectInfo.noRoadsReason"
        private const val RoadLength = "infrastructureObjectInfo.road.length"
        private const val RoadType = "infrastructureObjectInfo.road.type"
        private const val RoadSchema = "infrastructureObjectInfo.road.schemePhotos"

        private const val HasFences = "infrastructureObjectInfo.hasFences"
        private const val NoFencesReason = "infrastructureObjectInfo.noFencesReason"
        private const val FenceType = "infrastructureObjectInfo.fence.type"
        private const val FencePhotos = "infrastructureObjectInfo.fence.photos"

        private const val HasLights = "infrastructureObjectInfo.hasLights"
        private const val NoLightsReason = "infrastructureObjectInfo.noLightsReason"
        private const val LightType = "infrastructureObjectInfo.light.type"

        private const val HasSecurity = "infrastructureObjectInfo.hasSecurity"
        private const val NoSecurityReason = "infrastructureObjectInfo.noSecurityReason"
        private const val SecurityCount = "infrastructureObjectInfo.security.count"
        private const val SecurityType = "infrastructureObjectInfo.security.type"

        private const val HasAsuSystem = "infrastructureObjectInfo.hasAsu"
        private const val NoAsuSystemReason = "infrastructureObjectInfo.noAsuReason"
        private const val AsuSystemPurpose = "infrastructureObjectInfo.asu.purpose"
        private const val AsuSystemFunctions = "infrastructureObjectInfo.asu.functions"

        private const val HasVideoEquipment = "infrastructureObjectInfo.hasVideoEquipment"
        private const val NoVideoEquipmentReason = "infrastructureObjectInfo.noVideoEquipmentReason"
        private const val VideoEquipmentCount = "infrastructureObjectInfo.videoEquipment.count"

        private const val EnvironmentMonitoringSystemPrefix = "infrastructureObjectInfo.environmentMonitoringSystems"
        private const val HasEnvironmentMonitoringSystem = "infrastructureObjectInfo.hasEnvironmentMonitoringSystem"
        private const val NoEnvironmentMonitoringSystemReason =
            "infrastructureObjectInfo.noEnvironmentMonitoringSystemReason"
        private const val EnvironmentMonitoringSystemCount = "infrastructureObjectInfo.environmentMonitoringSystemCount"

        fun getFromVerifiedValues(verified: Set<String>): InfrastructureCheckedSurvey {
            return InfrastructureCheckedSurvey(
                weightControl = verified.contains(HasWeightControl),
                wheelsWashing = verified.contains(HasWheelsWashing),
                sewagePlant = verified.contains(HasLocalSewagePlant),
                radiationControl = verified.contains(HasRadiationControl),
                roadNetwork = verified.contains(HasRoads),
                fences = verified.contains(HasFences),
                lightSystem = verified.contains(HasLights),
                securityStation = verified.contains(HasSecurity),
                asu = verified.contains(HasAsuSystem),
                securityCamera = verified.contains(HasVideoEquipment),
                environmentMonitoring = verified.contains(HasEnvironmentMonitoringSystem)
            )
        }

        fun getVerifiedValues(
            checkedSurvey: InfrastructureCheckedSurvey,
            survey: InfrastructureSurvey,
        ): List<String> {
            val isWeightControlAvailable = survey.weightControl.availabilityInfo.isAvailable
            val isWheelsWashingAvailable = survey.wheelsWashing.availabilityInfo.isAvailable
            val isSewagePlantAvailable = survey.sewagePlant.availabilityInfo.isAvailable
            val isRadiationControlAvailable = survey.radiationControl.availabilityInfo.isAvailable
            val isEnvironmentMonitoringAvailable = survey.environmentMonitoring
                ?.availabilityInfo
                ?.isAvailable
                .orFalse()

            val isRoadsAvailable = survey.roadNetwork.availabilityInfo.isAvailable
            val isFencesAvailable = survey.fences.availabilityInfo.isAvailable
            val isLightsAvailable = survey.lightSystem.availabilityInfo.isAvailable
            val isSecurityAvailable = survey.securityStation.availabilityInfo.isAvailable
            val isAsuAvailable = survey.asu.availabilityInfo.isAvailable
            val isVideoEquipmentAvailable = survey.securityCamera.availabilityInfo.isAvailable

            return listOfNotNull(
                HasWeightControl.takeIf { checkedSurvey.weightControl },
                NoWeightControlReason.takeIf { isWeightControlAvailable.not() },
                WeightControlCount.takeIf { isWeightControlAvailable },

                HasWheelsWashing.takeIf { checkedSurvey.wheelsWashing },
                NoWheelsWashingReason.takeIf { isWheelsWashingAvailable.not() },
                WheelsWashingCount.takeIf { isWheelsWashingAvailable },

                HasLocalSewagePlant.takeIf { checkedSurvey.sewagePlant },
                NoLocalSewagePlantReason.takeIf { isSewagePlantAvailable.not() },
                LocalSewagePlantCount.takeIf { isSewagePlantAvailable },

                HasRadiationControl.takeIf { checkedSurvey.radiationControl },
                NoRadiationControlReason.takeIf { isRadiationControlAvailable.not() },
                RadiationControlCount.takeIf { isRadiationControlAvailable },

                HasEnvironmentMonitoringSystem.takeIf { checkedSurvey.environmentMonitoring.orFalse() },
                NoEnvironmentMonitoringSystemReason.takeIf { isEnvironmentMonitoringAvailable.not() },
                EnvironmentMonitoringSystemCount.takeIf { isEnvironmentMonitoringAvailable },

                HasRoads.takeIf { checkedSurvey.roadNetwork },
                NoRoadsReason.takeIf { isRoadsAvailable.not() },
                RoadLength.takeIf { isRoadsAvailable },
                RoadType.takeIf { isRoadsAvailable },
                RoadSchema.takeIf { isRoadsAvailable },

                HasFences.takeIf { checkedSurvey.fences },
                NoFencesReason.takeIf { isFencesAvailable.not() },
                FenceType.takeIf { isFencesAvailable },
                FencePhotos.takeIf { isFencesAvailable },

                HasLights.takeIf { checkedSurvey.lightSystem },
                NoLightsReason.takeIf { isLightsAvailable.not() },
                LightType.takeIf { isLightsAvailable },

                HasSecurity.takeIf { checkedSurvey.securityStation },
                NoSecurityReason.takeIf { isSecurityAvailable.not() },
                SecurityCount.takeIf { isSecurityAvailable },
                SecurityType.takeIf { isSecurityAvailable },

                HasAsuSystem.takeIf { checkedSurvey.asu },
                NoAsuSystemReason.takeIf { isAsuAvailable.not() },
                AsuSystemPurpose.takeIf { isAsuAvailable },
                AsuSystemFunctions.takeIf { isAsuAvailable },

                HasVideoEquipment.takeIf { checkedSurvey.securityCamera },
                NoVideoEquipmentReason.takeIf { isVideoEquipmentAvailable.not() },
                VideoEquipmentCount.takeIf { isVideoEquipmentAvailable }
            ) +
                survey.weightControl.equipment.values.getIndexes()
                    .let {
                        getRequiredFieldsForPositions(
                            prefix = WeightControlPrefix,
                            objectFields = WeightControlFields,
                            positions = it
                        )
                    }.takeIfOrEmpty { isWeightControlAvailable } +
                survey.wheelsWashing.equipment.values.getIndexes()
                    .let {
                        getRequiredFieldsForPositions(
                            prefix = WheelsWashingPrefix,
                            objectFields = WheelsWashingFields,
                            positions = it
                        )
                    }.takeIfOrEmpty { isWheelsWashingAvailable } +
                survey.radiationControl.equipment.values.getIndexes()
                    .let {
                        getRequiredFieldsForPositions(
                            prefix = RadiationControlPrefix,
                            objectFields = RadiationControlFields,
                            positions = it
                        )
                    }.takeIfOrEmpty { isRadiationControlAvailable } +
                survey.sewagePlant.equipment.values.getIndexes()
                    .let {
                        getRequiredFieldsForPositions(
                            prefix = LocalSewagePlantPrefix,
                            objectFields = LocalSewagePlantFields,
                            positions = it
                        )
                    }.takeIfOrEmpty { isSewagePlantAvailable } +
                survey.environmentMonitoring?.equipment.orEmpty().values.getIndexes()
                    .let {
                        getRequiredFieldsForPositions(
                            prefix = EnvironmentMonitoringSystemPrefix,
                            objectFields = EnvironmentMonitoringSystemFields,
                            positions = it
                        )
                    }.takeIfOrEmpty { isEnvironmentMonitoringAvailable }
        }
    }

    object Equipment {

        const val Prefix = "equipment"
        private const val Brand = "brand"
        private const val Manufacturer = "manufacturer"
        private const val Count = "count"
        private const val Photos = "photos"
        private const val PassportPhotos = "passportPhotos"
        private const val Length = "length"
        private const val Width = "width"
        private const val Speed = "speed"
        private const val Name = "name"
        private const val LoadingMechanism = "loadingMechanism"
        private const val WasteHeight = "wasteWidth"
        private const val SortPointCount = "sortingPostsCount"
        private const val Type = "type"

        val ServingConveyorFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos,
            Length,
            Width,
            Speed,
            Name,
            LoadingMechanism
        )

        val SortConveyorFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos,
            Length,
            Width,
            Speed,
            Name,
            WasteHeight,
            SortPointCount
        )

        val CommonConveyorFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos,
            Length,
            Width,
            Speed,
            Name
        )

        val BagBreakerFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos
        )

        val SeparatorFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos,
            Type
        )

        val PressFields = listOf(
            Brand,
            Manufacturer,
            Count,
            Photos,
            PassportPhotos,
            Type
        )

        val AdditionalEquipmentFields = listOf(
            Brand,
            Manufacturer,
            Type,
            Count,
            Photos,
            PassportPhotos
        )

        fun getServingConveyorCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, ServingConveyorFields, positions)
        }

        fun getSortConveyorCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, SortConveyorFields, positions)
        }

        fun getCommonConveyorCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, CommonConveyorFields, positions)
        }

        fun getBagBreakerConveyorCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, BagBreakerFields, positions)
        }

        fun getAdditionalEquipmentCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, AdditionalEquipmentFields, positions)
        }

        fun getSeparatorsCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, SeparatorFields, positions)
        }

        fun getPressesCheck(positions: List<Int>, verified: Set<String>): Boolean {
            return verified.checkFieldsForIndexesExists(Prefix, PressFields, positions)
        }
    }

    object SecondaryResources {

        private const val Prefix = "secondaryResources"

        private const val ExtractPercent = "$Prefix.extractPercent"
        private const val Types = "$Prefix.types"

        private val TypeFields = listOf(
            "reference",
            "percent"
        )

        fun getSecondaryResourcesFromVerifiedFields(
            verifiedFields: Set<String>,
            secondaryResources: RemoteSecondaryResources?
        ): SecondaryResourcesCheckedSurvey {
            val typeIndexes = secondaryResources?.types.orEmpty().getIndexes { true }
            val isTypesChecked = verifiedFields.checkFieldsForIndexesExists(
                Types,
                TypeFields, typeIndexes
            )
            return SecondaryResourcesCheckedSurvey(
                extractPercent = verifiedFields.contains(ExtractPercent),
                types = isTypesChecked
            )
        }

        fun getVerifiedValues(
            survey: SecondaryResourcesCheckedSurvey,
            secondaryResources: RemoteSecondaryResources?
        ): List<String> {
            val typesIndexes = secondaryResources?.types.orEmpty().getIndexes { true }
            val typeFields = getRequiredFieldsForPositions(
                Types,
                TypeFields,
                typesIndexes
            )

            return listOfNotNull(
                ExtractPercent.takeIf { survey.extractPercent }
            ) + typeFields.takeIf { survey.types }.orEmpty()
        }
    }

    object Production {

        private const val Prefix = "productsInfo"
        private const val TotalCountPerYear = "${Prefix}.totalCountPerYear"
        private const val ServicesPrefix = "${Prefix}.providedServices"
        private const val ProductsPrefix = "${Prefix}.products"
        private const val Name = "name"
        private const val Volume = "volume"
        private const val Photo = "photo"

        private val ProductFields = listOf(
            Name,
            Volume,
            Photo
        )

        private val ServiceFields = listOf(
            Name,
            Volume
        )

        fun getProductionFromVerifiedFields(
            remoteProduction: RemoteProductsInfo?,
            verifiedFields: Set<String>
        ): ProductionCheckedSurvey {
            val productIndexes = remoteProduction?.products.orEmpty().getIndexes { true }
            val serviceIndexes = remoteProduction?.providedServices.orEmpty().getIndexes { true }
            val isProductChecked =
                verifiedFields.checkFieldsForIndexesExists(ProductsPrefix, ProductFields, productIndexes)
            val isServiceChecked =
                verifiedFields.checkFieldsForIndexesExists(ServicesPrefix, ServiceFields, serviceIndexes)

            return ProductionCheckedSurvey(
                productCapacity = verifiedFields.contains(TotalCountPerYear),
                products = isProductChecked,
                services = isServiceChecked
            )
        }

        fun getVerifiedValues(
            remoteProduction: RemoteProductsInfo,
            checkedSurvey: ProductionCheckedSurvey
        ): List<String> {
            val productIndexes = remoteProduction.products.orEmpty().getIndexes { true }
            val serviceIndexes = remoteProduction.providedServices.orEmpty().getIndexes { true }
            val productsFields = getRequiredFieldsForPositions(ProductsPrefix, ProductFields, productIndexes)
            val serviceFields = getRequiredFieldsForPositions(ServicesPrefix, ServiceFields, serviceIndexes)

            return listOfNotNull(TotalCountPerYear.takeIf { checkedSurvey.productCapacity }) +
                productsFields.takeIf { checkedSurvey.products }.orEmpty() +
                serviceFields.takeIf { checkedSurvey.services }.orEmpty()
        }
    }

    private fun Set<String>.checkFieldsForIndexesExists(
        prefix: String,
        requiredFields: List<String>,
        positions: List<Int>
    ): Boolean {
        if (positions.isEmpty()) return false
        return containsAll(getRequiredFieldsForPositions(prefix, requiredFields, positions))
    }

    fun getRequiredFieldsForPositions(prefix: String, objectFields: List<String>, positions: List<Int>): List<String> {
        val positionPrefixes = positions.map { "$prefix[$it]." }
        return positionPrefixes.flatMap { positionPrefix ->
            objectFields.map { "$positionPrefix$it" }
        }
    }

    private fun <T> List<T>.takeIfOrEmpty(predicate: (List<T>) -> Boolean): List<T> {
        return takeIf { predicate(this) }.orEmpty()
    }
}