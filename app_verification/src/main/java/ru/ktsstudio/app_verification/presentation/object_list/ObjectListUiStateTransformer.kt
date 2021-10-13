package ru.ktsstudio.app_verification.presentation.object_list

import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.ktsstudio.app_verification.domain.object_list.ObjectListFeature
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
class ObjectListUiStateTransformer(
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>
) : (ObjectListFeature.State) -> ObjectListUiState {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    override fun invoke(state: ObjectListFeature.State): ObjectListUiState {
        return ObjectListUiState(
            loading = state.loading,
            error = state.error,
            isFilterSet = state.currentFilter.filterMap.isNotEmpty(),
            searchQuery = state.currentFilter.searchQuery,
            data = state.data.toUiList()
        )
    }

    private fun List<VerificationObject>.toUiList(): List<ObjectListItemUi> {
        return map {
            ObjectListItemUi(
                id = it.id,
                name = it.generalInformation.name,
                address = it.generalInformation.addressDescription.orEmpty(),
                status = it.status?.name.orEmpty(),
                date = formatter.format(it.date),
                type = objectTypeUiMapper.map(it.type)
            )
        }
    }
}
