package ru.ktsstudio.core_data_verification_impl.data.db.store

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_verification_impl.data.db.dao.CheckedSurveyDao
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class CheckedSurveyStore @Inject constructor(
    private val checkedSurveyDao: CheckedSurveyDao
) {
    fun save(checkedSurveys: List<LocalCheckedSurvey>): Completable {
        return checkedSurveyDao.insertLocalCheckSurveys(checkedSurveys)
    }

    fun getCheckedSurveyByObjectId(id: String): Maybe<LocalCheckedSurvey> {
        return checkedSurveyDao.getById(id)
    }

    fun observeCheckedSurveyByObjectId(id: String): Observable<LocalCheckedSurvey> {
        return checkedSurveyDao.observeCheckedSurvey(id)
    }
}