package ru.ktsstudio.app_verification.presentation.tabs

import androidx.lifecycle.LiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.domain.tabs.TabsFeature
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
internal class TabsViewModel @Inject constructor(
    private val tabsFeature: Feature<TabsFeature.Wish, Unit, TabsFeature.News>,
    private val binder: Binder,
) : RxViewModel() {

    private val actionSubject = Rx2PublishSubject.create<TabsFeature.Wish>()

    private val syncLiveData = SingleLiveEvent<Unit>()
    private val networkUnavailableLiveData = SingleLiveEvent<Unit>()

    val sync: LiveData<Unit>
        get() = syncLiveData
    val networkUnavailable: LiveData<Unit>
        get() = networkUnavailableLiveData

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun refreshData() {
        actionSubject.onNext(TabsFeature.Wish.RefreshData)
    }

    private fun setupBindings() = with(binder) {
        bind(actionSubject to tabsFeature)
        bind(tabsFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: TabsFeature.News) {
        when (news) {
            is TabsFeature.News.SyncData -> syncLiveData.postValue(Unit)
            is TabsFeature.News.ShowNetworkUnavailableDialog -> networkUnavailableLiveData.postValue(Unit)
        }
    }
}
