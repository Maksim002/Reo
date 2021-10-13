package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import ru.ktsstudio.common.presentation.filter.UiFilterItem

/**
 * Created by Igor Park on 22/10/2020.
 */
sealed class ContainerField {
    abstract val title: Int

    data class Title(
        @StringRes override val title: Int,
        @DimenRes val textSize: Int
    ) : ContainerField()

    data class Prefilled(
        @StringRes
        override val title: Int,
        override val dataType: ContainerDataType,
        override val value: String?,
        override val isRequired: Boolean
    ) : ContainerField(),
        DataField

    data class Selector(
        @StringRes override val title: Int,
        @StringRes val hint: Int,
        override val dataType: ContainerDataType,
        override val value: String?,
        val dropDownItems: List<UiFilterItem>,
        override val isRequired: Boolean
    ) : ContainerField(),
        DataField

    data class OpenField(
        @StringRes override val title: Int,
        @StringRes val hint: Int,
        @StringRes val error: Int? = null,
        override val dataType: ContainerDataType,
        override val value: String?,
        override val isRequired: Boolean
    ) : ContainerField(),
        DataField

    data class Autofill(
        @StringRes override val title: Int,
        @StringRes val hint: Int,
        override val dataType: ContainerDataType,
        override val value: String?,
        override val isRequired: Boolean
    ) : ContainerField(),
        DataField
}

interface DataField {
    val dataType: ContainerDataType
    val value: String?
    val isRequired: Boolean
}

fun List<DataField>.validate(): Boolean {
    return all {
        it.isRequired.not() || it.value.isNullOrBlank().not()
    }
}
