package ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class SerializableInfrastructureCheckedSurveyMapper @Inject constructor() : Mapper<
    InfrastructureCheckedSurvey,
    SerializableInfrastructureCheckedSurvey
    > {
    override fun map(item: InfrastructureCheckedSurvey): SerializableInfrastructureCheckedSurvey {
        return with(item) {
            SerializableInfrastructureCheckedSurvey(
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
