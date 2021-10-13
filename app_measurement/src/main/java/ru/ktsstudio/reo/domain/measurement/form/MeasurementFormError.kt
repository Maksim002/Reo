package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.form_feature.field.FieldError
import ru.ktsstudio.reo.R

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.11.2020.
 */
sealed class MeasurementFormError : FieldError {

    object Empty : MeasurementFormError() {
        override val messageRes: Int = R.string.measurement_form_empty_error
    }

    object WasteVolume {
        object MinValue : MeasurementFormError() {
            override val messageRes: Int = R.string.measurement_form_waste_volume_min_error
        }
    }

    object WasteVolumeDaily {
        object MinValue : MeasurementFormError() {
            override val messageRes: Int = R.string.measurement_form_waste_daily_volume_min_error
        }
    }

    object WasteWeight {
        object MinValue : MeasurementFormError() {
            override val messageRes: Int = R.string.measurement_form_waste_weight_min_error
        }
    }

    object WasteWeightDaily {
        object MinValue : MeasurementFormError() {
            override val messageRes: Int = R.string.measurement_form_waste_daily_weight_min_error
        }
    }
}
