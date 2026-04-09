package mohalim.billing.we.ui.new_main

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

class NewMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel : NewMainViewModel by viewModels()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WETheme {
                NewMainActivityUI(viewModel)
            }
        }
    }
}

@Composable
fun NewMainActivityUI(viewModel: NewMainViewModel) {
    val context = LocalContext.current

    val currentScreen by viewModel.currentScreen.collectAsState("Login")
    val isLoadingPage by viewModel.isLoadingPage.collectAsState(true)


    // Initialize and remember the WebView instance
    val webViewInstance = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.userAgentString = "Mozilla/5.0 (Linux; Android 10) Chrome/91.0.4472.124 Mobile Safari/537.36"

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    viewModel.setIsLoadingPage(false)
                    Log.d("WebView", "Page finished loading: $url")

                    // Basic logic to detect if we logged in (this would need real implementation)
                    if (url?.contains("login") == true || url?.contains("home") == true) {
                    }else if (url?.contains("accountoverview") == true) {
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("WebView", consoleMessage.message())
                    return true
                }
            }

            loadUrl("https://my.te.eg/echannel/#/login")
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFFF8F9FA)
        ) {
            when (currentScreen) {
                "Login" -> {
                    LoginScreen(
                        viewModel,
                        webViewInstance,
                        isLoadingPage = isLoadingPage,
                        onLoginSuccess = { serviceType ->
                            viewModel.setCurrentScreen(serviceType)
                        })
                }
                "Internet" -> {
                    InternetHomeScreen(onLogout = {
                        viewModel.setCurrentScreen("Login")
                    })
                }
                "Landline" -> {
                    LandlineHomeScreen(onLogout = {
                        viewModel.setCurrentScreen("Login")
                    })
                }
            }
        }
    }
}

@Composable
fun WETheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4A148C),
            secondary = Color(0xFF00BFA5),
            surface = Color.White
        ),
        content = content
    )
}
