package mohalim.billing.we.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mohalim.billing.we.databinding.FragmentLookUpByAccountBinding;
import mohalim.billing.we.databinding.FragmentLookUpByPhoneBinding;

import static mohalim.billing.we.core.utils.Constants.BASE_URL;

public class LookUpByAccountFragment extends DaggerFragment {
    FragmentLookUpByAccountBinding binding;
    private boolean loading = true;

    @Inject
    WebView webView;

    public static LookUpByAccountFragment newInstance() {
        Bundle args = new Bundle();

        LookUpByAccountFragment fragment = new LookUpByAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLookUpByAccountBinding.inflate(inflater,container,false);
        return binding.getRoot();
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
}
