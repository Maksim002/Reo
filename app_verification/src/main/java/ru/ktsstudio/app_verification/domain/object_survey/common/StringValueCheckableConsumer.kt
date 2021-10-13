package ru.ktsstudio.app_verification.domain.object_survey.common

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater

data class StringValueCheckableConsumer<DraftType>(
    override val isChecked: Boolean,
    private val value: String?,
    private val updater: (String?, Boolean, DraftType) -> DraftType
) : CheckableValueConsumer<String?, DraftType> {
    override fun get(): String? = value

    override fun consume(value: String?): Updater<DraftType> {
        return copy(value = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return updater(value, isChecked, updatable)
    }

    override fun setChecked(isChecked: Boolean): Updater<DraftType> {
        return copy(isChecked = isChecked)
    }
}
