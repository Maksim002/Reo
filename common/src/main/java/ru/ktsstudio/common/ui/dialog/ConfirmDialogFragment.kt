package ru.ktsstudio.common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.extensions.LayoutContainer
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.requireNotNull
import ru.ktsstudio.utilities.extensions.withArguments

/**
 * @author Maxim Ovchinnikov on 03.02.2021.
 */
class ConfirmDialogFragment : DialogFragment(), LayoutContainer {

    private var clickListener: OnClickListener? = null

    private val title: String? get() = arguments?.getString(TITLE)
    private val message: String get() = arguments?.getString(MSG).orEmpty()
    private val positive: String? get() = arguments?.getString(POSITIVE_TEXT)
    private val negative: String? get() = arguments?.getString(NEGATIVE_TEXT)
    private val highlightPositive: Boolean get() = requireArguments().getBoolean(HIGHLIGHT_POSITIVE)
    private val dialogTag: String get() = requireArguments().getString(TAG).requireNotNull()

    private var contentView: View by autoCleared()
    override val containerView: View
        get() = contentView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        contentView = requireActivity()
            .layoutInflater
            .inflate(R.layout.dialog_confirm, null)

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
        val rejectButton = contentView.findViewById<TextView>(R.id.reject)
        val approveButton = contentView.findViewById<TextView>(R.id.approve)

        contentView.findViewById<TextView>(R.id.title).text = title
        contentView.findViewById<TextView>(R.id.message).text = message

        rejectButton.apply {
            text = negative
            setOnClickListener {
                clickListener?.onRejectDialog(dialogTag)
                dismiss()
            }
        }

        approveButton.apply {
            text = positive
            isActivated = highlightPositive
            setOnClickListener {
                clickListener?.onConfirmDialog(dialogTag)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        clickListener = when {
            parentFragment is OnClickListener -> parentFragment as OnClickListener
            activity is OnClickListener -> activity as OnClickListener
            else -> null
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        clickListener?.onRejectDialog(dialogTag)
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
    }

    interface OnClickListener {
        fun onConfirmDialog(tag: String)
        fun onRejectDialog(tag: String) {}
    }

    companion object {
        private const val TITLE = "title"
        private const val MSG = "msg"
        private const val POSITIVE_TEXT = "positive_text"
        private const val NEGATIVE_TEXT = "negative_text"
        private const val HIGHLIGHT_POSITIVE = "highlight_positive"
        private const val TAG = "tag"

        fun getInstance(
            title: String? = null,
            message: String,
            positive: String? = null,
            negative: String? = null,
            highlightPositive: Boolean = false,
            tag: String
        ) = ConfirmDialogFragment().withArguments {
            putString(TITLE, title)
            putString(MSG, message)
            putString(POSITIVE_TEXT, positive)
            putString(NEGATIVE_TEXT, negative)
            putBoolean(HIGHLIGHT_POSITIVE, highlightPositive)
            putString(TAG, tag)
        }
    }
}