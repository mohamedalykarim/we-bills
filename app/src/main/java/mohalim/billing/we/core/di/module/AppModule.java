package mohalim.billing.we.core.di.module;

import android.app.Application;
import android.webkit.WebSettings;
import android.webkit.WebView;

import dagger.Module;
import dagger.Provides;
import mohalim.billing.we.core.utils.AppExecutor;

@Module
public class AppModule {

    @Provides
    static WebView provideWebView(Application application){
        WebView webView = new WebView(application);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return webView;
    }


    @Provides
    static AppExecutor provideAppExecuter(){
        return AppExecutor.getInstance();
    }
}
