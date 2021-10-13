package ru.ktsstudio.app_verification.presentation.object_survey.tech

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * Created by Igor Park on 21/01/2021.
 */
data class CheckableTitle<DraftType>(
    override val isChecked: Boolean,
    private val updater: (Boolean, DraftType) -> DraftType
) : CheckableValueConsumer<Unit, DraftType> {
    override fun setChecked(isChecked: Boolean): Updater<DraftType> = copy(isChecked = isChecked)

    override fun get(): Unit = Unit
    override fun consume(value: Unit): Updater<DraftType> = this

    override fun update(updatable: DraftType): DraftType {
        return updater.invoke(
            isChecked,
            updatable
        )
    }
}
