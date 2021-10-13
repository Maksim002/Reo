package ru.ktsstudio.reo.presentation.measurement.list

import androidx.annotation.ColorRes
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.list.MeasurementListFeature

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal class MeasurementListUiStateTransformer :
        (MeasurementListFeature.State) -> MeasurementListUiState {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm")
        .withZone(ZoneId.systemDefault())

    override fun invoke(state: MeasurementListFeature.State): MeasurementListUiState {
        return MeasurementListUiState(
            loading = state.loading,
            error = state.error,
            data = state.data.toUiElements(),
            searchQuery = state.currentFilter.searchQuery,
            isFilterSet = state.currentFilter.filterMap.isNotEmpty()
        )
    }

    private fun List<Pair<Measurement, Mno>>.toUiElements(): List<MeasurementListItemUi> {

        @ColorRes
        fun MeasurementStatus.getColor(): Int {
            return when (id) {
                MeasurementRepository.REVISION_STATUS_ID -> R.color.pomegranate
                else -> R.color.colorPrimary
            }
        }

        return map { (measurement, mno) ->
            MeasurementListItemUi(
                id = measurement.measurementLocalId,
                sourceName = mno.source.name,
                categoryName = mno.source.category.name,
                measurementAvailability = measurement.isPossible,
                address = mno.sourceAddress.address,
                measurementCreatedDate = formatter.format(measurement.date),
                measurementStatus = measurement.status,
                statusColor = measurement.status.getColor()
            )
        }
    }
}
