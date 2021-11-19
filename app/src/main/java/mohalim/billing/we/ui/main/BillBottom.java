package mohalim.billing.we.ui.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

import java.util.List;

import mohalim.billing.we.R;
import mohalim.billing.we.core.model.Bill;
import mohalim.billing.we.databinding.BottomBillBinding;


public class BillBottom extends BottomSheetDialogFragment {
    BottomBillBinding binding;
    String phoneNumber;
    String accountNumber;
    String phoneType;
    String balance;

    boolean isZeroBills = false;

    List<Bill> billList;

    private InterstitialAd mInterstitialAd;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomBillBinding.inflate(inflater, container, false);
        binding.setPhone(phoneNumber);
        binding.setAccount(accountNumber);
        binding.setType(phoneType);
        binding.setBalance(balance);
        binding.setIsZero(isZeroBills);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-5350581213670869/5323847169");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    Log.d("TAG", "onFinish: loaded");

                }else {
                    Log.d("TAG", "onFinish: Not loaded");
                }
            }
        }.start();



        if (billList != null ){
            for (Bill bill : billList){
                View view = inflater.inflate(R.layout.row_bill, container, false);
                TextView billDate = view.findViewById(R.id.billDate);
                TextView billValue = view.findViewById(R.id.billValue);

                billDate.setText(bill.getBillDate());
                billValue.setText(bill.getBillValue());

                binding.billContainer.addView(view);
            }

        }




        return binding.getRoot();
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public void setZeroBills(boolean zeroBills) {
        isZeroBills = zeroBills;
    }
}
