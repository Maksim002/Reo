package ru.ktsstudio.app_verification.domain.tabs

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Ovchinnikov on 02.11.2020.
 */
internal class TabsNewsPublisher : NewsPublisher<
    TabsFeature.Wish,
    TabsFeature.Effect,
    Unit,
    TabsFeature.News
    > {

    override fun invoke(
        action: TabsFeature.Wish,
        effect: TabsFeature.Effect,
        state: Unit
    ): TabsFeature.News {
        return when (effect) {
            is TabsFeature.Effect.SyncData -> TabsFeature.News.SyncData
            is TabsFeature.Effect.NetworkUnavailable -> TabsFeature.News.ShowNetworkUnavailableDialog
        }
    }
}
