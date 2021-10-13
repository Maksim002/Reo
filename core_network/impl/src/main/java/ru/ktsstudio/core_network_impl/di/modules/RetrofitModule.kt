package ru.ktsstudio.core_network_impl.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_network_api.RetrofitExceptionParser
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthOkhttpClient
import ru.ktsstudio.core_network_api.qualifiers.AuthRetrofit
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.IdentityRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit
import ru.ktsstudio.core_network_impl.di.qualifiers.DefaultOkhttpClient
import ru.ktsstudio.core_network_api.qualifiers.MediaOkhttpClient
import ru.ktsstudio.core_network_impl.utils.network.RetrofitExceptionParserImpl
import ru.ktsstudio.core_network_impl.utils.network.RxErrorHandlingCallAdapterFactory

/**
 * @author Maxim Myalkin (MaxMyalkin) on 31.10.2019.
 */
@Module
object RetrofitModule {
    @Provides
    @FeatureScope
    @DefaultRetrofit
    fun providesRetrofit(
        @DefaultOkhttpClient client: OkHttpClient,
        gson: Gson,
        @ApiUrl apiUrl: String,
        @DefaultRetrofit rxAdapterFactory: CallAdapter.Factory
    ): Retrofit = buildRetrofitWith(apiUrl, gson, client, rxAdapterFactory)

    @Provides
    @FeatureScope
    @IdentityRetrofit
    fun providesIdentityRetrofit(
        @DefaultOkhttpClient client: OkHttpClient,
        gson: Gson,
        @AuthApiUrl apiUrl: String,
        @DefaultRetrofit rxAdapterFactory: CallAdapter.Factory
    ): Retrofit = buildRetrofitWith(apiUrl, gson, client, rxAdapterFactory)

    @Provides
    @FeatureScope
    @AuthRetrofit
    fun providesAuthRetrofit(
        @AuthOkhttpClient client: OkHttpClient,
        gson: Gson,
        @AuthApiUrl apiUrl: String,
        @DefaultRetrofit rxAdapterFactory: CallAdapter.Factory
    ): Retrofit = buildRetrofitWith(apiUrl, gson, client, rxAdapterFactory)

    @Provides
    @FeatureScope
    @MediaRetrofit
    fun providesMediaRetrofit(
        @MediaOkhttpClient client: OkHttpClient,
        gson: Gson,
        @ApiUrl apiUrl: String,
        @MediaRetrofit rxAdapterFactory: CallAdapter.Factory
    ): Retrofit = buildRetrofitWith(apiUrl, gson, client, rxAdapterFactory)

    @Provides
    fun provideRetrofitExceptionParser(): RetrofitExceptionParser =
        RetrofitExceptionParserImpl()

    @Provides
    @DefaultRetrofit
    fun provideRxCallAdapterFactory(
        schedulers: SchedulerProvider,
        exceptionParser: RetrofitExceptionParser
    ): CallAdapter.Factory {
        return RxErrorHandlingCallAdapterFactory.create(
            exceptionParser = exceptionParser,
            original = RxJava3CallAdapterFactory.createWithScheduler(schedulers.io)
        )
    }

    @Provides
    @MediaRetrofit
    fun provideMediaRxCallAdapterFactory(
        schedulers: SchedulerProvider,
        exceptionParser: RetrofitExceptionParser
    ): CallAdapter.Factory {
        return RxErrorHandlingCallAdapterFactory.create(
            exceptionParser = exceptionParser,
            original = RxJava3CallAdapterFactory.createWithScheduler(schedulers.single)
        )
    }


    private fun buildRetrofitWith(
        apiUrl: String,
        gson: Gson,
        client: OkHttpClient,
        rxAdapterFactory: CallAdapter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapterFactory)
            .client(client)
            .build()
    }
}
