package ru.ktsstudio.common.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.toTwoNumber

/**
 * Created by Igor Park on 10/02/2021.
 */
class TimePickerDialogFragment(
    private val initialTime: String,
    private val onTimeSet: (String) -> Unit
) : DialogFragment() {
    private var contentView: View by autoCleared()
    private var timePicker: TimePicker by autoCleared()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        contentView = requireActivity().layoutInflater.inflate(R.layout.dialog_time_picker, null)
        dialog.setContentView(contentView)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setDimAmount(0.5f)
        initView()
        return dialog
    }

    private fun initView() {

        timePicker = contentView.findViewById(R.id.timePicker)
        timePicker.setIs24HourView(true)
        timePicker.setInitialTime(initialTime)

        contentView.findViewById<View>(R.id.cancel).setOnClickListener { dismiss() }

        contentView.findViewById<View>(R.id.ok).setOnClickListener {
            onTimeSet(getTimeRange(timePicker.currentHour, timePicker.currentMinute))
            dismiss()
        }
    }

    private fun TimePicker.setInitialTime(time: String) {
        val hour: Int
        val minute: Int
        if (validateTime(time)) {
            val initialTime = time.split(":")
            hour = initialTime.first().toInt()
            minute = initialTime.last().toInt()
        } else {
            val currentTime = Instant.now().atZone(ZoneId.systemDefault())
            hour = currentTime.hour
            minute = currentTime.minute
        }
        currentHour = hour
        currentMinute = minute
    }

    private fun getTimeRange(hourOfDay: Int, minute: Int): String {
        return "${hourOfDay.toLong().toTwoNumber()}:${minute.toLong().toTwoNumber()}"
    }

    private fun validateTime(time: String): Boolean {
        if (time.isBlank()) return false
        val initialTime = time.split(TIME_DELIMITER)
        return initialTime.isNotEmpty() && initialTime.size == 2 && initialTime.all { it.toIntOrNull() != null }
    }

    companion object {
        private const val TIME_DELIMITER = ":"
    }
}