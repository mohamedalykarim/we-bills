package mohalim.billing.we.ui.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mohalim.billing.we.core.model.Bill;
import mohalim.billing.we.core.utils.AppExecutor;
import mohalim.billing.we.databinding.FragmentLookUpByPhoneBinding;

import static mohalim.billing.we.core.utils.Constants.BASE_URL;

public class LookUpByPhoneFragment extends DaggerFragment {
    FragmentLookUpByPhoneBinding binding;
    private boolean loading = true;
    private boolean countDownFinished;

    int error;

    BillBottom billBottom;


    @Inject
    WebView webView;

    @Inject
    AppExecutor appExecutor;

    private CountDownTimer countDownTimer;

    public static LookUpByPhoneFragment newInstance() {
        Bundle args = new Bundle();

        final LookUpByPhoneFragment fragment = new LookUpByPhoneFragment();
        fragment.setArguments(args);


        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLookUpByPhoneBinding.inflate(inflater,container,false);
        binding.setLoading(loading);

        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5350581213670869/8633849860");

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);




        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
                if (error > 0)return;
                if (loading) return;

                loading = true;
                binding.setLoading(loading);


                webView.loadUrl("javascript:document.getElementById(\"TxtAreaCode\").value = "+ Integer.parseInt(binding.codeET.getText().toString()));
                webView.loadUrl("javascript:document.getElementById(\"TxtPhoneNumber\").value = "+Integer.parseInt(binding.phoneET.getText().toString()));
                webView.loadUrl("javascript:(document.querySelectorAll(\"form#FrmInquiryByPhone div.form-inline div.form-group button.btn\"))[0].click()");



                countDownTimer = new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countDownFinished = false;

                        webView.loadUrl("javascript:window.HTMLOUT.processHTML(" +
                                "document.querySelectorAll('html body main.container-double--ver.clearfix.one-one.align-center')[0].innerHTML" +
                                ");");
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(getContext(), "رجاء التأكد من ادخال كود المنطقة ورقم التليفون بشكل صحيح", Toast.LENGTH_SHORT).show();
                        countDownFinished = true;

                        webView.loadUrl("javascript:window.HTMLOUT.processHTML(" +
                                "document.querySelectorAll('html body main.container-double--ver.clearfix.one-one.align-center')[0].innerHTML" +
                                ");");
                    }
                }.start();



            }
        });

        return binding.getRoot();
    }

    private void validateForm() {
        error = 0;

        if (binding.codeET.getText().toString().equals("")){
            binding.codeET.setError("رجاء ادخال كود المنطقة");
            error++;
        }

        if (binding.phoneET.getText().toString().equals("")){
            binding.phoneET.setError("رجاء ادخال رقم التليفون");
            error++;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        loading = true;
        binding.setLoading(loading);


        webView.loadUrl(BASE_URL);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                loading = false;
                binding.setLoading(loading);
            }
        });



    }

    class LoadListener{

        @JavascriptInterface
        public void processHTML(String html)
        {
            Document document = Jsoup.parse(html);



            if (html.contains(""+ Integer.parseInt(binding.phoneET.getText().toString()))){

                if(billBottom == null){
                    billBottom = new BillBottom();
                }

                String zeroBillString = document.select("div.col-md-12").get(0).html();


                if (zeroBillString.contains("لا يوجد فواتير مستحقة للسداد")){

                    Log.d("TAG", "processHTML: "+zeroBillString);

                    initPhoneDaa(document);

                    loading = false;
                    binding.setLoading(loading);
                    countDownTimer.cancel();
                    billBottom.setZeroBills(true);


                }
                else
                {
                    Log.d("TAG", "processHTML: ok");


                    initPhoneDaa(document);
                    Elements billElements = document.select("table").get(0).select("tbody tr");

                    ArrayList<Bill> bills = new ArrayList<>();

                    for (Element billElement : billElements) {
                        String billDate = billElement.select("td").get(0).html();
                        String billValue = billElement.select("td").get(1).html();
                        Bill bill = new Bill();
                        bill.setBillDate(billDate);
                        bill.setBillValue(billValue);

                        bills.add(bill);
                    }


                    billBottom.setBillList(bills);

                }



                if (billBottom.isAdded()){
                    billBottom.dismiss();
                }

                billBottom.show(getFragmentManager(), "BillBottom");

                loading = false;
                binding.setLoading(loading);
                countDownTimer.cancel();
            }
            else {

                if (countDownFinished){

                    loading = false;
                    binding.setLoading(loading);

                    binding.codeET.setError("تأكد من الكود");
                    binding.phoneET.setError("تأكد من الرقم");

                    binding.getRoot().clearFocus();

                }

            }
        }
    }

    private void initPhoneDaa(Document document) {
        Elements phoneElements = document.select("span.TelNo");
        Elements noWrapElements = document.select("div.NoWrap");
        Elements balanceElements = document.select("input.InputLnk");


        String phone = phoneElements.get(0).html();
        String account = noWrapElements.get(1).text().replace("رقم الحساب: ", "");
        String type = noWrapElements.get(2).text().replace("فئة التليفون : ", "");

        if (balanceElements.size() > 0){
            String balance = balanceElements.get(0).val();
            billBottom.setBalance(balance);
        }

        billBottom.setPhoneNumber(phone);
        billBottom.setAccountNumber(account);
        billBottom.setPhoneType(type);
    }


}
