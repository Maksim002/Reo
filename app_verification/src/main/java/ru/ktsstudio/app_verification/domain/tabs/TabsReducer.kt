package ru.ktsstudio.app_verification.domain.tabs

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
internal class TabsReducer : Reducer<Unit, TabsFeature.Effect> {
    override fun invoke(
        state: Unit,
        effect: TabsFeature.Effect
    ) {
    }
}
