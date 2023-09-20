package mohalim.billing.we.ui.viewer

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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


        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                var adRequest = AdRequest.Builder().build()

                InterstitialAd.load(
                    this@ViewActivity,
                    "ca-app-pub-5350581213670869/5323847169",
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        val mInterstitialAd: InterstitialAd? = null
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)
                            interstitialAd.show(this@ViewActivity)
                        }


                    })
            }

        }.start()



        setContent {
            if (selectedCode != null) {
                if (selectedPhoneNumber != null) {
                    WebView(
                        webView = webView,
                        selectedCode = selectedCode,
                        selectedPhoneNumber = selectedPhoneNumber,
                        context = applicationContext
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
    selectedPhoneNumber: String,
    context: Context,
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
