package ru.ktsstudio.reo.domain.measurement.create_measurement

/**
 * Created by Igor Park on 13/10/2020.
 */
sealed class MediaProcessingState {
    object Processing : MediaProcessingState()
    object Completed : MediaProcessingState()
}
