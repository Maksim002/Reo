package ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation
import javax.inject.Inject

class VerificationObjectFromDbMapper @Inject constructor(
    private val referenceMapper: Mapper<LocalReference, Reference>
) : Mapper<LocalVerificationObjectWithRelation, VerificationObject> {

    override fun map(item: LocalVerificationObjectWithRelation): VerificationObject = with(item) {
        VerificationObject(
            id = verificationObject.id,
            gpsPoint = verificationObject.location,
            status = item.verificationObject.status,
            objectStatus = verificationObject.objectStatus,
            otherObjectStatusName = verificationObject.otherObjectStatusName,
            date = verificationObject.date,
            scheduleSurvey = verificationObject.scheduleSurvey,
            type = verificationObject.type,
            survey = verificationObject.survey,
            generalInformation = verificationObject.generalInformation,
            checkedSurvey = checkedSurveyHolder.checkedSurvey
        )
    }
}