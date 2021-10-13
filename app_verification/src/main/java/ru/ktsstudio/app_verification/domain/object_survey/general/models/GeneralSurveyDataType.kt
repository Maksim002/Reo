package ru.ktsstudio.app_verification.domain.object_survey.general.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * Created by Igor Park on 04/12/2020.
 */
data class OtherObjectStatus(
    val value: String?
) : ValueConsumer<String?, GeneralSurveyDraft> {
    override fun get(): String? = value

    override fun consume(
        value: String?
    ): Updater<GeneralSurveyDraft> {
        return copy(value = value)
    }

    override fun update(updatable: GeneralSurveyDraft): GeneralSurveyDraft {
        return updatable.copy(otherStatusName = value)
    }
}

data class FiasAddressType(
    override val isChecked: Boolean,
    val value: String?
) : CheckableValueConsumer<String?, GeneralSurveyDraft> {
    override fun get(): String? = value

    override fun consume(value: String?): Updater<GeneralSurveyDraft> {
        return copy(value = value)
    }

    override fun setChecked(isChecked: Boolean): Updater<GeneralSurveyDraft> {
        return copy(isChecked = isChecked)
    }

    override fun update(updatable: GeneralSurveyDraft): GeneralSurveyDraft {
        return updatable.copy(
            information = updatable.information.copy(),
            generalCheckedSurvey = updatable.generalCheckedSurvey.copy(
                fiasAddress = isChecked
            )
        )
    }
}

data class AddressDescriptionType(
    val value: String?,
    override val isChecked: Boolean
) : CheckableValueConsumer<String?, GeneralSurveyDraft> {
    override fun get(): String? = value

    override fun consume(value: String?): Updater<GeneralSurveyDraft> {
        return copy(value = value)
    }

    override fun setChecked(isChecked: Boolean): Updater<GeneralSurveyDraft> {
        return copy(isChecked = isChecked)
    }

    override fun update(updatable: GeneralSurveyDraft): GeneralSurveyDraft {
        return updatable.copy(
            information = updatable.information.copy(
                addressDescription = value
            ),
            generalCheckedSurvey = updatable.generalCheckedSurvey.copy(
                addressDescription = isChecked
            )
        )
    }
}
