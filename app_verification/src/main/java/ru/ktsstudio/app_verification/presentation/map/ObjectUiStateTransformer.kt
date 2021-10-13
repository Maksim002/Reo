package ru.ktsstudio.app_verification.presentation.map

import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoFeature
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
class ObjectUiStateTransformer(
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>
) : (ObjectInfoFeature.State) -> ObjectInfoUiState {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    override fun invoke(state: ObjectInfoFeature.State): ObjectInfoUiState {
        return ObjectInfoUiState(
            isLoading = state.isLoading,
            error = state.error,
            info = state.infoList.flatMapIndexed { index, verificationObject ->
                val mnoIsNotLast = index < state.infoList.size - 1
                val uiState = mapToUiInfo(verificationObject)

                if (mnoIsNotLast) listOf(uiState, DividerItem()) else listOf(uiState)
            }
        )
    }

    private fun mapToUiInfo(verificationObject: VerificationObject): ObjectUiInfo = with(verificationObject) {
        return ObjectUiInfo(
            id = id,
            location = gpsPoint,
            name = generalInformation.name,
            address = generalInformation.addressDescription.orEmpty(),
            date = formatter.format(date),
            status = status?.name.orEmpty(),
            type = objectTypeUiMapper.map(type)
        )
    }
}
