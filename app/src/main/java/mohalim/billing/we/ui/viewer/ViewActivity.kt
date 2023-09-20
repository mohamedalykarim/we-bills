package mohalim.billing.we.ui.viewer

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint
import mohalim.billing.we.ui.main2.BillsByPhoneNumberDialog
import javax.inject.Inject

@AndroidEntryPoint
class ViewActivity : AppCompatActivity() {
    @Inject
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedCode =
            intent?.getStringExtra(BillsByPhoneNumberDialog.CONSTANTS.STRING_PHONE_CODE)
        val selectedPhoneNumber =
            intent?.getStringExtra(BillsByPhoneNumberDialog.CONSTANTS.STRING_PHONE_NUMBER)

        setContent {
            if (selectedCode != null) {
                if (selectedPhoneNumber != null) {
                    WebView(
                        webView = webView,
                        selectedCode = selectedCode,
                        selectedPhoneNumber = selectedPhoneNumber
                    )
                }
            }

        }
    }
}


@Composable
fun WebView(
    modifier: Modifier = Modifier,
    webView: WebView,
    selectedCode: String,
    selectedPhoneNumber: String
) {
    var backEnabled by remember { mutableStateOf(false) }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )


                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Toast.makeText(context, "finish", Toast.LENGTH_LONG).show()

                        webView.loadUrl(
                            "javascript:document.getElementById(\"TxtAreaCode\").value = ${selectedCode}"
                        )
                        webView.loadUrl(
                            "javascript:document.getElementById(\"TxtPhoneNumber\").value = ${selectedPhoneNumber}"
                        )
                        webView.loadUrl("javascript:(document.querySelectorAll(\"form#FrmInquiryByPhone div.form-inline div.form-group button.btn\"))[0].click()")


                    }
                }


            }
        }, update = {

        })

    BackHandler(enabled = backEnabled) {
        webView.goBack()
    }
}
