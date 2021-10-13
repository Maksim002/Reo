package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.11.2020.
 */
class VerificationObjectDbMapper @Inject constructor() : Mapper<
    VerificationObject,
    LocalVerificationObject
    > {

    override fun map(item: VerificationObject): LocalVerificationObject = with(item) {
        LocalVerificationObject(
            id = id,
            location = gpsPoint,
            address = generalInformation.addressDescription.orEmpty(),
            status = item.status,
            date = date,
            objectStatus = objectStatus,
            otherObjectStatusName = otherObjectStatusName,
            generalInformation = generalInformation,
            scheduleSurvey = scheduleSurvey,
            type = type,
            survey = item.survey,
            state = LocalModelState.SUCCESS
        )
    }
}