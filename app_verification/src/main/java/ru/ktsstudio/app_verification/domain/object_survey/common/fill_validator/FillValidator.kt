package ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
interface FillValidator<Entity> {
    fun isFilled(entity: Entity): Boolean
}
