package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */

data class SerializableGeneralInformationCheckedSurvey(
    @SerializedName("objectName")
    val objectName: Boolean = false,
    @SerializedName("objectStatus")
    val objectStatus: Boolean = false,
    @SerializedName("subject")
    val subject: Boolean = false,
    @SerializedName("fiasAddress")
    val fiasAddress: Boolean = false,
    @SerializedName("addressDescription")
    val addressDescription: Boolean = false,
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.GENERAL
)

data class SerializableWorkScheduleCheckedSurvey(
    @SerializedName("schedule")
    val schedule: Boolean = false,
    @SerializedName("shiftsPerDayCount")
    val shiftsPerDayCount: Boolean = false,
    @SerializedName("daysPerYearCount")
    val daysPerYearCount: Boolean = false,
    @SerializedName("workplacesCount")
    val workplacesCount: Boolean = false,
    @SerializedName("managersCount")
    val managersCount: Boolean = false,
    @SerializedName("workersCount")
    val workersCount: Boolean = false
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.SCHEDULE
}

sealed class SerializableCheckedSurvey {

    abstract val generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey
    abstract val workScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey
    abstract val type: VerificationObjectType

    data class SerializableWasteTreatmentCheckedSurvey(
        @SerializedName("generalInformationCheckedSurvey")
        override val generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey,
        @SerializedName("workScheduleCheckedSurvey")
        override val workScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey,
        @SerializedName("technicalCheckedSurvey")
        val technicalCheckedSurvey: SerializableTechnicalCheckedSurvey,
        @SerializedName("infrastructureCheckedSurvey")
        val infrastructureCheckedSurvey: SerializableInfrastructureCheckedSurvey,
        @SerializedName("equipmentCheckedSurvey")
        val equipmentCheckedSurvey: SerializableEquipmentCheckedSurvey,
        @SerializedName("secondaryResourcesCheckedSurvey")
        val secondaryResourcesCheckedSurvey: SerializableSecondaryResourcesCheckedSurvey
    ) : SerializableCheckedSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_TREATMENT

        data class SerializableTechnicalCheckedSurvey(
            @SerializedName("tkoWeightForLastYear")
            val tkoWeightForLastYear: Boolean = false,
            @SerializedName("otherWastesWeightForLastYear")
            val otherWastesWeightForLastYear: Boolean = false,
            @SerializedName("objectArea")
            val objectArea: Boolean = false,
            @SerializedName("productionArea")
            val productionArea: Boolean = false,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Boolean = false,
            @SerializedName("sortDepartmentArea")
            val sortDepartmentArea: Boolean = false,
            @SerializedName("hasCompostArea")
            val hasCompostArea: Boolean = false,
            @SerializedName("hasRdfArea")
            val hasRdfArea: Boolean = false,
            @SerializedName("schemePhotos")
            val schemePhotos: Boolean = false
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.TECHNICAL
        }

        data class SerializableEquipmentCheckedSurvey(
            @SerializedName("servingConveyors")
            val servingConveyors: Boolean = false,
            @SerializedName("sortConveyors")
            val sortConveyors: Boolean = false,
            @SerializedName("reverseConveyors")
            val reverseConveyors: Boolean = false,
            @SerializedName("pressConveyors")
            val pressConveyors: Boolean = false,
            @SerializedName("otherConveyors")
            val otherConveyors: Boolean = false,
            @SerializedName("bagBreakers")
            val bagBreakers: Boolean = false,
            @SerializedName("additional")
            val additionalEquipment: Boolean = false,
            @SerializedName("separators")
            val separators: Set<SeparatorType> = emptySet(),
            @SerializedName("presses")
            val presses: Set<PressType> = emptySet()
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.EQUIPMENT
        }

