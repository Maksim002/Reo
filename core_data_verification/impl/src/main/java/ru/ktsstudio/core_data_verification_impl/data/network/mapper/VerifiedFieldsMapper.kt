package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.12.2020.
 */

class VerifiedFieldsMapper @Inject constructor() :
    Mapper2<RemoteVerificationObject, VerificationObjectType, CheckedSurvey> {
    override fun map(
        item1: RemoteVerificationObject,
        item2: VerificationObjectType
    ): CheckedSurvey {
        return when (item2) {
            VerificationObjectType.WASTE_TREATMENT -> createTreatmentCheckedSurvey(item1)
            VerificationObjectType.WASTE_PLACEMENT -> createPlacementCheckedSurvey(item1)
            VerificationObjectType.WASTE_DISPOSAL -> createDisposalCheckedSurvey(item1)
            VerificationObjectType.WASTE_RECYCLING -> createRecyclingCheckedSurvey(item1)
            else -> CheckedSurvey.getEmptyCheckedSurveyByType(item2)
        }
    }

    private fun createTreatmentCheckedSurvey(remoteObject: RemoteVerificationObject): CheckedSurvey.WasteTreatmentCheckedSurvey {
        val verified = remoteObject.verifiedFields.orEmpty().toSet()
        return CheckedSurvey.WasteTreatmentCheckedSurvey(
            generalCheckedSurvey = VerifiedFieldsMapping.GeneralInformation.getFromVerifiedValues(verified),
            workScheduleCheckedSurvey = VerifiedFieldsMapping.WorkSchedule.getFromVerifiedValues(verified),
            technicalCheckedSurvey = VerifiedFieldsMapping.Technical.getFromTreatmentVerifiedValues(verified),
            infrastructureCheckedSurvey = VerifiedFieldsMapping.Infrastructure.getFromVerifiedValues(verified),
            equipmentCheckedSurvey = CheckedSurvey.WasteTreatmentCheckedSurvey.EquipmentCheckedSurvey(
                servingConveyors = remoteObject.equipment
                    .getIndexes { it.type.getConveyorType() == ConveyorType.SERVING }
                    .let { VerifiedFieldsMapping.Equipment.getServingConveyorCheck(it, verified) },
                sortConveyors = remoteObject.equipment
                    .getIndexes { it.type.getConveyorType() == ConveyorType.SORT }
                    .let { VerifiedFieldsMapping.Equipment.getSortConveyorCheck(it, verified) },
                reverseConveyors = remoteObject.equipment
                    .getIndexes { it.type.getConveyorType() == ConveyorType.REVERSE }
                    .let { VerifiedFieldsMapping.Equipment.getCommonConveyorCheck(it, verified) },
                pressConveyors = remoteObject.equipment
                    .getIndexes { it.type.getConveyorType() == ConveyorType.PRESS }
                    .let { VerifiedFieldsMapping.Equipment.getCommonConveyorCheck(it, verified) },
                otherConveyors = remoteObject.equipment
                    .getIndexes { it.isOtherConveyorType() }
                    .let { VerifiedFieldsMapping.Equipment.getCommonConveyorCheck(it, verified) },
                bagBreakers = remoteObject.equipment
                    .getIndexes { it.type.getConveyorType() == ConveyorType.BAG_BREAKER }
                    .let {
                        VerifiedFieldsMapping.Equipment.getBagBreakerConveyorCheck(
                            it,
                            verified
                        )
                    },
                separators = getSeparatorsCheck(remoteObject, verified),
                presses = getPressesCheck(remoteObject, verified),
                additionalEquipment = remoteObject.equipment
                    .getIndexes {
                        it.isOtherAdditionalEquipment() ||
                            it.type?.type?.name == ReferenceType.ADDITIONAL_EQUIPMENT
                    }
                    .let {
                        VerifiedFieldsMapping.Equipment.getAdditionalEquipmentCheck(
                            it,
                            verified
                        )
                    }
            ),
            secondaryResourcesCheckedSurvey = VerifiedFieldsMapping.SecondaryResources
                .getSecondaryResourcesFromVerifiedFields(verified, remoteObject.secondaryResources)
        )
    }

    private fun getPressesCheck(
        remoteObject: RemoteVerificationObject,
        verified: Set<String>
    ): Set<PressType> {
        return PressType.values()
            .mapNotNull { pressType ->
                val isPressTypeChecked = remoteObject.equipment
                    .getIndexes {
                        (it.isOtherPress() && pressType == PressType.OTHER) ||
                            it.type.getPressType() == pressType
                    }
                    .let { VerifiedFieldsMapping.Equipment.getPressesCheck(it, verified) }

                pressType.takeIf { isPressTypeChecked }
            }
            .toSet()
    }

    private fun getSeparatorsCheck(
        remoteObject: RemoteVerificationObject,
        verified: Set<String>
    ): Set<SeparatorType> {
        return SeparatorType.values()
            .mapNotNull { separatorType ->
                val isSeparatorTypeChecked = remoteObject.equipment
                    .getIndexes {
                        (it.isOtherSeparator() && separatorType == SeparatorType.OTHER) ||
                            it.type.getSeparatorType() == separatorType
                    }
                    .let { VerifiedFieldsMapping.Equipment.getSeparatorsCheck(it, verified) }

                separatorType.takeIf { isSeparatorTypeChecked }
            }
            .toSet()
    }

    private fun createPlacementCheckedSurvey(remoteObject: RemoteVerificationObject): CheckedSurvey.WastePlacementCheckedSurvey {
        val verified = remoteObject.verifiedFields.orEmpty().toSet()
        return CheckedSurvey.WastePlacementCheckedSurvey(
            generalCheckedSurvey = VerifiedFieldsMapping.GeneralInformation.getFromVerifiedValues(verified),
            workScheduleCheckedSurvey = VerifiedFieldsMapping.WorkSchedule.getFromVerifiedValues(verified),
            technicalCheckedSurvey = VerifiedFieldsMapping.Technical.getFromPlacementVerifiedValues(remoteObject, verified),
            infrastructureCheckedSurvey = VerifiedFieldsMapping.Infrastructure.getFromVerifiedValues(verified)
        )
    }

    private fun createRecyclingCheckedSurvey(remoteObject: RemoteVerificationObject): CheckedSurvey.WasteRecyclingCheckedSurvey {
        val verified = remoteObject.verifiedFields.orEmpty().toSet()
        return CheckedSurvey.WasteRecyclingCheckedSurvey(
            generalCheckedSurvey = VerifiedFieldsMapping.GeneralInformation.getFromVerifiedValues(
                verified
            ),
            workScheduleCheckedSurvey = VerifiedFieldsMapping.WorkSchedule.getFromVerifiedValues(
                verified
            ),
            technicalCheckedSurvey = VerifiedFieldsMapping.Technical.getFromRecyclingVerifiedValues(
                verified,
                remoteObject,
                remoteObject.wasteInfo?.wasteTypes.orEmpty().getIndexes()
            ),
            infrastructureCheckedSurvey = VerifiedFieldsMapping.Infrastructure.getFromVerifiedValues(
                verified
            ),
            productionCheckedSurvey = VerifiedFieldsMapping.Production.getProductionFromVerifiedFields(
                remoteObject.productionInfo,
                verified
            )
        )
    }

    private fun createDisposalCheckedSurvey(remoteObject: RemoteVerificationObject): CheckedSurvey.WasteDisposalCheckedSurvey {
        val verified = remoteObject.verifiedFields.orEmpty().toSet()
        return CheckedSurvey.WasteDisposalCheckedSurvey(
            generalCheckedSurvey = VerifiedFieldsMapping.GeneralInformation.getFromVerifiedValues(
                verified
            ),
            workScheduleCheckedSurvey = VerifiedFieldsMapping.WorkSchedule.getFromVerifiedValues(
                verified
            ),
            technicalCheckedSurvey = VerifiedFieldsMapping.Technical.getFromDisposalVerifiedValues(
                verified,
                remoteObject,
                remoteObject.wasteInfo?.wasteTypes.orEmpty().getIndexes()
            ),
            infrastructureCheckedSurvey = VerifiedFieldsMapping.Infrastructure.getFromVerifiedValues(
                verified
            ),
            productionCheckedSurvey = VerifiedFieldsMapping.Production.getProductionFromVerifiedFields(
                remoteObject.productionInfo,
                verified
            )
        )
    }
}