package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.core.MapK
import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey

/**
 * @author Maxim Ovchinnikov on 24.11.2020.
 */
@optics
sealed class Survey {
    abstract val technicalSurvey: TechnicalSurvey
    abstract val infrastructureSurvey: InfrastructureSurvey

    abstract fun copyTechnicalSurvey(technicalSurvey: TechnicalSurvey): Survey

    data class WasteTreatmentSurvey(
        override val technicalSurvey: TechnicalSurvey.WasteTreatmentTechnicalSurvey,
        override val infrastructureSurvey: InfrastructureSurvey,
        val equipmentSurvey: EquipmentSurvey,
        val secondaryResourcesSurvey: SecondaryResourcesSurvey
    ) : Survey() {

        override fun copyTechnicalSurvey(technicalSurvey: TechnicalSurvey): Survey {
            return this.copy(technicalSurvey = technicalSurvey as TechnicalSurvey.WasteTreatmentTechnicalSurvey)
        }

        @optics
        data class EquipmentSurvey(
            val servingConveyors: MapK<String, ServingConveyor>,
            val sortConveyors: MapK<String, SortConveyor>,
            val reverseConveyors: MapK<String, Conveyor>,
            val pressConveyors: MapK<String, Conveyor>,
            val otherConveyors: MapK<String, Conveyor>,
            val bagBreakers: MapK<String, BagBreakerConveyor>,
            val separators: MapK<String, Separator>,
            val presses: MapK<String, Press>,
            val additionalEquipment: MapK<String, AdditionalEquipment>
        ) {
            companion object
        }
    }

    data class WastePlacementSurvey(
        override val technicalSurvey: TechnicalSurvey.WastePlacementTechnicalSurvey,
        override val infrastructureSurvey: InfrastructureSurvey
    ) : Survey() {

        override fun copyTechnicalSurvey(technicalSurvey: TechnicalSurvey): Survey {
            return this.copy(technicalSurvey = technicalSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey)
        }
    }

    @optics
    data class WasteRecyclingSurvey(
        override val technicalSurvey: TechnicalSurvey.WasteRecyclingTechnicalSurvey,
        override val infrastructureSurvey: InfrastructureSurvey,
        val productionSurvey: ProductionSurvey
    ) : Survey() {

        override fun copyTechnicalSurvey(technicalSurvey: TechnicalSurvey): Survey {
            return this.copy(technicalSurvey = technicalSurvey as TechnicalSurvey.WasteRecyclingTechnicalSurvey)
        }

        companion object
    }

    @optics
    data class WasteDisposalSurvey(
        override val technicalSurvey: TechnicalSurvey.WasteDisposalTechnicalSurvey,
        override val infrastructureSurvey: InfrastructureSurvey,
        val productionSurvey: ProductionSurvey
    ) : Survey() {

        override fun copyTechnicalSurvey(technicalSurvey: TechnicalSurvey): Survey {
            return this.copy(technicalSurvey = technicalSurvey as TechnicalSurvey.WasteDisposalTechnicalSurvey)
        }

        companion object
    }

    companion object
}
