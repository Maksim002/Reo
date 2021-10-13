package ru.ktsstudio.settings.domain

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
internal class SettingsActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val schedulers: SchedulerProvider
) : Actor<SettingsFeature.State, SettingsFeature.Wish, SettingsFeature.Effect> {
    private val interruptSettingsSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: SettingsFeature.State,
        action: SettingsFeature.Wish
    ): Observable<out SettingsFeature.Effect> {
        return when (action) {
            is SettingsFeature.Wish.Load -> observeSettings()
            is SettingsFeature.Wish.SendSupportEmail -> sendEmail(state)
            is SettingsFeature.Wish.CallSupportPhoneNumber -> callPhoneNumber(state)
            is SettingsFeature.Wish.Logout -> logout()
        }
    }

    private fun observeSettings(): Observable<SettingsFeature.Effect> {
        interruptSettingsLoading()
        return settingsRepository.observeSettings()
            .map<SettingsFeature.Effect> {
                SettingsFeature.Effect.SettingsSuccess(settings = it)
            }
            .onErrorReturn {
                SettingsFeature.Effect.SettingsError(throwable = it)
            }
            .startWithItem(SettingsFeature.Effect.SettingsLoading)
            .takeUntil(interruptSettingsSignal)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun sendEmail(state: SettingsFeature.State): Observable<SettingsFeature.Effect.SupportEmail> {
        return Rx3Observable.fromCallable {
            SettingsFeature.Effect.SupportEmail(
                emailAddress = state.settings.supportEmail
            )
        }
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun callPhoneNumber(state: SettingsFeature.State): Observable<SettingsFeature.Effect.SupportPhone> {
        return Rx3Observable.fromCallable {
            SettingsFeature.Effect.SupportPhone(
                phoneNumber = state.settings.supportPhoneNumber
            )
        }
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun logout(): Observable<SettingsFeature.Effect> {
        return authRepository.logout()
            .toSingleDefault<SettingsFeature.Effect>(SettingsFeature.Effect.LogoutSuccess)
            .toObservable()
            .onErrorReturn {
                SettingsFeature.Effect.LogoutError(it)
            }
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun interruptSettingsLoading() {
        interruptSettingsSignal.onNext(Unit)
    }
}