package ru.ktsstudio.common.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import ru.ktsstudio.utilities.extensions.haveOreo
import java.util.Calendar

@TargetApi(Build.VERSION_CODES.N)
@SuppressLint("NewApi")
fun Context.openDateSelector(
    default: Long?,
    limitMinDate: Boolean = false,
    withTime: Boolean = true,
    onDateSelected: (Long) -> Unit
) {

    fun toTimeInMillis(year: Int, month: Int, date: Int, hour: Int, minute: Int): Long {
        return Calendar.getInstance()
            .apply {
                set(year, month, date, hour, minute)
                clear(Calendar.MILLISECOND)
                clear(Calendar.SECOND)
            }
            .timeInMillis
    }

    fun onDateSet(calendar: Calendar, year: Int, month: Int, day: Int) {
        if (withTime) {
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val selectedDate = toTimeInMillis(year, month, day, hour, minute)
                    onDateSelected.invoke(selectedDate)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
                .show()
        } else {
            val selectedDate = toTimeInMillis(year, month, day, hour = 0, minute = 0)
            onDateSelected.invoke(selectedDate)
        }
    }

    val calendar = Calendar.getInstance().also { it.timeInMillis = default ?: System.currentTimeMillis() }

    DatePickerDialog(
        this,
        DatePickerDialog.OnDateSetListener { _, year, month, day -> onDateSet(calendar, year, month, day) },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE)
    )
        .apply {
            if (limitMinDate) datePicker.minDate = System.currentTimeMillis()
            if (haveOreo()) {
                datePicker.setOnDateChangedListener { view, year, month, day ->
                    onDateSet(calendar, year, month, day)
                    dismiss()
                }
            }
        }
        .show()
}