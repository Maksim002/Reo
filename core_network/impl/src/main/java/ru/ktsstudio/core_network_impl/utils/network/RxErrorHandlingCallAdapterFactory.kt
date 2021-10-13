package ru.ktsstudio.core_network_impl.utils.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import ru.ktsstudio.core_network_api.RetrofitExceptionParser
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor(
    private val exceptionParser: RetrofitExceptionParser,
    private val original: RxJava3CallAdapterFactory
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        return RxCallAdapterWrapper(
            exceptionParser,
            original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        )
    }

    private class RxCallAdapterWrapper<R> internal constructor(
        private val exceptionParser: RetrofitExceptionParser,
        private val wrapped: CallAdapter<R, *>
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        private fun RetrofitExceptionParser.parse(t: Throwable): Throwable {
            return if (t is HttpException) {
                fromRetrofitResponse(t.response()!!)
            } else {
                t
            }
        }

        override fun adapt(call: Call<R>): Any {
            val adaptedCall = wrapped.adapt(call)

            if (adaptedCall is Completable) {
                return adaptedCall.onErrorResumeNext { throwable ->
                    Completable.error(exceptionParser.parse(throwable))
                }
            }

            if (adaptedCall is Single<*>) {
                return adaptedCall.onErrorResumeNext { throwable ->
                    Single.error(exceptionParser.parse(throwable))
                }
            }

            if (adaptedCall is Observable<*>) {
                return adaptedCall.onErrorResumeNext { throwable: Throwable ->
                    Observable.error(exceptionParser.parse(throwable))
                }
            }

            if (adaptedCall is Maybe<*>) {
                return adaptedCall.onErrorResumeNext { throwable: Throwable ->
                    Maybe.error(exceptionParser.parse(throwable))
                }
            }

            throw RuntimeException("Observable Type not supported")
        }
    }

    companion object {
        fun create(
            exceptionParser: RetrofitExceptionParser,
            original: RxJava3CallAdapterFactory = RxJava3CallAdapterFactory.create()
        ): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory(exceptionParser, original)
        }
    }
}