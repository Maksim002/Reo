package ru.ktsstudio.app_verification.domain.object_survey.common

import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * Created by Igor Park on 09/02/2021.
 */
data class BooleanValueConsumer<DraftType>(
    private val value: Boolean?,
    private val updater: (Boolean?, DraftType) -> DraftType
) : ValueConsumer<Boolean?, DraftType> {
    override fun get(): Boolean? = value

    override fun consume(value: Boolean?): Updater<DraftType> {
        return copy(value = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return updater(value, updatable)
    }
}
