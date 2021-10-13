package ru.ktsstudio.feature_auth.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.widget.textChanges
import kotlinx.android.synthetic.main.fragment_login.*
import ru.ktsstudio.common.data.AppVersion
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.utils.form.isCorrect
import ru.ktsstudio.feature_auth.BuildConfig
import ru.ktsstudio.feature_auth.R
import ru.ktsstudio.feature_auth.di.flow.AuthFlowComponent
import ru.ktsstudio.feature_auth.domain.form.field.LoginField
import ru.ktsstudio.feature_auth.domain.form.field.LoginFieldType
import ru.ktsstudio.feature_auth.presentation.login.LoginUiEvent
import ru.ktsstudio.feature_auth.presentation.login.LoginUiState
import ru.ktsstudio.feature_auth.presentation.login.LoginViewModel
import ru.ktsstudio.feature_auth.presentation.login.form.FieldViewImpl
import ru.ktsstudio.feature_auth.utils.setupEndButton
import ru.ktsstudio.form_feature.form.FormFieldsHolder
import ru.ktsstudio.utilities.KeyboardUtils
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.requireNotNull
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
internal class LoginFragment : BaseFragment(R.layout.fragment_login) {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: AuthNavigator

    private val viewModel: LoginViewModel by viewModels { factory }
    private lateinit var fieldsHolder: FormFieldsHolder<LoginField>
    private val keyboardToggleListener = KeyboardUtils.SoftKeyboardToggleListener { show ->
        logo.isVisible = show.not()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthFlowComponent.create().loginComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLoginForm()
        bindViewModel()
        if (savedInstanceState == null && BuildConfig.DEBUG) {
            emailInput.setText("ros-tech-user@reo.ru")
            passwordInput.setText("P@ssw0rd")
        }
    }

    override fun onResume() {
        super.onResume()
        KeyboardUtils.addKeyboardToggleListener(requireActivity(), keyboardToggleListener)
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.removeKeyboardToggleListener(keyboardToggleListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    private fun bindViewModel() = with(viewModel) {
        listenFieldFocus()
        viewModel.listenLoginForm(
            emailInput.textChanges(),
            passwordInput.textChanges(),
        )
        viewLifecycleOwner.observeNotNull(loginState, ::applyState)
        viewLifecycleOwner.observeNotNull(errorMessage, { toast(it) })
        viewLifecycleOwner.observeNotNull(navigate, { navigateToMainFragment() })
        submitButton.setOnClickListener {
            requireActivity().hideKeyboard()
            login()
        }
    }

    private fun applyState(loginState: LoginUiState) = with(loginState) {
        fieldsHolder.applyState(formState)
        progress.isVisible = isLoading
        showCredentialsError(isIncorrectData)
        submitButton.isEnabled = isLoading.not() && formState.isCorrect && isIncorrectData.not()
    }

    private fun initLoginForm() {
        emailInput.setupEndButton(
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)
                .requireNotNull(),
            onClick = { emailInput.setText("") }
        )
        val formFields = listOf(
            LoginField.Email(
                fieldView = FieldViewImpl(
                    input = emailInput,
                    errorMessage = emailError
                )
            ),
            LoginField.Password(
                fieldView = FieldViewImpl(
                    input = passwordInput,
                    errorMessage = passwordError
                )
            )
        )
        fieldsHolder = FormFieldsHolder(formFields)
        submitButton.isEnabled = false
    }

    private fun listenFieldFocus() {
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.switchField(
                event = LoginUiEvent.SwitchField(
                    fieldType = LoginFieldType.Email,
                    hasFocus = hasFocus
                )
            )
        }

        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.switchField(
                event = LoginUiEvent.SwitchField(
                    fieldType = LoginFieldType.Password,
                    hasFocus = hasFocus
                )
            )
        }
    }

    private fun navigateToMainFragment() {
        navigator.authFeatureLoginToFinishAuth()
    }

    private fun showCredentialsError(show: Boolean) {
        if (show) {
            emailInput.errorState = show
            passwordInput.errorState = show
        }
        errorMessage.isVisible = show
    }
}
