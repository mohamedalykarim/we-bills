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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.DialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint
import mohalim.billing.we.R
import mohalim.billing.we.ui.viewer.ViewActivity
import javax.inject.Inject


@AndroidEntryPoint
class BillsByPhoneNumberDialog : DialogFragment() {
    @Inject
    lateinit var webView: WebView


    object CONSTANTS {
        const val JAVASCRIPT_INTERFACE_NAME = "HTML_OUT"
        const val STATE_GETTING_BILL = "STATE_GETTING_BILL"
        const val STRING_PHONE_CODE = "STRING_PHONE_CODE"
        const val STRING_PHONE_NUMBER = "STRING_PHONE_NUMBER"
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


        return ComposeView(requireContext()).apply {


            setContent {

                BillsByPhoneNumberUI(webView, CONSTANTS, context)

            }
        }
    }

    companion object {
        const val TAG = "BillsByPhoneNumberDialog"
    }
}


@Composable
fun BillsByPhoneNumberUI(
    webView: WebView,
    CONSTANTS: BillsByPhoneNumberDialog.CONSTANTS,
    context: Context
) {
    var phoneNumber = ""


    val governerates = listOf(
        "القاهرة",
        "الجيزة",
        "القليوبية",
        "الاسكندرية",
        "البحيرة",
        "الدقهلية",
        "كفر الشيخ",
        "الغربية",
        "المنوفية",
        "دمياط",
        "بورسعيد",
        "الشرقية",
        "الاسماعيلية",
        "السويس",
        "شمال سيناء",
        "جنوب سيناء",
        "بني سويف",
        "الفيوم",
        "المنيا",
        "اسيوط",
        "سوهاج",
        "قنا",
        "الاقصر",
        "اسوان",
        "الوداي الجديد",
        "مطروح",
        "البحر الاحمر",
        "السادس من اكتوبر",
        "الشيخ زايد",
    )

    val phoneCodes = listOf(
        "02",
        "02",
        "013",
        "03",
        "045",
        "050",
        "047",
        "040",
        "048",
        "057",
        "066",
        "055",
        "064",
        "062",
        "068",
        "069",
        "082",
        "084",
        "086",
        "088",
        "093",
        "096",
        "095",
        "097",
        "092",
        "046",
        "065",
        "038",
        "02",
    )


    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("اختار كود المحافظة") }
    var selectedCode by remember { mutableStateOf(phoneCodes[0]) }
    var selectedPhoneNumber by remember { mutableStateOf("") }
    var currentPractice by remember { mutableStateOf("") }
    var scrollState = rememberScrollState()


    webView.addJavascriptInterface(
        DataByPhoneNumberLoadListerner(),
        CONSTANTS.JAVASCRIPT_INTERFACE_NAME
    )

    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            currentPractice = CONSTANTS.STATE_GETTING_BILL
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

            Image(
                painter = painterResource(id = R.drawable.we),
                contentDescription = "We Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            AdvertmRecView()


            OutlinedButton(modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(16.dp, 0.dp),
                shape = RoundedCornerShape(50.dp),
                onClick = { expanded = !expanded }) {

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.CenterVertically),
                    fontSize = 20.sp,
                    text = selectedItem
                )


            }



            AnimatedVisibility(expanded) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {

                    items(governerates.size) { item ->
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(64.dp, 8.dp, 64.dp, 8.dp),
                            onClick = {
                                expanded = !expanded
                                selectedItem = governerates[item] + " - " + phoneCodes[item]
                                selectedCode = phoneCodes[item]
                            }) {


                            Image(
                                painter = painterResource(id = R.drawable.phone),
                                contentDescription = "Phone Icon",
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically)
                            )

                            Spacer(modifier = Modifier.width(10.dp))



                            Text(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(),
                                text = governerates[item] + " - " + phoneCodes[item],
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = selectedPhoneNumber,
                onValueChange = { selectedPhoneNumber = it },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(16.dp, 0.dp),
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                label = {
                    Text(
                        text = "رقم التليفون",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }, textStyle = TextStyle.Default.copy(
                    fontSize = 20.sp,
                )

            )


            Spacer(modifier = Modifier.height(10.dp))

            if (currentPractice == CONSTANTS.STATE_GETTING_BILL && selectedCode != "0" && selectedPhoneNumber.length >= 6 && selectedPhoneNumber.length <= 8) {

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
                            intent.putExtra(CONSTANTS.STRING_PHONE_CODE, selectedCode)
                            intent.putExtra(CONSTANTS.STRING_PHONE_NUMBER, selectedPhoneNumber)
                            context.startActivity(intent)

                        },
                    ) {
                        Text(text = "عرض الفاتورة")
                    }

                }


            }


        }
    }

}

@Composable
fun AdvertmRecView(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                val addView = AdView(context)
                addView.setAdSize(AdSize.MEDIUM_RECTANGLE)
                addView.apply {
                    adUnitId = "ca-app-pub-5350581213670869/8633849860"
                    loadAd(AdRequest.Builder().build())
                }

            }
        )
    }
}

