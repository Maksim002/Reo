package ru.ktsstudio.common.di.glide

import dagger.Component
import okhttp3.OkHttpClient

/**
 * @author Maxim Ovchinnikov on 30.12.2020.
 */
@Component(
    dependencies = [OkHttpClient::class]
)
interface GlideComponent {

    fun okHttp(): OkHttpClient

    companion object {
        fun create(
            okHttpClient: OkHttpClient
        ): GlideComponent {
            return DaggerGlideComponent.builder()
                .okHttpClient(okHttpClient)
                .build()
        }
    }
}