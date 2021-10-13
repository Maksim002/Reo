package ru.ktsstudio.common.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type
import kotlin.jvm.Throws

internal class InstantDateAdapter : JsonDeserializer<Instant>, JsonSerializer<Instant> {

    private val formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.systemDefault())

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Instant {
        return if (json.isJsonPrimitive) {
            getInstantFromString(json.asString)
        } else {
            DEFAULT_DATE
        }
    }

    override fun serialize(
        src: Instant,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val dateString = LocalDateTime.ofInstant(src, ZoneOffset.UTC).format(formatter)
        return context.serialize(dateString)
    }

    @Throws(JsonParseException::class)
    private fun getInstantFromString(dateStr: String): Instant {
        return LocalDateTime.parse(dateStr, formatter).toInstant(ZoneOffset.UTC)
    }

    companion object {
        private val DEFAULT_DATE = Instant.ofEpochMilli(0)
    }
}