package ru.ktsstudio.app_verification.domain.object_survey.common

import org.threeten.bp.Instant
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * @author Maxim Ovchinnikov on 16.01.2021.
 */
data class DateValueConsumer<DraftType>(
    private val value: Instant?,
    private val updater: (Instant?, DraftType) -> DraftType
) : ValueConsumer<Instant?, DraftType> {
    override fun get(): Instant? = value

    override fun consume(value: Instant?): Updater<DraftType> {
        return copy(value = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return updater(value, updatable)
    }
}
