package ru.ktsstudio.common.domain.filter.data

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterDataReducer<DataType> :
    Reducer<FilterDataFeature.State<DataType>, FilterDataFeature.Effect<DataType>> {
    override fun invoke(
        state: FilterDataFeature.State<DataType>,
        effect: FilterDataFeature.Effect<DataType>
    ): FilterDataFeature.State<DataType> {
        return when (effect) {
            is FilterDataFeature.Effect.ChangeData<*> -> {
                val updatedData = state.data +
                    mapOf(effect.key to effect.data as List<DataType>)
                state.copy(
                    data = updatedData
                )
            }
        }
    }
}