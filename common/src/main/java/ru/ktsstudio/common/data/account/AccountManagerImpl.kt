package ru.ktsstudio.common.data.account

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AccountManagerImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : AccountManager {
    override fun accessToken(): Maybe<String> {
        return Maybe.fromCallable {
            sharedPrefs.getString(KEY_ACCESS_TOKEN, null)
        }
    }

    override fun refreshToken(): Maybe<String> {
        return Maybe.fromCallable {
            sharedPrefs.getString(KEY_REFRESH_TOKEN, null)
        }
    }

    override fun updateTokens(accessToken: String?, refreshToken: String?): Completable {
        return Completable.fromCallable {
            sharedPrefs.edit()
                .putOrDeleteIfNull(KEY_ACCESS_TOKEN, accessToken)
                .putOrDeleteIfNull(KEY_REFRESH_TOKEN, refreshToken)
                .commit()
        }
    }

    override fun updateAccessToken(accessToken: String?): Completable {
        return Completable.fromCallable {
            sharedPrefs.edit()
                .putOrDeleteIfNull(KEY_ACCESS_TOKEN, accessToken)
                .commit()
        }
    }

    private fun SharedPreferences.Editor.putOrDeleteIfNull(key: String, value: String?): SharedPreferences.Editor {
        value?.let { putString(key, value) }
            ?: remove(key)
        return this
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}