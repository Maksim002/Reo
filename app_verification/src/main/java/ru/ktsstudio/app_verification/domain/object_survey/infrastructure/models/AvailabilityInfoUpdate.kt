package ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

data class AvailabilityUpdater(
    override val isChecked: Boolean,
    val isAvailable: Boolean,
    val setInfo: (InfrastructureSurveyDraft, Boolean, Boolean) -> InfrastructureSurveyDraft
) : CheckableValueConsumer<Boolean, InfrastructureSurveyDraft> {

    override fun get(): Boolean = isAvailable

    override fun consume(value: Boolean): Updater<InfrastructureSurveyDraft> {
        return copy(isAvailable = value)
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return setInfo(updatable, isAvailable, isChecked)
    }

    override fun setChecked(isChecked: Boolean): Updater<InfrastructureSurveyDraft> {
        return copy(isChecked = isChecked)
    }
}

data class NotAvailableReasonUpdater(
    val notAvailableReason: String?,
    val setInfo: (InfrastructureSurveyDraft, String?) -> InfrastructureSurveyDraft
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = notAvailableReason

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(notAvailableReason = value)
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return setInfo(updatable, notAvailableReason)
    }
}
