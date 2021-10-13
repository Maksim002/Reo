package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * Created by Igor Park on 17/12/2020.
 */
sealed class ProductionCheckableDataType : CheckableValueConsumer<Unit, ProductionSurveyDraft> {

    override fun get(): Unit = Unit
    override fun consume(value: Unit): Updater<ProductionSurveyDraft> = this

    data class Products(override val isChecked: Boolean) : ProductionCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<ProductionSurveyDraft> =
            copy(isChecked = isChecked)

        override fun update(updatable: ProductionSurveyDraft): ProductionSurveyDraft {
            return updatable.copy(
                productionCheckedSurvey = updatable.productionCheckedSurvey.copy(
                    products = isChecked
                )
            )
        }
    }

    data class Services(override val isChecked: Boolean) : ProductionCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<ProductionSurveyDraft> =
            copy(isChecked = isChecked)

        override fun update(updatable: ProductionSurveyDraft): ProductionSurveyDraft {
            return updatable.copy(
                productionCheckedSurvey = updatable.productionCheckedSurvey.copy(
                    services = isChecked
                )
            )
        }
    }
}
