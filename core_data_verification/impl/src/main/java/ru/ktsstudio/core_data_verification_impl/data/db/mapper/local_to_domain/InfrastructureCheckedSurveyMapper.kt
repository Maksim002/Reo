package ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class InfrastructureCheckedSurveyMapper @Inject constructor() : Mapper<
    SerializableInfrastructureCheckedSurvey,
    InfrastructureCheckedSurvey
    > {
    override fun map(item: SerializableInfrastructureCheckedSurvey): InfrastructureCheckedSurvey {
        return with(item) {
            InfrastructureCheckedSurvey(
                weightControl = weightControl,
                wheelsWashing = wheelsWashing,
                sewagePlant = sewagePlant,
                radiationControl = radiationControl,
                securityCamera = securityCamera,
                roadNetwork = roadNetwork,
                fences = fences,
                lightSystem = lightSystem,
                securityStation = securityStation,
                asu = asu,
                environmentMonitoring = environmentMonitoring.orFalse()
            )
        }
    }
}