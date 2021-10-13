package ru.ktsstudio.feature_mno_list.presentation.list

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal data class MnoListItemUi(
    val id: String,
    val sourceName: String,
    val measureCount: Int,
    val categoryName: String,
    val address: String,
    val containers: List<ContainerItemUi>
)