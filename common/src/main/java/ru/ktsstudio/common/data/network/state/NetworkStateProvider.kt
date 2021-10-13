package ru.ktsstudio.common.data.network.state

import io.reactivex.rxjava3.core.Observable

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
interface NetworkStateProvider {
    fun getCurrentNetworkState(): NetworkState
}