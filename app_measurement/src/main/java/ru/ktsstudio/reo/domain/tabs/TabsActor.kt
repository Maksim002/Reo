package ru.ktsstudio.reo.domain.tabs

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.ktsstudio.common.data.network.state.NetworkState
import ru.ktsstudio.common.data.network.state.NetworkStateProvider

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
internal class TabsActor(
    private val networkStateProvider: NetworkStateProvider
) : Actor<
    Unit,
    TabsFeature.Wish,
    TabsFeature.Effect
    > {

    override fun invoke(
        state: Unit,
        action: TabsFeature.Wish
    ): Observable<TabsFeature.Effect> {
        return when (action) {
            is TabsFeature.Wish.RefreshData -> refreshData()
        }
    }

    private fun refreshData(): Observable<TabsFeature.Effect> {
        return Observable.fromCallable {
            when (networkStateProvider.getCurrentNetworkState()) {
                NetworkState.ConnectedState -> TabsFeature.Effect.SyncData
                NetworkState.DisconnectedState -> TabsFeature.Effect.NetworkUnavailable
            }
        }
    }
}
