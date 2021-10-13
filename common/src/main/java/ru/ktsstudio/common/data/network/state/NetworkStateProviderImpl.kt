package ru.ktsstudio.common.data.network.state

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import ru.ktsstudio.utilities.extensions.haveNougat
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
class NetworkStateProviderImpl @Inject constructor(
    private val context: Context
) : NetworkStateProvider {

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("NewApi", "MissingPermission")
    override fun getCurrentNetworkState(): NetworkState {
        return if (haveNougat()) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            connectivityManager.activeNetworkInfo
        }
            .let { networkCheck ->
                if (networkCheck != null) NetworkState.ConnectedState else NetworkState.DisconnectedState
            }
    }
}