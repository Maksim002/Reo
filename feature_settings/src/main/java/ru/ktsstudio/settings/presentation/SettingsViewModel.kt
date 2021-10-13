package ru.ktsstudio.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.common.navigation.intent.ImplicitIntent
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.feature_settings.R
import ru.ktsstudio.settings.domain.SettingsFeature
import ru.ktsstudio.settings.navigation.SettingsNavigator
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
internal class SettingsViewModel @Inject constructor(
    private val settingsFeature: Feature<SettingsFeature.Wish, SettingsFeature.State, SettingsFeature.News>,
    private val resources: ResourceManager,
    private val binder: Binder,
    private val settingsNavigator: SettingsNavigator
) : RxViewModel(), Consumer<SettingsFeature.State> {

    private val actionSubject = Rx2PublishSubject.create<SettingsFeature.Wish>()

    private val stateLiveData = MutableLiveData<SettingsFeature.State>()
    private val navigateLiveData = SingleLiveEvent<ImplicitIntent>()
    private val errorMessageLiveData = SingleLiveEvent<String>()

    val state: LiveData<SettingsFeature.State>
        get() = stateLiveData
    val navigate: LiveData<ImplicitIntent>
        get() = navigateLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageLiveData

    init {
        setupBindings()
        actionSubject.onNext(SettingsFeature.Wish.Load)
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: SettingsFeature.State) {
        stateLiveData.postValue(state)
    }

    fun retry() {
        actionSubject.onNext(SettingsFeature.Wish.Load)
    }

    fun sendSupportEmail() {
        actionSubject.onNext(SettingsFeature.Wish.SendSupportEmail)
    }

    fun callSupportPhoneNumber() {
        actionSubject.onNext(SettingsFeature.Wish.CallSupportPhoneNumber)
    }

    fun logout() {
        actionSubject.onNext(SettingsFeature.Wish.Logout)
    }

    private fun setupBindings() = with(binder) {
        bind(actionSubject to settingsFeature)
        bind(settingsFeature to this@SettingsViewModel)
        bind(settingsFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: SettingsFeature.News) {
        when (news) {
            is SettingsFeature.News.SendEmail -> sendEmail(news.emailAddress)
            is SettingsFeature.News.CallPhone -> callPhoneNumber(news.phoneNumber)
            is SettingsFeature.News.Logout -> settingsNavigator.settingsLogout()
            is SettingsFeature.News.LogoutError -> errorMessageLiveData.postValue(
                resources.getString(
                    R.string.settings_exit_error
                )
            )
        }
    }

    private fun sendEmail(emailAddress: String) {
        val intent = ImplicitIntent.SendEmail(emailAddress)
        navigateLiveData.postValue(intent)
    }

    private fun callPhoneNumber(phoneNumber: String) {
        val intent = ImplicitIntent.CallPhone(phoneNumber)
        navigateLiveData.postValue(intent)
    }
}