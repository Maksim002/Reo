package ru.ktsstudio.reo.presentation.map

import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.reo.domain.map.mno_info.MnoInfoFeature

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
class MnoUiStateTransformer : (MnoInfoFeature.State) -> MnoInfoUiState {

    override fun invoke(state: MnoInfoFeature.State): MnoInfoUiState {
        return MnoInfoUiState(
            isLoading = state.isLoading,
            error = state.error,
            info = state.infoList.flatMapIndexed { index, mnoWithMeasurements ->
                val mnoIsNotLast = index < state.infoList.size - 1
                val uiState = mapToUiInfo(mnoWithMeasurements.first, mnoWithMeasurements.second)

                if (mnoIsNotLast) listOf(uiState, DividerItem()) else listOf(uiState)
            }
        )
    }

    private fun mapToUiInfo(mno: Mno, measurements: List<Measurement>): MnoUiInfo = with(mno) {
        return MnoUiInfo(
            id = objectInfo.mnoId,
            sourceName = source.name,
            sourceType = source.type,
            measurementCount = measurements.size,
            address = sourceAddress.address,
            containers = containers.groupBy { it.type }.map {
                MnoUiContainer(typeUnit = it.key.name, quantityUnit = it.value.size)
            }
        )
    }
}
