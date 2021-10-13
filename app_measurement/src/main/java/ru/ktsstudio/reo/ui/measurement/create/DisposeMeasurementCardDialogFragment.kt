package ru.ktsstudio.reo.ui.measurement.create

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dialog_measurement_dispose.view.*
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.reo.R

class DisposeMeasurementCardDialogFragment : DialogFragment(), LayoutContainer {

    private val args: DisposeMeasurementCardDialogFragmentArgs by navArgs()

    private var contentView: View by autoCleared()
    override val containerView: View?
        get() = contentView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        contentView =
            requireActivity().layoutInflater.inflate(R.layout.dialog_measurement_dispose, null)

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
        contentView.leave.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                args.actionKey,
                DIALOG_LEAVE_ACTION
            )
            dismiss()
        }

        contentView.stay.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                args.actionKey,
                DIALOG_STAY_ACTION
            )
            dismiss()
        }
    }

    companion object {
        const val DIALOG_LEAVE_ACTION: String = "DIALOG_LEAVE_ACTION"
        const val DIALOG_STAY_ACTION: String = "DIALOG_STAY_ACTION"
    }
}
