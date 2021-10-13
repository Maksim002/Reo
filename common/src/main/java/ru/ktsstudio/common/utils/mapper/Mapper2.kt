package ru.ktsstudio.common.utils.mapper

interface Mapper2<From1, From2, To> {
    fun map(item1: From1, item2: From2): To

    fun map(items: List<From1>, item: From2): List<To> {
        return items.map { map(it, item) }
    }

    fun map(item: From1, items: List<From2>): List<To> {
        return items.map { map(item, it) }
    }
}
