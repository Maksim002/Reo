package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.floatValue

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.12.2020.
 */
object SecondaryResourcesDataType {

    data class ExtractPercent(
        override val isChecked: Boolean,
        val value: String?
    ) : CheckableValueConsumer<String?, SecondaryResourcesSurveyDraft> {

        override fun get(): String? = value

        override fun consume(value: String?): ValueConsumer<String?, SecondaryResourcesSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): ValueConsumer<String?, SecondaryResourcesSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: SecondaryResourcesSurveyDraft): SecondaryResourcesSurveyDraft {
            return updatable.copy(
                secondaryResources = updatable.secondaryResources.copy(
                    extractPercent = value?.floatValue()
                ),
                checkedSecondaryResources = updatable.checkedSecondaryResources.copy(
                    extractPercent = isChecked
                )
            )
        }
    }

    data class SecondaryResourceTypeConveyor(
        override val isChecked: Boolean
    ) : CheckableValueConsumer<Unit, SecondaryResourcesSurveyDraft> {

        override fun get(): Unit = Unit

        override fun consume(value: Unit): Updater<SecondaryResourcesSurveyDraft> = this

        override fun setChecked(isChecked: Boolean): Updater<SecondaryResourcesSurveyDraft> =
            copy(isChecked = isChecked)

        override fun update(updatable: SecondaryResourcesSurveyDraft): SecondaryResourcesSurveyDraft {
            return updatable.copy(
                checkedSecondaryResources = updatable.checkedSecondaryResources.copy(
                    types = isChecked
                )
            )
        }
    }
}
