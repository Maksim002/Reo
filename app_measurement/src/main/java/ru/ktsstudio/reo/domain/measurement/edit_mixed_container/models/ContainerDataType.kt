package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models

import ru.ktsstudio.common.utils.text_format.TextFormat

sealed class ContainerDataType {
    abstract val format: TextFormat

    object ContainerName : ContainerDataType() {
        override val format: TextFormat = TextFormat.Text
    }

    object ContainerVolume : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object ContainerFullness : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberPercent
    }

    object ContainerEmptyWeight : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object ContainerFilledWeight : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object WasteGroup : ContainerDataType() {
        override val format: TextFormat = TextFormat.Text
    }

    object WasteSubgroup : ContainerDataType() {
        override val format: TextFormat = TextFormat.Text
    }

    object WasteTypeCategory : ContainerDataType() {
        override val format: TextFormat = TextFormat.Text
    }

    object WasteTypeOtherCategoryName : ContainerDataType() {
        override val format: TextFormat = TextFormat.Text
    }

    object WasteVolume : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object WasteWeight : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object WasteVolumeDaily : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }

    object WasteWeightDaily : ContainerDataType() {
        override val format: TextFormat = TextFormat.NumberDecimal()
    }
}
