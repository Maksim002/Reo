package ru.ktsstudio.feature_auth.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.account.LogoutCleaner
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.data.network.IdentityApi
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.data.error.ErrorReporter
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.core_network_api.exceptions.BadRequestException
import ru.ktsstudio.core_network_api.qualifiers.ClientId
import ru.ktsstudio.core_network_api.qualifiers.ClientSecret
import ru.ktsstudio.feature_auth.domain.auth.IncorrectAuthDataException
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AuthRepositoryImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val fileManager: FileManager,
    private val databaseCleaner: DatabaseCleaner,
    private val logoutCleaner: LogoutCleaner,
    private val schedulers: SchedulerProvider,
    private val identityApi: IdentityApi,
    @ClientId private val clientId: String,
    @ClientSecret private val clientSecret: String
): AuthRepository {
    override fun isLoggedIn(): Single<Boolean> {
        return accountManager.accessToken()
            .map { it.isNullOrEmpty().not() }
            .defaultIfEmpty(false)
            .subscribeOn(schedulers.io)
    }

    override fun logout(): Completable {
        return accountManager.accessToken().flatMapCompletable { token ->
            identityApi.logout(token, clientId, clientSecret)
        }
            .andThen(accountManager.updateTokens(null, null))
            .andThen(fileManager.clearAll())
            .andThen(databaseCleaner.clearDatabase())
            .andThen(logoutCleaner.cleanOnLogout())
            .subscribeOn(schedulers.io)
    }

    override fun login(email: String, password: String): Completable {
        val grantType = "password"
        return identityApi.login(
            grantType = grantType,
            login = email,
            password = password,
            clientId = clientId,
            clientSecret = clientSecret
        )
            .onErrorResumeNext {
                if(it is BadRequestException) {
                    Single.error(IncorrectAuthDataException())
                } else {
                    Single.error(it)
                }
            }
            .flatMapCompletable {
                accountManager.updateTokens(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            }
            .subscribeOn(schedulers.io)
    }
}