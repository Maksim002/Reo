package ru.ktsstudio.common.utils.text_format

sealed class TextFormat {
    object Text : TextFormat()
    data class Number(
        val min: Float = 0F,
        val max: Float = 10_000_000F
    ) : TextFormat()
    data class NumberDecimal(
        val min: Float = 0F,
        val max: Float = 10_000_000F,
        val decimalDigits: Int = 3
    ) : TextFormat()
    object Time : TextFormat()
    companion object {
        val NumberPercent = NumberDecimal(
            min = 0f,
            max = 100f,
            decimalDigits = 3
        )
    }

}