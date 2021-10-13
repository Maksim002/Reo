package ru.ktsstudio.common.data.network.state

/**
 * @author Maxim Ovchinnikov on 28.05.2020.
 */
sealed class NetworkState {
    object ConnectedState : NetworkState()
    object DisconnectedState : NetworkState()
}
