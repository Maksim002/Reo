package ru.ktsstudio.core_data_measurement_api.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer

interface MnoRepository {
    fun fetchMnos(): Single<List<Mno>>
    fun getMnoListByIds(ids: List<String>): Single<List<Mno>>
    fun observeMnoList(): Observable<List<Mno>>
    fun observeMnoById(mnoId: String): Observable<Mno>
    fun observeMnoListByIds(mnoIds: List<String>): Observable<List<Mno>>
    fun getMnoContainersByMnoId(mnoId: String): Single<List<MnoContainer>>
    fun getMnoContainerById(containerId: String): Maybe<MnoContainer>
    fun getMnoById(id: String): Maybe<Mno>
}