        data class SerializableSecondaryResourcesCheckedSurvey(
            @SerializedName("extractPercent")
            val extractPercent: Boolean = false,
            @SerializedName("types")
            val types: Boolean = false
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.SECONDARY_RESOURCE
        }
    }

    data class SerializableWastePlacementCheckedSurvey(
        @SerializedName("generalInformationCheckedSurvey")
        override val generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey,
        @SerializedName("workScheduleCheckedSurvey")
        override val workScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey,
        @SerializedName("technicalCheckedSurvey")
        val technicalCheckedSurvey: SerializableTechnicalCheckedSurvey,
        @SerializedName("infrastructureCheckedSurvey")
        val infrastructureCheckedSurvey: SerializableInfrastructureCheckedSurvey
    ) : SerializableCheckedSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_PLACEMENT

        data class SerializableTechnicalCheckedSurvey(
            @SerializedName("tkoWeightForLastYear")
            val tkoWeightForLastYear: Boolean = false,
            @SerializedName("otherWastesWeightForLastYear")
            val otherWastesWeightForLastYear: Boolean = false,
            @SerializedName("hasCompostArea")
            val hasCompostArea: Boolean = false,
            @SerializedName("hasRdfArea")
            val hasRdfArea: Boolean = false,
            @SerializedName("objectArea")
            val objectArea: Boolean = false,
            @SerializedName("productionArea")
            val productionArea: Boolean = false,
            @SerializedName("objectBodyArea")
            val objectBodyArea: Boolean = false,
            @SerializedName("polygonHeight")
            val polygonHeight: Boolean = false,
            @SerializedName("waterproofingType")
            val waterproofingType: Boolean = false,
            @SerializedName("relief")
            val relief: Boolean = false,
            @SerializedName("groundwaterDepth")
            val groundwaterDepth: Boolean = false,
            @SerializedName("wastePlacementMap")
            val wastePlacementMap: Boolean = false,
            @SerializedName("canFlooding")
            val canFlooding: Boolean = false,
            @SerializedName("hasWasteSealant")
            val hasWasteSealant: Boolean = false,
            @SerializedName("schemePhotos")
            val schemePhotos: Boolean = false
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.TECHNICAL
        }
    }

    data class SerializableWasteRecyclingCheckedSurvey(
        @SerializedName("generalInformationCheckedSurvey")
        override val generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey,
        @SerializedName("workScheduleCheckedSurvey")
        override val workScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey,
        @SerializedName("technicalCheckedSurvey")
        val technicalCheckedSurvey: SerializableTechnicalCheckedSurvey,
        @SerializedName("infrastructureCheckedSurvey")
        val infrastructureCheckedSurvey: SerializableInfrastructureCheckedSurvey,
        @SerializedName("productionCheckedSurvey")
        val productionCheckedSurvey: SerializableProductionCheckedSurvey
    ) : SerializableCheckedSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_RECYCLING

        data class SerializableTechnicalCheckedSurvey(
            @SerializedName("recyclingType")
            val recyclingType: Boolean = false,
            @SerializedName("techProcessComment")
            val techProcessComment: Boolean = false,
            @SerializedName("objectArea")
            val objectArea: Boolean = false,
            @SerializedName("productionArea")
            val productionArea: Boolean = false,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Boolean = false,
            @SerializedName("hasTemporaryWasteAccumulation")
            val hasTemporaryWasteAccumulation: Boolean = false,
            @SerializedName("receivedWastes")
            val receivedWastes: Boolean = false,
            @SerializedName("receivedWastesWeightThisYear")
            val receivedWastesWeightThisYear: Boolean = false,
            @SerializedName("receivedWastesWeightLastYear")
            val receivedWastesWeightLastYear: Boolean = false,
            @SerializedName("hasMainEquipment")
            val hasMainEquipment: Boolean = false,
            @SerializedName("hasSecondaryEquipment")
            val hasSecondaryEquipment: Boolean = false,
            @SerializedName("schemePhotos")
            val schemePhotos: Boolean = false
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.TECHNICAL
        }
    }

    data class SerializableWasteDisposalCheckedSurvey(
        @SerializedName("generalInformationCheckedSurvey")
        override val generalInformationCheckedSurvey: SerializableGeneralInformationCheckedSurvey,
        @SerializedName("workScheduleCheckedSurvey")
        override val workScheduleCheckedSurvey: SerializableWorkScheduleCheckedSurvey,
        @SerializedName("technicalCheckedSurvey")
        val technicalCheckedSurvey: SerializableTechnicalCheckedSurvey,
        @SerializedName("infrastructureCheckedSurvey")
        val infrastructureCheckedSurvey: SerializableInfrastructureCheckedSurvey,
        @SerializedName("productionCheckedSurvey")
        val productionCheckedSurvey: SerializableProductionCheckedSurvey
    ) : SerializableCheckedSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_DISPOSAL

        data class SerializableTechnicalCheckedSurvey(
            @SerializedName("objectArea")
            val objectArea: Boolean = false,
            @SerializedName("productionArea")
            val productionArea: Boolean = false,
            @SerializedName("techProcessComment")
            val techProcessComment: Boolean = false,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Boolean = false,
            @SerializedName("hasThermalEnergyProduction")
            val hasThermalEnergyProduction: Boolean = false,
            @SerializedName("hasTemporaryWasteAccumulation")
            val hasTemporaryWasteAccumulation: Boolean = false,
            @SerializedName("receivedWastes")
            val receivedWastes: Boolean = false,
            @SerializedName("hasMainEquipment")
            val hasMainEquipment: Boolean = false,
            @SerializedName("hasSecondaryEquipment")
            val hasSecondaryEquipment: Boolean = false,
            @SerializedName("schemePhotos")
            val schemePhotos: Boolean = false
        ) {
            @SerializedName("surveySubtype")
            val surveySubtype: SurveySubtype = SurveySubtype.TECHNICAL
        }
    }
}