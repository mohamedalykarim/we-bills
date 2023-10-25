package mohalim.billing.we.ui.main2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mohalim.billing.we.R
import mohalim.billing.we.ui.viewer.ViewActivity
import javax.inject.Inject


@AndroidEntryPoint
class WeLinesDialog : DialogFragment() {
    @Inject
    lateinit var webView: WebView


    object CONSTANTS {
        const val JAVASCRIPT_INTERFACE_NAME = "HTML_OUT"
        const val STATE_WE_LINE_READY = "STATE_WE_LINE_READY"
        const val STATE_ENTER = "STATE_ENTER"
        const val STATE_REGISTER = "STATE_REGISTER"
        const val STRING_EMAIL = "STRING_EMAIL"
        const val STRING_PASSWORD = "STRING_PASSWORD"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        webView.loadUrl("https://billing.te.eg/ar-EG/Login")


        return ComposeView(requireContext()).apply {


            setContent {

                WeLinesDialogUI(webView, CONSTANTS, context)

            }
        }
    }

    companion object {
        const val TAG = "BillsByPhoneNumberDialog"
    }
}


@Composable
fun WeLinesDialogUI(
    webView: WebView,
    CONSTANTS: WeLinesDialog.CONSTANTS,
    context: Context
) {
    var currentPractice by remember { mutableStateOf("") }
    var scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    webView.addJavascriptInterface(
        DataByPhoneNumberLoadListerner(),
        CONSTANTS.JAVASCRIPT_INTERFACE_NAME
    )

    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            currentPractice = CONSTANTS.STATE_WE_LINE_READY
        }
    }

    Box(modifier = Modifier.background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .paint(
                    painterResource(id = R.drawable.transparent_bg),
                    contentScale = ContentScale.FillBounds
                )
                .verticalScroll(state = scrollState)
        ) {


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(16.dp, 0.dp),
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                label = {
                    Text(
                        text = "الايميل",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }, textStyle = TextStyle.Default.copy(
                    fontSize = 20.sp,
                )

            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(16.dp, 0.dp),
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                label = {
                    Text(
                        text = "الرقم السري",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }, textStyle = TextStyle.Default.copy(
                    fontSize = 20.sp,
                )

            )

            Spacer(modifier = Modifier.height(10.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ViewActivity::class.java)
                        intent.putExtra("STATE", CONSTANTS.STATE_ENTER)
                        intent.putExtra(CONSTANTS.STRING_EMAIL, email)
                        intent.putExtra(CONSTANTS.STRING_PASSWORD, password)
                        context.startActivity(intent)

                    },
                ) {
                    Text(text = "دخول")
                }

            }


            Spacer(modifier = Modifier.height(10.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ViewActivity::class.java)
                        intent.putExtra("STATE", CONSTANTS.STATE_REGISTER)
                        context.startActivity(intent)

                    },
                ) {
                    Text(text = "اشترك الان")
                }

            }


        }

    }

}


