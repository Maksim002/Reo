package ru.ktsstudio.core_network_impl.di.modules;

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;

import org.jetbrains.annotations.NotNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import ru.ktsstudio.common.di.FeatureScope;
import ru.ktsstudio.core_network_api.qualifiers.AuthOkhttpClient;
import ru.ktsstudio.core_network_impl.di.qualifiers.AuthFailedInterceptor;
import ru.ktsstudio.core_network_impl.di.qualifiers.AuthHeaderInterceptor;
import ru.ktsstudio.core_network_impl.di.qualifiers.BaseOkhttpClient;
import ru.ktsstudio.core_network_impl.di.qualifiers.DefaultLoggingInterceptor;
import ru.ktsstudio.core_network_impl.di.qualifiers.DefaultOkhttpClient;
import ru.ktsstudio.core_network_impl.di.qualifiers.HeadersLoggingInterceptor;
import ru.ktsstudio.core_network_api.qualifiers.MediaOkhttpClient;
import ru.ktsstudio.core_network_impl.di.qualifiers.VersionInterceptor;

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2019.
 */
@Module
public class OkHttpModule {

    private static final long TIMEOUT = 30;

    @Provides
    @NotNull
    @FeatureScope
    static NetworkFlipperPlugin provideNetworkFlipperPlugin() {
        return new NetworkFlipperPlugin();
    }

    @Provides
    @FeatureScope
    @BaseOkhttpClient
    static OkHttpClient providesBaseOkhttpClient(
            SSLSocketFactory socketFactory
    ) {
        return new OkHttpClient()
                .newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(socketFactory)
                .build();
    }

    @Provides
    @FeatureScope
    @DefaultOkhttpClient
    static OkHttpClient provideHttpClient(
            @BaseOkhttpClient OkHttpClient baseClient,
            @AuthHeaderInterceptor Interceptor authHeaderInterceptor,
            @AuthFailedInterceptor Interceptor authFailedInterceptor,
            @DefaultLoggingInterceptor Interceptor loggingInterceptor,
            @VersionInterceptor Interceptor versionInterceptor,
            @NotNull NetworkFlipperPlugin networkFlipperPlugin
    ) {
        return baseClient
                .newBuilder()
                .addNetworkInterceptor(authHeaderInterceptor)
                .addNetworkInterceptor(versionInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(authFailedInterceptor)
                .addNetworkInterceptor(new FlipperOkhttpInterceptor(networkFlipperPlugin))
                .build();
    }

    @Provides
    @FeatureScope
    @AuthOkhttpClient
    static OkHttpClient provideNoAuthHttpClient(
            @BaseOkhttpClient OkHttpClient baseClient,
            @DefaultLoggingInterceptor Interceptor loggingInterceptor,
            @NotNull NetworkFlipperPlugin networkFlipperPlugin
    ) {
        return baseClient
                .newBuilder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new FlipperOkhttpInterceptor(networkFlipperPlugin))
                .build();
    }

    @Provides
    @FeatureScope
    @MediaOkhttpClient
    static OkHttpClient provideMediaHttpClient(
            SSLSocketFactory socketFactory,
            @HeadersLoggingInterceptor Interceptor loggingInterceptor,
            @AuthHeaderInterceptor Interceptor authHeaderInterceptor,
            @AuthFailedInterceptor Interceptor authFailedInterceptor,
            @VersionInterceptor Interceptor versionInterceptor,
            @NotNull NetworkFlipperPlugin networkFlipperPlugin
    ) {
        return new OkHttpClient()
                .newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(socketFactory)
                .addNetworkInterceptor(authHeaderInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(authFailedInterceptor)
                .addNetworkInterceptor(versionInterceptor)
                .addNetworkInterceptor(new FlipperOkhttpInterceptor(networkFlipperPlugin))
                .build();
    }

    @Provides
    @FeatureScope
    static SSLSocketFactory provideSocketFactory() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
