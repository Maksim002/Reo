package ru.ktsstudio.app_verification.domain.object_survey.tech.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.utilities.extensions.orTrue

/**
 * @author Maxim Ovchinnikov on 03.12.2020.
 */
sealed class TechnicalSurveyDataType<T> : ValueConsumer<T, TechnicalSurveyDraft> {
    protected abstract val value: T

    override fun get(): T = value

    data class TkoWeightForLastYearType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(tkoWeightForLastYear = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(tkoWeightForLastYear = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(tkoWeightForLastYear = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(tkoWeightForLastYear = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class OtherWastesWeightForLastYearType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        otherWastesWeightForLastYear = value?.toFloatOrNull()
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(otherWastesWeightForLastYear = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        otherWastesWeightForLastYear = value?.toFloatOrNull()
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(otherWastesWeightForLastYear = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class RecyclingTypeType(
        override val isChecked: Boolean,
        override val value: String?,
        val getReference: (String) -> Reference?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        recyclingType = value?.let(getReference)
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(recyclingType = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class TechProcessCommentType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techProcessComment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(techProcessComment = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techProcessComment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(techProcessComment = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class ObjectAreaType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(objectArea = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(objectArea = isChecked)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(objectArea = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(objectArea = isChecked)
                )
            }
        }
    }

    data class ObjectPhotosType(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectPhotos = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectPhotos = value)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectPhotos = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectPhotos = value)
                )
            }
        }
    }

    data class ProductionAreaType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(productionArea = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(productionArea = isChecked)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(productionArea = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(productionArea = isChecked)
                )
            }
        }
    }

    data class ProductionPhotosType(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionPhotos = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionPhotos = value)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionPhotos = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionPhotos = value)
                )
            }
        }
    }

    data class ObjectBodyAreaType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(objectBodyArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(objectBodyArea = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class PolygonHeightType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(polygonHeight = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(polygonHeight = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class WaterproofingTypeType(
        override val isChecked: Boolean,
        override val value: String?,
        val getReference: (String) -> Reference?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        waterproofingType = value?.let(getReference)
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(waterproofingType = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class ReliefType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(relief = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(relief = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class WastePlacementMapType(
        override val isChecked: Boolean
    ) : CheckableValueConsumer<Unit, TechnicalSurveyDraft> {
        override fun get(): Unit = Unit
        override fun consume(value: Unit): Updater<TechnicalSurveyDraft> = this

        override fun setChecked(isChecked: Boolean): ValueConsumer<Unit, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(wastePlacementMap = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class GroundwaterDepthType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(groundWaterDepth = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(groundwaterDepth = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class CanFloodingType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(canFlooding = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(canFlooding = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasMainEquipment(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(hasMainEquipment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(mainEquipment = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(hasMainEquipment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(mainEquipment = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasSecondaryEquipment(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(hasSecondaryEquipment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(secondaryEquipment = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(hasSecondaryEquipment = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(secondaryEquipment = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasWasteSealantType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasWasteSealant = value,
                        sealantType = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.sealantType
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(wasteSealant = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class SealantType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(sealantType = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class SealantWeight(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(sealantWeight = value?.toFloatOrNull())
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class SealantPhotos(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(sealantPhotos = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class WasteUnloadingAreaType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(wasteUnloadingArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(wasteUnloadingArea = isChecked)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(wasteUnloadingArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(wasteUnloadingArea = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(wasteUnloadingArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(wasteUnloadingArea = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasThermalEnergyProductionType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasThermalEnergyProduction = value,
                        thermalEnergyProductionPower = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.thermalEnergyProductionPower
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(thermalEnergyProduction = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class ThermalEnergyProductionPowerType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        thermalEnergyProductionPower = value?.toFloatOrNull()
                    )
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasTemporaryWasteAccumulationType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasTemporaryWasteAccumulation = value,
                        temporaryWasteArea = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.temporaryWasteArea
                        ),
                        temporaryWastes = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.temporaryWastes
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(temporaryWasteAccumulation = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasTemporaryWasteAccumulation = value,
                        temporaryWasteArea = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.temporaryWasteArea
                        ),
                        temporaryWastes = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.temporaryWastes
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(temporaryWasteAccumulation = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class TemporaryWasteAreaType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(temporaryWasteArea = value?.toFloatOrNull())
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(temporaryWasteArea = value?.toFloatOrNull())
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class TemporaryWastesType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(temporaryWastes = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(temporaryWastes = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class SortDepartmentAreaType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(sortDepartmentArea = value?.toFloatOrNull()),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(sortDepartmentArea = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasCompostAreaType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasCompostArea = value,
                        compostAreaPower = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.compostAreaPower
                        ),
                        compostMaterial = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.compostMaterial
                        ),
                        compostPurpose = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.compostPurpose
                        ),
                        noCompostAreaReason = getDependentValue(
                            hideDependentField = value,
                            currentValue = technicalSurvey.noCompostAreaReason
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(compostArea = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasCompostArea = value,
                        compostAreaPower = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.compostAreaPower
                        ),
                        compostMaterial = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.compostMaterial
                        ),
                        compostPurpose = getDependentValue(
                            hideDependentField = value?.not(),
                            currentValue = technicalSurvey.compostPurpose
                        ),
                        noCompostAreaReason = getDependentValue(
                            hideDependentField = value,
                            currentValue = technicalSurvey.noCompostAreaReason
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(compostArea = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class NoCompostAreaReasonType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(noCompostAreaReason = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(noCompostAreaReason = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class CompostAreaPowerType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostAreaPower = value?.toFloatOrNull())
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostAreaPower = value?.toFloatOrNull())
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class CompostMaterialType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostMaterial = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostMaterial = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class CompostPurposeType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostPurpose = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(compostPurpose = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class HasRdfAreaType(
        override val isChecked: Boolean,
        override val value: Boolean
    ) : TechnicalSurveyDataType<Boolean>(), CheckableValueConsumer<Boolean, TechnicalSurveyDraft> {

        override fun consume(value: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<Boolean, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasRdfArea = value,
                        rdfPower = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.rdfPower
                        ),
                        rdfPurpose = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.rdfPurpose
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey)
                        .copy(rdfArea = isChecked)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        hasRdfArea = value,
                        rdfPower = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.rdfPower
                        ),
                        rdfPurpose = getDependentValue(
                            hideDependentField = value.not(),
                            currentValue = technicalSurvey.rdfPurpose
                        )
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey)
                        .copy(rdfArea = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class RdfPowerType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(rdfPower = value?.toFloatOrNull())
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(rdfPower = value?.toFloatOrNull())
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class RdfPurposeType(
        override val value: String?
    ) : TechnicalSurveyDataType<String?>() {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(rdfPurpose = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(rdfPurpose = value)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class SchemePhotosType(override val isChecked: Boolean) : CheckableValueConsumer<Unit, TechnicalSurveyDraft> {
        override fun get() = Unit

        override fun consume(value: Unit): Updater<TechnicalSurveyDraft> = this

        override fun setChecked(isChecked: Boolean): Updater<TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalCheckedSurvey) {
                is TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey -> copy(
                    technicalCheckedSurvey = technicalCheckedSurvey.copy(schemePhotos = isChecked)
                )
                is TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey -> copy(
                    technicalCheckedSurvey = technicalCheckedSurvey.copy(schemePhotos = isChecked)
                )
                is TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey -> copy(
                    technicalCheckedSurvey = technicalCheckedSurvey.copy(schemePhotos = isChecked)
                )
                is TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey -> copy(
                    technicalCheckedSurvey = technicalCheckedSurvey.copy(schemePhotos = isChecked)
                )
            }
        }
    }

    data class TechSchemaType(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techSchema = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techSchema = value)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techSchema = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(techSchema = value)
                )
            }
        }
    }

    data class GeneralSchemaType(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(generalSchema = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(generalSchema = value)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(generalSchema = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(generalSchema = value)
                )
            }
        }
    }

    data class ProductionSchemaType(
        override val value: List<Media>
    ) : TechnicalSurveyDataType<List<Media>>() {

        override fun consume(value: List<Media>): ValueConsumer<List<Media>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionSchema = value)
                )
                is TechnicalSurvey.WastePlacementTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionSchema = value)
                )
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionSchema = value)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(productionSchema = value)
                )
            }
        }
    }

    data class ReceivedWastesType(
        override val isChecked: Boolean,
        override val value: List<Reference>
    ) : TechnicalSurveyDataType<List<Reference>>(), CheckableValueConsumer<List<Reference>, TechnicalSurveyDraft> {

        override fun consume(value: List<Reference>): ValueConsumer<List<Reference>, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<List<Reference>, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(receivedWastes = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(receivedWastes = isChecked)
                )
                is TechnicalSurvey.WasteDisposalTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(receivedWastes = value),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey)
                        .copy(receivedWastes = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class ReceivedWastesWeightThisYearType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        receivedWastesWeightThisYear = value?.toFloatOrNull()
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(receivedWastesWeightThisYear = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    data class ReceivedWastesWeightLastYearType(
        override val isChecked: Boolean,
        override val value: String?
    ) : TechnicalSurveyDataType<String?>(), CheckableValueConsumer<String?, TechnicalSurveyDraft> {

        override fun consume(value: String?): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, TechnicalSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: TechnicalSurveyDraft): TechnicalSurveyDraft = with(updatable) {
            return when (technicalSurvey) {
                is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> copy(
                    technicalSurvey = technicalSurvey.copy(
                        receivedWastesWeightLastYear = value?.toFloatOrNull()
                    ),
                    technicalCheckedSurvey =
                    (technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey)
                        .copy(receivedWastesWeightLastYear = isChecked)
                )
                else -> error(
                    "Cannot update value=${this::class.qualifiedName} for type=${technicalSurvey::class.qualifiedName}"
                )
            }
        }
    }

    fun <T> getDependentValue(
        hideDependentField: Boolean,
        currentValue: T?
    ): T? {
        return if (hideDependentField.orTrue()) null else currentValue
    }
}
