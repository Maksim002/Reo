package ru.ktsstudio.reo.presentation.map

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
data class MnoInfoUiState(
    val isLoading: Boolean,
    val error: Throwable?,
    val info: List<Any>,
)

data class MnoUiInfo(
    val id: String,
    val sourceName: String,
    val sourceType: String,
    val measurementCount: Int,
    val address: String,
    val containers: List<MnoUiContainer>
)

data class MnoUiContainer(
    val typeUnit: String,
    val quantityUnit: Int
)
