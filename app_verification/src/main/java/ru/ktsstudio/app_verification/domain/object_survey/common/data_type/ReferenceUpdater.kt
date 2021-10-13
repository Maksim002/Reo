package ru.ktsstudio.app_verification.domain.object_survey.common.data_type

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
data class ReferenceUpdater<DraftType>(
    val reference: String?,
    val getReference: (String) -> Reference?,
    val setReference: (DraftType, Reference) -> DraftType
) : ValueConsumer<String?, DraftType> {

    override fun get(): String? = reference

    override fun consume(value: String?): Updater<DraftType> {
        return copy(reference = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return reference?.let(getReference)
            ?.let { setReference(updatable, it) }
            ?: updatable
    }
}

data class ReferenceCheckableUpdater<Draft>(
    val reference: String?,
    val getReference: (String) -> Reference?,
    val setReference: (Draft, Reference, Boolean) -> Draft,
    override val isChecked: Boolean
) : CheckableValueConsumer<String?, Draft> {

    override fun get(): String? = reference

    override fun consume(value: String?): Updater<Draft> {
        return copy(reference = value)
    }

    override fun setChecked(isChecked: Boolean): Updater<Draft> {
        return copy(isChecked = isChecked)
    }

    override fun update(updatable: Draft): Draft {
        return reference?.let(getReference)
            ?.let { setReference(updatable, it, isChecked) }
            ?: updatable
    }
}
