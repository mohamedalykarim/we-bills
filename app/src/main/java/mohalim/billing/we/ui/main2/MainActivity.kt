package mohalim.billing.we.ui.main2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import mohalim.billing.we.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityUI(supportFragmentManager)
        }
    }
}


@Composable
fun MainActivityUI(supportFragmentManager: FragmentManager) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .paint(
                painterResource(id = R.drawable.transparent_bg),
                contentScale = ContentScale.FillBounds
            )
            .verticalScroll(scrollState)
    )
    {

        Image(
            painter = painterResource(id = R.drawable.we),
            contentDescription = "We Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Buttons(supportFragmentManager)

    }


}

@Composable
fun Buttons(supportFragmentManager: FragmentManager) {

    Column(modifier = Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp)) {

        /** Get bills by phone number button **/
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = {
                BillsByPhoneNumberDialog().show(
                    supportFragmentManager,
                    BillsByPhoneNumberDialog.TAG
                )
            }) {

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.phone_number_icon),
                        contentDescription = "Phone Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        text = "الاستعلام برقم التليفون"
                    )
                }


            }


        }

        Spacer(modifier = Modifier.height(20.dp))


        /** Get bills by Account number button **/
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = { /*TODO*/ }) {

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.account_number_icon),
                        contentDescription = "Phone Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        text = "الاستعلام برقم الحساب"
                    )
                }


            }


        }

        Spacer(modifier = Modifier.height(20.dp))


        /** Get new number button **/
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = { /*TODO*/ }) {

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.phone_number),
                        contentDescription = "Phone Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        text = "استعلام عن الرقم الجديد"
                    )
                }


            }


        }

        Spacer(modifier = Modifier.height(20.dp))

        /** Get new number button **/
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = { /*TODO*/ }) {

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.contacts_icon),
                        contentDescription = "Phone Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        text = "أرقامي"
                    )
                }


            }


        }

    }


}






