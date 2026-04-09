package mohalim.billing.we.ui.new_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import jakarta.inject.Inject

class NewMainViewModel @Inject constructor() : ViewModel() {
    private val _currentScreen = MutableLiveData<String>("Login")
    val currentScreen = _currentScreen.asFlow()

    fun setCurrentScreen(screen: String) {
        _currentScreen.value = screen
    }

    private val _isloadingPage = MutableLiveData<Boolean>(true)
    val isLoadingPage = _isloadingPage.asFlow()

    fun setIsLoadingPage(isLoading: Boolean) {
        _isloadingPage.value = isLoading
    }
}


