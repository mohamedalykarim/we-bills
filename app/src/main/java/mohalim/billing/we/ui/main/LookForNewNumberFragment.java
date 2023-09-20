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

import mohalim.billing.we.databinding.FragmentLookForNewNumberBinding;

import static mohalim.billing.we.core.utils.Constants.BASE_URL;

public class LookForNewNumberFragment extends Fragment {
    private boolean loading = true;

    @Inject
    WebView webView;

    FragmentLookForNewNumberBinding binding;

    public static LookForNewNumberFragment newInstance() {

        Bundle args = new Bundle();

        LookForNewNumberFragment fragment = new LookForNewNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLookForNewNumberBinding.inflate(inflater,container,false);


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
