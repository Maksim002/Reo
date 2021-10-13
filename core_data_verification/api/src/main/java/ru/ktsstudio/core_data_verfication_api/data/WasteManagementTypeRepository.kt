package ru.ktsstudio.core_data_verfication_api.data

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
interface WasteManagementTypeRepository {
    fun observeAllWasteManagementTypes(): Observable<List<VerificationObjectType>>
}