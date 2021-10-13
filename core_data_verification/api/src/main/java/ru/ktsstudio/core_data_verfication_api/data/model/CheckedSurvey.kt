package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
@optics
data class GeneralCheckedSurvey(
    val objectStatus: Boolean = false,
//    val power: Boolean = false,
    val name: Boolean = false,
    val subject: Boolean = false,
    val fiasAddress: Boolean = false,
    val addressDescription: Boolean = false
) {
    companion object
}

data class WorkScheduleCheckedSurvey(
    val schedule: Boolean = false,
    val shiftsPerDayCount: Boolean = false,
    val workDaysPerYearCount: Boolean = false,
    val workplacesCount: Boolean = false,
    val managersCount: Boolean = false,
    val workersCount: Boolean = false
)

@optics
sealed class CheckedSurvey {
    abstract val generalCheckedSurvey: GeneralCheckedSurvey
    abstract val workScheduleCheckedSurvey: WorkScheduleCheckedSurvey
    abstract val technicalCheckedSurvey: TechnicalCheckedSurvey

    abstract fun copyWorkSchedule(workScheduleCheckedSurvey: WorkScheduleCheckedSurvey): CheckedSurvey
    abstract fun copyTechnicalCheckedSurvey(technicalCheckedSurvey: TechnicalCheckedSurvey): CheckedSurvey
    abstract fun copyGeneralSurvey(generalSurvey: GeneralCheckedSurvey): CheckedSurvey

