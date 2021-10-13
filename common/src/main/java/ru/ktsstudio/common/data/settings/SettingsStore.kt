package ru.ktsstudio.common.data.settings

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.utilities.extensions.fromJson
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
class SettingsStore @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val rxSharedPreferences: RxSharedPreferences,
    private val gson: Gson
) {

    fun updateSettings(settings: RemoteSettings): Completable {
        return Completable.fromCallable {
            sharedPreferences.edit()
                .putString(KEY_SETTINGS, gson.toJson(settings))
                .commit()
        }
    }

    fun observeSettings(): Observable<RemoteSettings> {
        return rxSharedPreferences.getString(KEY_SETTINGS)
            .asObservable()
            .map {
                gson.fromJson<RemoteSettings>(it) ?: RemoteSettings(
                    supportEmail = null,
                    supportPhoneNumber = null
                )
            }
    }

    companion object {
        private const val KEY_SETTINGS = "settings"
    }
}