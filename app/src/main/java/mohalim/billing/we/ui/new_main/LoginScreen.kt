package mohalim.billing.we.ui.new_main

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import mohalim.billing.we.R
import org.json.JSONObject
import org.jsoup.Jsoup

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: NewMainViewModel,
    webViewInstance: WebView,
    isLoadingPage: Boolean,
    onLoginSuccess: (String) -> Unit
) {
    var serviceNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("Internet") }
    var expanded by remember { mutableStateOf(false) }
    var showWebViewDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }


    val primaryColor = Color(0xFF4A148C)
    val secondaryColor = Color(0xFF7B1FA2)
    val surfaceColor = Color(0xFFF8F9FA)

    // Sync Compose state to WebView on every change
    LaunchedEffect(isLoadingPage, serviceNumber, password, serviceType) {
        if (!isLoadingPage) {
            errorText = ""
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
                                const options = rcVirtualListHolderInner?.children || [];
                                for (let i = 0; i < options.length; i++) {
                                  const option = options[i];
                                  if($safeServiceType === "Internet" && i === 0){
                                      option.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('click', { bubbles: true }));
                                  } else if($safeServiceType === "Landline" && i === 1){
                                      option.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));
                                      option.dispatchEvent(new MouseEvent('click', { bubbles: true }));
                                  }
                                }
                              }, 300);
                            })();
                            """.trimIndent()
            webViewInstance.evaluateJavascript(script2, null)

            webViewInstance.evaluateJavascript(
                "(function() { return document.documentElement.outerHTML; })();",
                { value ->
                    var html: String = value
                    // Clean escaped characters
                    html = html.replace("\\u003C", "<")
                        .replace("\\n", "")
                        .replace("\\\"", "\"")

                    val doc = Jsoup.parse(html)
                    val error  = doc.getElementsByClass("ant-form-item-explain-error")
                    if (!error.isEmpty()){
                        Log.d("TAG", "" + error[0].text())
                        errorText = error[0].text().toString()
                    }

                }
            )
        }
    }

    val isInputValid = serviceNumber.length >= 8 && password.isNotBlank()
    val showServiceType = serviceNumber.length >= 8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        // Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            primaryColor,
                            secondaryColor
                        )
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 48.dp,
                        bottomEnd = 48.dp
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Brand Logo Section
            Surface(
                modifier = Modifier.size(90.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "we",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.welcome_back),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.login_to_manage),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Main Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Service Number
                    OutlinedTextField(
                        value = serviceNumber,
                        onValueChange = { serviceNumber = it },
                        label = { Text(stringResource(R.string.service_number)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = primaryColor
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                   if (errorText.isNotEmpty()){
                       Text(
                           text = errorText,
                           color = Color.Red,
                           fontSize = 14.sp,
                       )
                   }


                    // Animated Service Type
                    AnimatedVisibility(
                        visible = showServiceType,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = serviceType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.select_service_type)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = primaryColor
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.internet)) },
                                    onClick = { serviceType = "Internet"; expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.landline)) },
                                    onClick = { serviceType = "Landline"; expanded = false }
                                )
                            }
                        }
                    }

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = primaryColor
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Login Button
                    Button(
                        onClick = {
                            val script = "document.getElementById('login-withecare')?.click();"
                            webViewInstance.evaluateJavascript(script, null)

                            // await 1 second
                            Thread.sleep(1000)

                            Log.d("TAG", "Login")


                            webViewInstance.evaluateJavascript(
                                """
                                (function() { 
                                    const notification = document.querySelector('.ant-notification-notice-description');
                                    return notification ? notification.innerText : null;
                                })();
                                """.trimIndent()
                            ) { value ->

                                Log.d("TAG", "Result: $value")

                                if (!value.isNullOrEmpty() && value != "null") {
                                    errorText = value
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        enabled = !isLoadingPage && isInputValid
                    ) {
                        if (isLoadingPage) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.login),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { showWebViewDialog = true }) {
                Text(stringResource(R.string.need_help_debug), color = primaryColor.copy(alpha = 0.7f))
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // WebView Management
        if (showWebViewDialog) {
            Dialog(
                onDismissRequest = { showWebViewDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = { showWebViewDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                            }
                        }
                        AndroidView(
                            factory = {
                                (webViewInstance.parent as? ViewGroup)?.removeView(webViewInstance)
                                webViewInstance
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.size(1.dp)) {
                AndroidView(factory = {
                    (webViewInstance.parent as? ViewGroup)?.removeView(webViewInstance)
                    webViewInstance
                })
            }
        }
    }
}
