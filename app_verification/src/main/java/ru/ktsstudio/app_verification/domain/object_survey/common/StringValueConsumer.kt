package ru.ktsstudio.app_verification.domain.object_survey.common

import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

data class StringValueConsumer<DraftType>(
    private val value: String?,
    private val updater: (String?, DraftType) -> DraftType
) : ValueConsumer<String?, DraftType> {
    override fun get(): String? = value

    override fun consume(value: String?): Updater<DraftType> {
        return copy(value = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return updater(value, updatable)
    }
}
