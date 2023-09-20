package mohalim.billing.we.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import mohalim.billing.we.R;
import mohalim.billing.we.databinding.ActivityMainBinding;
import mohalim.billing.we.databinding.TabCustomItemBinding;

import static mohalim.billing.we.core.utils.Constants.BASE_URL;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Inject
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        webView.loadUrl(BASE_URL);


        if (savedInstanceState == null) {
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_NONE);
            binding.viewPager.setAdapter(pagerAdapter);
            initTabs();

//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, LookUpByPhoneFragment.newInstance())
//                    .commitNow();

        }
    }

    private void initTabs() {

        TabLayout.Tab tabByPhone = binding.tab.newTab();
        TabLayout.Tab tabByAccount = binding.tab.newTab();
        TabLayout.Tab tabNewNumber = binding.tab.newTab();
        TabLayout.Tab tabSavedNumber = binding.tab.newTab();

        tabByPhone.setIcon(R.drawable.phone);

        binding.tab.addTab(tabByPhone);
        binding.tab.addTab(tabByAccount);
        binding.tab.addTab(tabNewNumber);
        binding.tab.addTab(tabSavedNumber);

        binding.tab.setupWithViewPager(binding.viewPager);



        for (int i = 0; i < binding.tab.getTabCount(); i++) {
            View view = TabCustomItemBinding.inflate(getLayoutInflater(), null,false).getRoot();
            if(i==0) ((TextView)view.findViewById(R.id.title)).setText("رقم التليفون");
            if(i==1) ((TextView)view.findViewById(R.id.title)).setText("رقم الحساب");
            if(i==2) ((TextView)view.findViewById(R.id.title)).setText("الرقم الجديد");
            if(i==3) ((TextView)view.findViewById(R.id.title)).setText("ارقامي");

            binding.tab.getTabAt(i).setCustomView(view);
        }

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
