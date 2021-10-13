package ru.ktsstudio.core_data_verfication_api.data.model

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
data class VerificationObjectWithCheckedSurvey(
    val verificationObject: VerificationObject,
    val checkedSurvey: CheckedSurvey
)
