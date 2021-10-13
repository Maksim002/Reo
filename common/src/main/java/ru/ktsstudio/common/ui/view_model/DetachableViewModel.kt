package ru.ktsstudio.common.ui.view_model

import androidx.lifecycle.ViewModel

abstract class DetachableViewModel : ViewModel() {
    open fun onDetach() {}
}
