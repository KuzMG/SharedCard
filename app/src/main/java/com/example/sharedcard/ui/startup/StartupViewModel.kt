package com.example.sharedcard.ui.startup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.R
import com.example.sharedcard.repository.AuthManager
import com.example.sharedcard.ui.startup.data.AuthFormState
import com.example.sharedcard.ui.startup.data.AuthResult
import com.example.sharedcard.ui.startup.data.RegisterFormState
import com.example.sharedcard.ui.startup.data.RegisterResult
import com.example.sharedcard.ui.startup.data.SyncResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class StartupViewModel @Inject constructor(private val authManager: AuthManager) :
    ViewModel() {
    enum class State {
        Registration, Authorization, Continue, Startup, Synchronization
    }

    private val _synchronizationState = MutableLiveData<SyncResult>()
    val synchronizationState: LiveData<SyncResult> = _synchronizationState


    private val _registerFormState = MutableLiveData<RegisterFormState?>()
    val registerFormState: LiveData<RegisterFormState?> = _registerFormState

    private val _authFormState = MutableLiveData<AuthFormState?>()
    val authFormState: LiveData<AuthFormState?> = _authFormState

    private val _authResult = MutableLiveData<AuthResult?>()
    val authResult: LiveData<AuthResult?> = _authResult

    private val _registerResult = MutableLiveData<RegisterResult?>()
    val registerResult: LiveData<RegisterResult?> = _registerResult

    private val _transition = MutableLiveData<State>()
    val transitionState: LiveData<State> = _transition

    var authLogin = ""
    var authPassword = ""
    var regEmail = ""
        set(value) {
            field = value
            _registerFormState.value =  if (value.isNotBlank()) {
                when (isUserEmailValid(value)) {
                    true -> RegisterFormState(1,isDataValid = true)
                    false -> RegisterFormState(1,emailError = R.string.invalid_email)
                }
            } else {
                RegisterFormState(1)
            }
        }
    var regDate = Date()
    var regGender = true
    var regPassword = ""
        set(value) {
            field = value
            _registerFormState.value = if (value.isNotBlank()) {
                when (isPasswordValid(value)) {
                    true -> RegisterFormState(3,isDataValid = true)
                    false -> RegisterFormState(3,passwordError = R.string.invalid_password)
                }
            } else {
                RegisterFormState(3)
            }
        }
    var regCode = ""
        set(value) {
            field = value
            _registerFormState.value = when (value.isNotBlank()) {
                true -> RegisterFormState(4,isDataValid = true)
                false -> RegisterFormState(4)
            }
        }
    var regName = ""
        set(value) {
            field = value
            _registerFormState.value = when (value.isNotBlank()) {
                true -> RegisterFormState(0,isDataValid = true)
                false -> RegisterFormState(0)
            }
        }


    var currentPageReg = 1
        set(value) {
            if (value <= 5) {
                if(value==3)
                    _registerFormState.value = RegisterFormState(value, isDataValid = true)
                _currentPageLivedata.value = value
                field = value
            }
        }
    val _currentPageLivedata = MutableLiveData<Int>()
    val currentPageLivedata: LiveData<Int>
        get() = _currentPageLivedata

    init {
        _transition.value = State.Startup
        _currentPageLivedata.value = 0
    }

    fun setTransitionState(state: State) {
        _transition.value = state
    }

    fun synchronization() {
        viewModelScope.launch(Dispatchers.IO) {
            clear()
            val result = authManager.synchronization()
            _synchronizationState.postValue(result)
        }
    }

    fun authentication(login: String, password: String, isInternetConnection: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _authResult.postValue(AuthResult(loading = true))
            val result = authManager.authentication(login, password, isInternetConnection)
            _authResult.postValue(result)
        }
    }

    fun registration() {
        viewModelScope.launch(Dispatchers.IO) {
            _registerResult.postValue(RegisterResult(loading = true))
            val result = authManager.registration(regEmail, regPassword,regName,regDate, regGender)
            _registerResult.postValue(result)
        }
    }

    fun verification() {
        viewModelScope.launch(Dispatchers.IO) {
            _registerResult.postValue(RegisterResult(loading = true))
            val result = authManager.verification(regEmail, regPassword, regCode)
            _registerResult.postValue(result)
        }
    }

    fun authenticationUI(login: String, password: String) {
        authLogin = login
        authPassword = password
        if (!isUserEmailValid(login)) {
            _authFormState.value = when (login.isEmpty()) {
                true -> AuthFormState()
                false -> AuthFormState(usernameError = R.string.invalid_username)
            }
        } else if (!isPasswordValid(password)) {
            _authFormState.value = when (password.isEmpty()) {
                true -> AuthFormState()
                false -> AuthFormState(passwordError = R.string.invalid_password)
            }
        } else {
            _authFormState.value = AuthFormState(isDataValid = true)
        }
    }

    private fun isUserEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.length > 1) {
            true
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String) = password.length >= 8

    private fun clear() {
        _authResult.postValue(null)
        _authFormState.postValue(null)
        _registerFormState.postValue(null)
        _registerResult.postValue(null)
    }

}