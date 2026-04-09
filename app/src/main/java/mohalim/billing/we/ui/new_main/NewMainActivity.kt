package mohalim.billing.we.ui.new_main

import android.annotation.SuppressLint
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.json.JSONObject

class NewMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color(0xFFF8F9FA)
                    ) {
                        LoginScreen()
                    }
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

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var serviceNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("Internet") }
    var expanded by remember { mutableStateOf(false) }
    var isLoadingPage by remember { mutableStateOf(true) }
    var showWebViewDialog by remember { mutableStateOf(false) }

    // Initialize and remember the WebView instance so it persists and keeps loading in background
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
                    isLoadingPage = false
                    Log.d("WebView", "Page finished loading: $url")
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

    // Sync Compose state to WebView on every change
    LaunchedEffect(isLoadingPage, serviceNumber, password, serviceType) {
        if (!isLoadingPage) {
            delay(400)

            val safeServiceNumber = JSONObject.quote(serviceNumber)
            val safePassword = JSONObject.quote(password)
            val safeServiceType = JSONObject.quote(serviceType)

            val script = """
                            (function() {
                                const setNativeValue = (element, value) => {
                                    const valueSetter = Object.getOwnPropertyDescriptor(element.__proto__, 'value').set;
                                    valueSetter.call(element, value);
                                    element.dispatchEvent(new Event('input', { bubbles: true }));
                                    element.dispatchEvent(new Event('change', { bubbles: true }));
                                };
                    
                                const subInput = document.getElementById('login_loginid_input_01');
                                const passInput = document.getElementById('login_password_input_01');
                    
                                if (subInput) setNativeValue(subInput, $safeServiceNumber);
                                if (passInput) setNativeValue(passInput, $safePassword);
                                
                                const antSelect = document.querySelectorAll('.ant-select')[0];
                                
                                
                                 const children = antSelect.children;
                    
                                 for (let i = 0; i < children.length; i++) {                
                                    if(i === 0){
                                        children[i].dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                        children[i].dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                        children[i].dispatchEvent(new MouseEvent('click', { bubbles: true }));    
                                    }
                                    
                                 }
                            })();
                            """.trimIndent()
            webViewInstance.evaluateJavascript(script, null)


            val script2 = """
                            (function() {
                              setTimeout(() => {
                              
                                const rcVirtualListHolderInner = document.querySelectorAll('.rc-virtual-list-holder-inner')[0];
                                console.log("ANT SELECT", rcVirtualListHolderInner);
                                const options = rcVirtualListHolderInner.children;
                                console.log("OPTIONS", options);
                                for (let i = 0; i < options.length; i++) {
                                  const option = options[i];
                                  if($safeServiceType === "Internet" && i === 0){
                                      option.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('click', { bubbles: true }));
                                  }else if($safeServiceType === "Landline" && i === 1){
                                      option.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('click', { bubbles: true }));
                                  }
                                  
                                  console.log("CHILD " + " tagName", option.tagName);
                                  console.log("CHILD " + " className", option.className);
                                  console.log("CHILD " + " id", option.id);
                                  console.log("CHILD " + " textContent", option.textContent);
                                  console.log("CHILD " + " innerHTML", option.innerHTML);
                                  console.log("CHILD " + " outerHTML", option.outerHTML);

                                  
                                  
                                }
                              
                            
                              }, 500);
                            })();
                            """.trimIndent()
            webViewInstance.evaluateJavascript(script2, null)


        }
    }

    // Dropdown appears when service number is 8+ digits
    val showServiceType = serviceNumber.length >= 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Color(0xFF4A148C),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("we", fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color(0xFF4A148C))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Login to your account", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Login Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                
                // Service Number
                OutlinedTextField(
                    value = serviceNumber,
                    onValueChange = { serviceNumber = it },
                    label = { Text("Service number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Animated Dropdown Visibility
                AnimatedVisibility(visible = showServiceType, enter = fadeIn(), exit = fadeOut()) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = serviceType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Service type") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Internet") },
                                    onClick = { serviceType = "Internet"; expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Landline") },
                                    onClick = { serviceType = "Landline"; expanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        val script = """
                            (function() {
                
                                const loginButton = document.getElementById('login-withecare');
                                loginButton.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                loginButton.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                loginButton.dispatchEvent(new MouseEvent('click', { bubbles: true }));
                
                            })();
                        """.trimIndent()

                        webViewInstance.evaluateJavascript(script, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoadingPage
                ) {
                    if (isLoadingPage) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("LOGIN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Debug Button
                TextButton(
                    onClick = { showWebViewDialog = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Show Debug WebView", color = Color(0xFF4A148C).copy(alpha = 0.7f))
                }
            }
        }

        // WebView Host: either in a Dialog or hidden in a 1dp Box (to keep it active)
        if (showWebViewDialog) {
            Dialog(onDismissRequest = { showWebViewDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 8.dp
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            TextButton(onClick = { showWebViewDialog = false }) {
                                Text("Close")
                            }
                        }
                        AndroidView(
                            factory = {
                                (webViewInstance.parent as? ViewGroup)?.removeView(webViewInstance)
                                webViewInstance
                            },
                            update = {},
                            onRelease = { view ->
                                (view.parent as? ViewGroup)?.removeView(view)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        } else {
            // Background WebView Engine (Hidden)
            Box(modifier = Modifier.size(1.dp)) {
                AndroidView(
                    factory = {
                        (webViewInstance.parent as? ViewGroup)?.removeView(webViewInstance)
                        webViewInstance
                    },
                    update = {},
                    onRelease = { view ->
                        (view.parent as? ViewGroup)?.removeView(view)
                    }
                )
            }
        }
    }
}
