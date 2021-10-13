package ru.ktsstudio.common.utils.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ItemTypeAdapterFactory : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>?): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        return object : TypeAdapter<T>() {

            override fun read(`in`: JsonReader?): T {
                var jsonElement = elementAdapter.read(`in`)
                if (jsonElement.isJsonObject) {
                    jsonElement = jsonElement.asJsonObject.parseFieldIfExists("data")
                }
                return delegate.fromJsonTree(jsonElement)
            }

            override fun write(out: JsonWriter?, value: T) {
                delegate.write(out, value)
            }
        }
            .nullSafe()
    }

    fun JsonObject.parseFieldIfExists(fieldName: String): JsonElement {
        if (has(fieldName)) {
            val data = get(fieldName)
            if (data.isJsonObject || data.isJsonArray) {
                return data
            }
        }
        return this
    }
}
