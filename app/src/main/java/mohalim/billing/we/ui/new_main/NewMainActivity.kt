package mohalim.billing.we.ui.new_main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import mohalim.billing.we.core.utils.Utils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

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

    val webViewInstance = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                javaScriptCanOpenWindowsAutomatically = true
            }

            webViewClient = object : WebViewClient() {
                private var lastUrl = ""

                override fun onPageFinished(view: WebView?, url: String?) {
                    viewModel.setIsLoadingPage(false)
                    Log.d("WebView", "onPageFinished: $url")
                    url?.let { handleUrlChange(view, it) }
                }

                override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    Log.d("WebView", "doUpdateVisitedHistory: $url")
                    url?.let { handleUrlChange(view, it) }
                }

                private fun handleUrlChange(view: WebView?, url: String) {
                    if (url == lastUrl && !url.contains("accountoverview")) return
                    lastUrl = url

                    if (url.contains("login")) {
                        viewModel.setCurrentScreen("Login")
                        view?.evaluateJavascript(
                            "(function() { return document.body.innerHTML; })();"
                        ) { value ->
                            val html = unescapeJsString(value)
                            val document = Jsoup.parse(html)
                            view?.let {
                                Utils.handleLoginPageOnPageFinished(viewModel, it, url, document)
                            }
                        }
                    } else if (url.contains("accountoverview")) {
                        viewModel.setCurrentScreen("Loading")
                        Log.d("WebView", "Detected accountoverview, starting poll...")
                        pollForAccountOverview(view)
                    }
                }

                private fun pollForAccountOverview(view: WebView?) {
                    if (view == null) return
                    
                    view.evaluateJavascript(
                        """
                        (function () {
                            const content = document.querySelector('.ant-layout-content');
                            const phone = document.querySelector('span.ant-select-selection-item');
                            const progress = document.querySelector('.ant-progress-circle');
                            const planText = document.body.innerText.includes('Your Current Plan');
                            const balanceText = document.body.innerText.includes('Current Balance');
                            
                            // ننتظر حتى تظهر العناصر ويكون لها محتوى نصي (مثل رقم الهاتف)
                            if (content && phone && phone.innerText.trim().length > 0 && progress && planText) {
                                return content.outerHTML;
                            }
                            return "PENDING";
                        })();
                        """.trimIndent()
                    ) { value ->
                        Log.d("WebView", "Poll result: ${value?.take(50)}")
                        
                        if (value == "\"PENDING\"" || value == "null" || value == null) {
                            // استخدام Handler لضمان استمرار التكرار في الـ Main Looper
                            Handler(Looper.getMainLooper()).postDelayed({
                                pollForAccountOverview(view)
                            }, 2000)
                        } else {
                            val html = unescapeJsString(value)
                            Log.d("WebView", "Content rendered, length: ${html.length}")
                            val document = Jsoup.parse(html)
                            
                            var welcome = ""
                            var phoneService = ""
                            var plan = ""
                            var balance = ""
                            var type = ""
                            var remaining = "0"
                            var used = "0"

                            val welcomeContainer = document.selectFirst("div.ant-col.ant-col-24")
                            val phoneServiceContainer = document.selectFirst("span.ant-select-selection-item")
                            val planContainer = document.selectFirst("div:contains(Your Current Plan)")
                            
                            val balanceLabel = document.getElementsContainingOwnText("Current Balance").firstOrNull()

                            welcome = welcomeContainer?.text() ?: ""
                            phoneService = phoneServiceContainer?.text() ?: ""
                            
                            planContainer?.parent()?.selectFirst("span[title]")?.let {
                                plan = it.text().trim()
                            }

                            balanceLabel?.parent()?.let { parent ->
                                val balElement = parent.select("div").firstOrNull { 
                                    val t = it.text().trim()
                                    t.isNotEmpty() && t.replace(".", "").all { c -> c.isDigit() }
                                }
                                balance = balElement?.text()?.trim() ?: ""
                            }

                            val activeSlide = document.selectFirst("div.slick-slide.slick-active") ?: document

                            type = activeSlide.select("span.ant-progress-text span").lastOrNull { 
                                it.text().isNotBlank() 
                            }?.text()?.trim() ?: ""

                            val remainingLabel = activeSlide.selectFirst("span:contains(Remaining)")
                            remaining = remainingLabel?.parent()?.select("span")?.firstOrNull { 
                                it.text().trim().toDoubleOrNull() != null 
                            }?.text()?.trim() ?: "0"

                            val usedLabel = activeSlide.selectFirst("span:contains(Used)")
                            used = usedLabel?.parent()?.select("span")?.firstOrNull { 
                                it.text().trim().toDoubleOrNull() != null
                            }?.text()?.trim() ?: "0"

                            Log.d("WebView", "Parsed: Welcome=$welcome, Phone=$phoneService, Plan=$plan, Bal=$balance, Type=$type, Rem=$remaining, Used=$used")

                            // إذا كانت البيانات الأساسية مفقودة رغم وجود العناصر، استمر في المحاولة
                            if (phoneService.isEmpty() || plan.isEmpty()) {
                                Log.d("WebView", "Essential data missing, retrying...")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    pollForAccountOverview(view)
                                }, 2000)
                                return@evaluateJavascript
                            }

                            val rFloat = remaining.replace(",", "").toFloatOrNull() ?: 0f
                            val uFloat = used.replace(",", "").toFloatOrNull() ?: 0f
                            
                            viewModel.setInternetData(
                                name = welcome,
                                number = phoneService,
                                plan = plan,
                                bal = balance,
                                remaining = rFloat,
                                total = rFloat + uFloat
                            )

                            if (type.contains("Internet", ignoreCase = true)) {
                                viewModel.setCurrentScreen("Internet")
                            } else if (type.contains("Minutes", ignoreCase = true)) {
                                viewModel.setCurrentScreen("Landline")
                            }
                        }
                    }
                }

                private fun unescapeJsString(value: String?): String {
                    var s = value ?: ""
                    if (s.startsWith("\"") && s.endsWith("\"")) {
                        s = s.substring(1, s.length - 1)
                            .replace("\\u003C", "<")
                            .replace("\\u003E", ">")
                            .replace("\\\"", "\"")
                            .replace("\\n", " ")
                            .replace("\\t", " ")
                            .replace("\\\\", "\\")
                    }
                    return s
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("WebView", "${consoleMessage.message()} @ ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}")
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
                "Loading" -> LoadingUI(webViewInstance)
                "Login" -> LoginScreen(
                    viewModel,
                    webViewInstance,
                    isLoadingPage = isLoadingPage,
                    onLoginSuccess = { serviceType ->
                        viewModel.setCurrentScreen(serviceType)
                    }
                )
                "Internet" -> InternetHomeScreen(
                    viewModel,
                    onLogout = {
                    viewModel.setCurrentScreen("Login")
                })
                "Landline" -> LandlineHomeScreen(viewModel,
                    onLogout = {
                    viewModel.setCurrentScreen("Login")
                })
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
