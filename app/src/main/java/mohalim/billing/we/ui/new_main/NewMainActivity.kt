package mohalim.billing.we.ui.new_main

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
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
import mohalim.billing.we.core.utils.Utils
import org.jsoup.Jsoup

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

    val currentScreen by viewModel.currentScreen.collectAsState("Loading")
    val isLoadingPage by viewModel.isLoadingPage.collectAsState(true)


    // Initialize and remember the WebView instance
    val webViewInstance = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                // Desktop User Agent
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                javaScriptCanOpenWindowsAutomatically = true
            }




            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    viewModel.setIsLoadingPage(false)
                    Log.d("WebView", "Page finished loading: $url")
                    view?.evaluateJavascript(
                        "(function() { return document.documentElement.outerHTML; })();",
                        { value ->
                            var html: String = value
                            // Clean escaped characters
                            html = html.replace("\\u003C", "<")
                                .replace("\\n", "")
                                .replace("\\\"", "\"")

                            val doc = Jsoup.parse(html)
                            val result = Utils.handleLoginPageOnPageFinished(viewModel, view, url!!, doc)



                            if (url?.contains("login") == true) {
                                viewModel.setCurrentScreen("Login")
                                Log.d("WebView", "Page finished loading: login")

                            } else if (url?.contains("accountoverview") == true ) {
                                Log.d("WebView", "Page finished loading: accountoverview")
                                viewModel.setCurrentScreen("Internet")

                            }


                        }
                    )







                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        "WebView",
                        "${consoleMessage.message()} @ ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}"
                    )
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
                "Loading" ->{
                    LoadingUI(webViewInstance)
                }
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
