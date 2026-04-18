package mohalim.billing.we.ui.new_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewMainViewModel @Inject constructor() : ViewModel() {
    private val _currentScreen = MutableStateFlow("Loading")
    val currentScreen = _currentScreen.asStateFlow()

    fun setCurrentScreen(screen: String) {
        _currentScreen.value = screen
    }

    private val _isLoadingPage = MutableStateFlow(true)
    val isLoadingPage = _isLoadingPage.asStateFlow()

    fun setIsLoadingPage(isLoading: Boolean) {
        _isLoadingPage.value = isLoading
    }

    // Internet Account Data
    private val _userName = MutableStateFlow("User")
    val userName = _userName.asStateFlow()

    private val _serviceNumber = MutableStateFlow("")
    val serviceNumber = _serviceNumber.asStateFlow()

    private val _currentPlan = MutableStateFlow("Loading...")
    val currentPlan = _currentPlan.asStateFlow()

    private val _balance = MutableStateFlow("0.00 EGP")
    val balance = _balance.asStateFlow()

    private val _remainingGB = MutableStateFlow(0f)
    val remainingGB = _remainingGB.asStateFlow()

    private val _totalGB = MutableStateFlow(0f)
    val totalGB = _totalGB.asStateFlow()

    // Internet PPPoE Credentials
    private val _internetUsername = MutableStateFlow("")
    val internetUsername = _internetUsername.asStateFlow()

    private val _internetPassword = MutableStateFlow("")
    val internetPassword = _internetPassword.asStateFlow()

    private val _isFetchingCredentials = MutableStateFlow(false)
    val isFetchingCredentials = _isFetchingCredentials.asStateFlow()

    fun setIsFetchingCredentials(fetching: Boolean) {
        _isFetchingCredentials.value = fetching
    }

    fun setInternetData(
        name: String,
        number: String,
        plan: String,
        bal: String,
        remaining: Float,
        total: Float
    ) {
        _userName.value = name
        _serviceNumber.value = number
        _currentPlan.value = plan
        _balance.value = bal
        _remainingGB.value = remaining
        _totalGB.value = total
    }

    fun setInternetCredentials(username: String, password: String) {
        _internetUsername.value = username
        _internetPassword.value = password
        _isFetchingCredentials.value = false
    }
}
