package ru.ktsstudio.core_data_verfication_api.data

import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
interface RegisterRepository {

    fun fetchReferences(): Single<List<Reference>>
}