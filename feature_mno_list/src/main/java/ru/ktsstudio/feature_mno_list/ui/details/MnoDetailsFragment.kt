package ru.ktsstudio.feature_mno_list.ui.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_mno_details.*
import kotlinx.android.synthetic.main.fragment_mno_list.list
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.di.details.MnoDetailsComponent
import ru.ktsstudio.feature_mno_list.presentation.details.MnoDetailsUiState
import ru.ktsstudio.feature_mno_list.presentation.details.MnoDetailsViewModel
import ru.ktsstudio.feature_mno_list.ui.details.adapter.MnoDetailsAdapter
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 01.10.2020.
 */
class MnoDetailsFragment : BaseFragment(R.layout.fragment_mno_details) {

    private val mnoId: String
        get() = requireArguments().getString("mnoId").requireNotNull()

    private var mnoDetailsAdapter: MnoDetailsAdapter by autoCleared()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: MnoDetailsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MnoDetailsComponent.create(mnoId).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initMnoList()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.state, ::setState)
        addMeasurementButton.setOnClickListener {
            viewModel.startMeasurement()
        }
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.mno_details_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initMnoList() {
        mnoDetailsAdapter = MnoDetailsAdapter(viewModel::retry)
        with(list) {
            adapter = mnoDetailsAdapter
            setHasFixedSize(true)
            addItemDecoration(getSpaceItemDecoration())
            addOnScrollListener(
                HideElevationScrollListener(
                    bottomBlock,
                    resources.getDimensionPixelSize(R.dimen.default_elevation)
                )
            )
        }
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_padding)
        return MarginInternalItemDecoration(margin) { holder ->
            holder !is CardBottomCornersAdapterDelegate.Holder &&
                    holder !is LabeledCardValueAdapterDelegate.Holder &&
                    holder !is CardTitleItemAdapterDelegate.Holder
        }
    }

    private fun setState(state: MnoDetailsUiState) {
        mnoDetailsAdapter.setDataWithState(
            data = state.data,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false,
            isLoading = state.loading,
            error = state.error
        )
    }
}