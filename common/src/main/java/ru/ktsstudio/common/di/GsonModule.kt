package ru.ktsstudio.common.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.threeten.bp.Instant
import ru.ktsstudio.common.utils.gson.InstantDateAdapter
import ru.ktsstudio.common.utils.gson.ItemTypeAdapterFactory

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Module
object GsonModule {

    @Provides
    @JvmStatic
    @FeatureScope
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(ItemTypeAdapterFactory())
            .registerTypeAdapter(Instant::class.java, InstantDateAdapter())
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}