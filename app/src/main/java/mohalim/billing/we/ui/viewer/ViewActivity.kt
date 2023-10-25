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
import mohalim.billing.we.ui.main2.WeLinesDialog
import javax.inject.Inject

@AndroidEntryPoint
class ViewActivity : AppCompatActivity() {
    @Inject
    lateinit var webView: WebView


    var state = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = intent.getStringExtra("STATE").toString()


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
            if (state == WeLinesDialog.CONSTANTS.STATE_REGISTER) {
                WebViewToRegister(
                    webView = webView,
                    context = applicationContext
                )
            } else if (state == WeLinesDialog.CONSTANTS.STATE_ENTER) {
                val email = intent.getStringExtra(WeLinesDialog.CONSTANTS.STRING_EMAIL).toString()
                val password =
                    intent.getStringExtra(WeLinesDialog.CONSTANTS.STRING_PASSWORD).toString()

                WebViewToEnter(
                    webView = webView,
                    email = email,
                    password = password,
                    context = applicationContext
                )
            }

        }
    }


}


@Composable
fun WebViewToEnter(
    modifier: Modifier = Modifier,
    webView: WebView,
    email: String,
    password: String,
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


                    }
                }


            }
        }, update = {

        })

    BackHandler(enabled = backEnabled) {


        webView.goBack()
    }
}


@Composable
fun WebViewToRegister(
    modifier: Modifier = Modifier,
    webView: WebView,
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

                webView.loadUrl("https://billing.te.eg/ar-EG/MyTe/Register?")




                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)


                    }
                }


            }
        }, update = {

        })

    BackHandler(enabled = backEnabled) {
        webView.goBack()
    }
}
