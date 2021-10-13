package ru.ktsstudio.reo.presentation.measurement.morphology.section

import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * Created by Igor Park on 21/10/2020.
 */
class EditMorphologyUiStateTransformer(private val resources: ResourceManager) :
        (EditMorphologyFeature.State) -> EditMorphologyUiState {

    override fun invoke(state: EditMorphologyFeature.State): EditMorphologyUiState =
        with(state) {
            val title = listOf(
                TitleItem(resources.getString(R.string.edit_mixed_container_fill_in_hint))
            )

            val morphologyTypesTitle = listOf(
                SmallTitleItem(text = resources.getString(R.string.edit_morphology_waste_categories))
            )

            val morphologyList = state.morphologyList
                .takeIfNotEmpty()
                ?.let { morphologyList ->
                    morphologyTypesTitle + morphologyList
                }
                .orEmpty()

            val addEntityItem = listOf(
                AddEntityItem(
                    text = resources.getString(R.string.edit_morphology_add_category),
                    icon = R.drawable.ic_plus,
                    qualifier = Unit
                )
            )

            return EditMorphologyUiState(
                morphologyList = title + morphologyList + addEntityItem,
                isLoading = isLoading,
                error = error
            )
        }
}
