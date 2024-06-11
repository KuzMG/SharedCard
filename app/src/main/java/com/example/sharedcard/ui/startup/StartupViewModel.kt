package com.example.sharedcard.ui.startup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.R
import com.example.sharedcard.email.EmailMessage
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.ui.startup.data.LoginFormState
import com.example.sharedcard.ui.startup.data.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class StartupViewModel @Inject constructor(private val accountManager: AccountManager) :
    ViewModel() {
    enum class State {
        Registration, Authorization, Continue, Startup
    }


    private val _loginForm = MutableLiveData<LoginFormState?>()
    val loginFormState: LiveData<LoginFormState?> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> = _loginResult

    private val _transition = MutableLiveData<State>()
    val transitionState: LiveData<State> = _transition

    private var code = ""

    init {
        _transition.value = State.Startup
    }

    fun setTransitionState(state: State) {
        _transition.value = state
    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = when (accountManager.signIn(login, password)) {
                true -> LoginResult()
                false -> LoginResult(R.string.invalid_sign_in)
            }
            _loginResult.postValue(result)
        }
    }

    fun signUp(login: String, password: String, code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (this@StartupViewModel.code == code) {
                accountManager.signUp(login, password)
                _loginResult.postValue(LoginResult())
            } else {
                _loginResult.postValue(LoginResult(R.string.invalid_code))
            }
        }
    }

    fun authorization(login: String, password: String) {
        if (!isUserNameValid(login)) {
            _loginForm.value = when (login.isEmpty()) {
                true -> LoginFormState(usernameError = 0)
                false -> LoginFormState(usernameError = R.string.invalid_username)
            }
        } else if (!isPasswordValid(password)) {
            _loginForm.value = when (password.isEmpty()) {
                true -> LoginFormState(passwordError = 0)
                false -> LoginFormState(passwordError = R.string.invalid_password)
            }

        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun sendMessage(email: String) {
        val strBuilder = StringBuilder()
        for (i in 1..6)
            strBuilder.append(Random.nextInt(0, 10))
        code = strBuilder.toString()
        viewModelScope.launch(Dispatchers.IO) {
            EmailMessage.send(email, code)
        }
    }

    fun registration(login: String, password: String, repeatPassword: String, code: String) {
        if (!isUserNameValid(login)) {
            _loginForm.value = when (login.isEmpty()) {
                true -> LoginFormState(usernameError = 0)
                false -> LoginFormState(usernameError = R.string.invalid_username)
            }
        } else if (!isPasswordValid(password)) {
            _loginForm.value = when (password.isEmpty()) {
                true -> LoginFormState(passwordError = 0)
                false -> LoginFormState(passwordError = R.string.invalid_password)
            }
        } else if (password != repeatPassword && repeatPassword.isNotEmpty()) {
            _loginForm.value =
                LoginFormState(repeatPasswordError = R.string.invalid_repeat_password)
        } else if (code.isEmpty()) {
            _loginForm.value = LoginFormState()
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun clear() {
        _loginResult.value = null
        _loginForm.value = null
    }

}