package ru.ktsstudio.core_data_verification_impl.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.RegisterRepository
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.network.RegisterNetworkApi
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
class RegisterRepositoryImpl @Inject constructor(
    private val registerNetworkApi: RegisterNetworkApi,
    private val referenceRemoteMapper: Mapper<RemoteReference, Reference>,
    private val schedulerProvider: SchedulerProvider
) : RegisterRepository {

    override fun fetchReferences(): Single<List<Reference>> {
        return registerNetworkApi.getReferences()
            .map { it.filter { it.type.name != null } }
            .map(referenceRemoteMapper::map)
            .subscribeOn(schedulerProvider.io)
    }
}