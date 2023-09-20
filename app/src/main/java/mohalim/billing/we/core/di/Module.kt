package mohalim.billing.we.core.di

import android.content.Context
import android.webkit.WebView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideWebView(@ApplicationContext context: Context): WebView {
        val webView: WebView = context.let { WebView(it) }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://billing.te.eg/ar-eg")

        return webView

    }
}