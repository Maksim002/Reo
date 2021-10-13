package ru.ktsstudio.common.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.extensions.LayoutContainer
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.view.autoCleared

class GpsRequestDialogFragment : DialogFragment(), LayoutContainer {

    private var contentView: View by autoCleared()
    override val containerView: View
        get() = contentView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        contentView = requireActivity()
            .layoutInflater
            .inflate(R.layout.dialog_permission_request, null)

        dialog.setContentView(contentView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.dialog_width),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        initView()

        return dialog
    }

    private fun initView() {

        fun onButtonClicked(value: String) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                DIALOG_ENABLE_GPS_REQUEST_KEY,
                value
            )
            dismiss()
        }

        contentView.findViewById<TextView>(R.id.title).setText(R.string.geo_data_request_dialog_title)
        contentView.findViewById<TextView>(R.id.message).text = arguments?.getString(MESSAGE_ARG_KEY)
        contentView.findViewById<View>(R.id.reject).setOnClickListener {
            onButtonClicked(DIALOG_GEO_DATA_REJECT)
        }

        contentView.findViewById<View>(R.id.approve).setOnClickListener {
            onButtonClicked(DIALOG_GEO_DATA_APPROVE)
        }
    }

    companion object {
        const val MESSAGE_ARG_KEY = "MESSAGE_ARG_KEY"
        const val DIALOG_ENABLE_GPS_REQUEST_KEY = "DIALOG_GPS_REQUEST_KEY"
        const val DIALOG_GEO_DATA_APPROVE: String = "DIALOG_GEO_DATA_APPROVE"
        const val DIALOG_GEO_DATA_REJECT: String = "DIALOG_GEO_DATA_REJECT"
    }
}
