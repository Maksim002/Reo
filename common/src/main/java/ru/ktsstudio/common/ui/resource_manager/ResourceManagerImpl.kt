package ru.ktsstudio.common.ui.resource_manager

import android.content.Context
import android.util.LruCache
import androidx.core.content.ContextCompat
import javax.inject.Inject

internal class ResourceManagerImpl @Inject constructor(
    private val context: Context,
    private val cacheSize: Int
) : ResourceManager {

    private val stringCache = LruCache<Int, String>(cacheSize)
    private val stringWithArgsCache = LruCache<KeyWithArgs<Int>, String>(cacheSize)

    override fun getString(resId: Int): String {
        return wrapOperationWithCacheCheck(stringCache, resId, context::getString)
    }

    override fun getString(resId: Int, vararg formatArgs: Any?): String {
        val keyWithArgs = KeyWithArgs(resId, formatArgs.filterNotNull().toList())
        return wrapOperationWithCacheCheck(stringWithArgsCache, keyWithArgs) { context.getString(it.key, *formatArgs) }
    }

    override fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    @Suppress("SpreadOperator")
    override fun getPluralString(id: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getDimensionPixelSize(sizeRes: Int): Int {
        return context.resources.getDimensionPixelSize(sizeRes)
    }

    private fun <K, V> wrapOperationWithCacheCheck(cache: LruCache<K, V>, key: K, getter: (K) -> V): V {
        val cached = cache.get(key)
        if (cached != null) return cached
        val newItem = getter(key)
        cache.put(key, newItem)
        return newItem
    }

    data class KeyWithArgs<K>(
        val key: K,
        val args: List<Any>
    )
}
