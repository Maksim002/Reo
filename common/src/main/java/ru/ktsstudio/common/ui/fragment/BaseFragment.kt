package ru.ktsstudio.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry

abstract class BaseFragment(
    @LayoutRes private val layoutId: Int? = null
) : Fragment() {

    private val destroyChecker = FragmentDestroyChecker(::onFullDestroy)

    protected open val backPressedHandler: (() -> Unit)? = null

    private val appCodeProvider: AppCodeProvider = ComponentRegistry.get<CoreApi>().appCodeProvider()

    private val firstLaunchChecker = FirstLaunchChecker(appCodeProvider)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutId?.let {
            DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                layoutId,
                container,
                false
            )
                .root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackPressedListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        firstLaunchChecker.saveState(outState)
        destroyChecker.onSaveInstanceState(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyChecker.onDestroyView(this)
    }

    protected fun isFirstLaunch(savedInstanceState: Bundle?): Boolean {
        return firstLaunchChecker.check(savedInstanceState)
    }

    private fun setBackPressedListener() {
        backPressedHandler?.let { onBackPressed ->
            requireActivity().onBackPressedDispatcher
                .addCallback(
                    viewLifecycleOwner,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() = onBackPressed()
                    }
                )
        }
    }

    open fun onFullDestroy() {}

    override fun onDestroy() {
        super.onDestroy()
        destroyChecker.onDestroy(this)
    }
}
