package ru.ktsstudio.feature_mno_list.presentation.list

import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.feature_mno_list.domain.list.MnoListFeature
import ru.ktsstudio.utilities.extensions.orDefault

/**
 * @author Maxim Myalkin (MaxMyalkin) on 08.10.2020.
 */
internal class MnoListUiStateTransformer : (MnoListFeature.State) -> MnoListUiState {
    override fun invoke(state: MnoListFeature.State): MnoListUiState {
        return MnoListUiState(
            loading = state.loading,
            error = state.error,
            data = mapToUiElements(state.mnoList, state.mnoIdToMeasurementsMap),
            searchQuery = state.currentFilter.searchQuery,
            isFilterSet = state.currentFilter.filterMap.isNotEmpty(),
        )
    }

    private fun mapToUiElements(
        mnoList: List<Mno>,
        mnoIdToMeasurementMap: Map<String, List<Measurement>>
    ): List<MnoListItemUi> {
        return mnoList.map {
            val measurements = mnoIdToMeasurementMap[it.objectInfo.mnoId]
            MnoListItemUi(
                id = it.objectInfo.mnoId,
                sourceName = it.source.name,
                measureCount = measurements?.size.orDefault(0),
                address = it.sourceAddress.address,
                categoryName = it.source.category.name,
                containers = it.containers.groupBy { it.type }.map {
                    ContainerItemUi(name = it.key.name, count = it.value.size)
                }
            )
        }
    }
}