    data class WasteTreatmentCheckedSurvey(
        override val generalCheckedSurvey: GeneralCheckedSurvey,
        override val workScheduleCheckedSurvey: WorkScheduleCheckedSurvey,
        override val technicalCheckedSurvey: TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey,
        val infrastructureCheckedSurvey: InfrastructureCheckedSurvey,
        val equipmentCheckedSurvey: EquipmentCheckedSurvey,
        val secondaryResourcesCheckedSurvey: SecondaryResourcesCheckedSurvey
    ) : CheckedSurvey() {

        override fun copyWorkSchedule(workScheduleCheckedSurvey: WorkScheduleCheckedSurvey): CheckedSurvey {
            return this.copy(workScheduleCheckedSurvey = workScheduleCheckedSurvey)
        }

        override fun copyGeneralSurvey(generalSurvey: GeneralCheckedSurvey): CheckedSurvey {
            return this.copy(generalCheckedSurvey = generalSurvey)
        }

        override fun copyTechnicalCheckedSurvey(technicalCheckedSurvey: TechnicalCheckedSurvey): WasteTreatmentCheckedSurvey {
            return this.copy(technicalCheckedSurvey = technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
        }

        data class EquipmentCheckedSurvey(
            val servingConveyors: Boolean = false,
            val sortConveyors: Boolean = false,
            val reverseConveyors: Boolean = false,
            val pressConveyors: Boolean = false,
            val otherConveyors: Boolean = false,
            val bagBreakers: Boolean = false,
            val additionalEquipment: Boolean = false,
            val separators: Set<SeparatorType> = emptySet(),
            val presses: Set<PressType> = emptySet()
        )
    }

    data class WastePlacementCheckedSurvey(
        override val generalCheckedSurvey: GeneralCheckedSurvey,
        override val workScheduleCheckedSurvey: WorkScheduleCheckedSurvey,
        override val technicalCheckedSurvey: TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey,
        val infrastructureCheckedSurvey: InfrastructureCheckedSurvey
    ) : CheckedSurvey() {

        override fun copyWorkSchedule(workScheduleCheckedSurvey: WorkScheduleCheckedSurvey): CheckedSurvey {
            return this.copy(workScheduleCheckedSurvey = workScheduleCheckedSurvey)
        }

        override fun copyTechnicalCheckedSurvey(technicalCheckedSurvey: TechnicalCheckedSurvey): WastePlacementCheckedSurvey {
            return this.copy(technicalCheckedSurvey = technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
        }

        override fun copyGeneralSurvey(generalSurvey: GeneralCheckedSurvey): CheckedSurvey {
            return this.copy(generalCheckedSurvey = generalSurvey)
        }
    }

    @optics
    data class WasteRecyclingCheckedSurvey(
        override val generalCheckedSurvey: GeneralCheckedSurvey,
        override val workScheduleCheckedSurvey: WorkScheduleCheckedSurvey,
        override val technicalCheckedSurvey: TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey,
        val infrastructureCheckedSurvey: InfrastructureCheckedSurvey,
        val productionCheckedSurvey: ProductionCheckedSurvey
    ) : CheckedSurvey() {

        override fun copyWorkSchedule(workScheduleCheckedSurvey: WorkScheduleCheckedSurvey): CheckedSurvey {
            return this.copy(workScheduleCheckedSurvey = workScheduleCheckedSurvey)
        }

        override fun copyGeneralSurvey(generalSurvey: GeneralCheckedSurvey): CheckedSurvey {
            return this.copy(generalCheckedSurvey = generalSurvey)
        }

        override fun copyTechnicalCheckedSurvey(technicalCheckedSurvey: TechnicalCheckedSurvey): WasteRecyclingCheckedSurvey {
            return this.copy(technicalCheckedSurvey = technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
        }
        companion object
    }

    @optics
    data class WasteDisposalCheckedSurvey(
        override val generalCheckedSurvey: GeneralCheckedSurvey,
        override val workScheduleCheckedSurvey: WorkScheduleCheckedSurvey,
        override val technicalCheckedSurvey: TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey,
        val infrastructureCheckedSurvey: InfrastructureCheckedSurvey,
        val productionCheckedSurvey: ProductionCheckedSurvey
    ) : CheckedSurvey() {

        override fun copyWorkSchedule(workScheduleCheckedSurvey: WorkScheduleCheckedSurvey): CheckedSurvey {
            return this.copy(workScheduleCheckedSurvey = workScheduleCheckedSurvey)
        }

        override fun copyGeneralSurvey(generalSurvey: GeneralCheckedSurvey): CheckedSurvey {
            return this.copy(generalCheckedSurvey = generalSurvey)
        }

        override fun copyTechnicalCheckedSurvey(technicalCheckedSurvey: TechnicalCheckedSurvey): WasteDisposalCheckedSurvey {
            return this.copy(technicalCheckedSurvey = technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
        }
        companion object
    }

    companion object {

        fun getEmptyCheckedSurveyByType(type: VerificationObjectType) = when (type) {
            VerificationObjectType.WASTE_TREATMENT -> WasteTreatmentCheckedSurvey(
                generalCheckedSurvey = GeneralCheckedSurvey(),
                workScheduleCheckedSurvey = WorkScheduleCheckedSurvey(),
                technicalCheckedSurvey = TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey(),
                infrastructureCheckedSurvey = InfrastructureCheckedSurvey(/*environmentMonitoring = false*/),
                equipmentCheckedSurvey = WasteTreatmentCheckedSurvey.EquipmentCheckedSurvey(),
                secondaryResourcesCheckedSurvey = SecondaryResourcesCheckedSurvey()
            )
            VerificationObjectType.WASTE_PLACEMENT -> WastePlacementCheckedSurvey(
                generalCheckedSurvey = GeneralCheckedSurvey(),
                workScheduleCheckedSurvey = WorkScheduleCheckedSurvey(),
                technicalCheckedSurvey = TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey(),
                infrastructureCheckedSurvey = InfrastructureCheckedSurvey(/*environmentMonitoring = false*/)
            )
            VerificationObjectType.WASTE_RECYCLING -> WasteRecyclingCheckedSurvey(
                generalCheckedSurvey = GeneralCheckedSurvey(),
                workScheduleCheckedSurvey = WorkScheduleCheckedSurvey(),
                technicalCheckedSurvey = TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey(),
                infrastructureCheckedSurvey = InfrastructureCheckedSurvey(),
                productionCheckedSurvey = ProductionCheckedSurvey()
            )
            VerificationObjectType.WASTE_DISPOSAL -> WasteDisposalCheckedSurvey(
                generalCheckedSurvey = GeneralCheckedSurvey(),
                workScheduleCheckedSurvey = WorkScheduleCheckedSurvey(),
                technicalCheckedSurvey = TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey(),
                infrastructureCheckedSurvey = InfrastructureCheckedSurvey(),
                productionCheckedSurvey = ProductionCheckedSurvey()
            )
        }
    }
}
