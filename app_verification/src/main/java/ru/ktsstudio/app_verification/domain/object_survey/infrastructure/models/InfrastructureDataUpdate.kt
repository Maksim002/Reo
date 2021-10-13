package ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models

import arrow.optics.Optional
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableRoadLength
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableSecurityStaffCount
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableSystemFunctions
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableSystemName
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.roadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.securityStation

/**
 * Created by Igor Park on 12/12/2020.
 */
data class WeightPlatformLengthUpdater(
    val length: Float?,
    val weightPlatformOptics: Optional<InfrastructureSurveyDraft, Float?>
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = length?.stringValue()

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(length = value?.floatValue())
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return weightPlatformOptics.set(updatable, length)
    }
}

data class RoadLengthUpdater(
    val roadLength: Float?
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = roadLength?.stringValue()

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(roadLength = value?.floatValue())
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return InfrastructureSurveyDraft.infrastructureSurvey
            .roadNetwork
            .nullableRoadLength
            .set(updatable, roadLength)
    }
}

data class SecurityStaffCountUpdater(
    val staffCount: Int?
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = staffCount?.toString()

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(staffCount = value?.toIntOrNull())
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return InfrastructureSurveyDraft.infrastructureSurvey
            .securityStation
            .nullableSecurityStaffCount
            .set(updatable, staffCount)
    }
}

data class AsuNameUpdater(
    val name: String?
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = name

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(name = value)
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return InfrastructureSurveyDraft.infrastructureSurvey
            .asu
            .nullableSystemName
            .set(updatable, name)
    }
}

data class AsuFunctionsUpdater(
    val functions: String?
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = functions

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(functions = value)
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return InfrastructureSurveyDraft.infrastructureSurvey
            .asu
            .nullableSystemFunctions
            .set(updatable, functions)
    }
}

data class EnvironmentMonitoringSystemCountUpdater(
    val count: Int?,
    val updater: (InfrastructureSurveyDraft, Int?) -> InfrastructureSurveyDraft
) : ValueConsumer<String?, InfrastructureSurveyDraft> {

    override fun get(): String? = count?.toString()

    override fun consume(value: String?): Updater<InfrastructureSurveyDraft> {
        return copy(count = value?.toIntOrNull())
    }

    override fun update(updatable: InfrastructureSurveyDraft): InfrastructureSurveyDraft {
        return updater.invoke(updatable, count)
    }
}
