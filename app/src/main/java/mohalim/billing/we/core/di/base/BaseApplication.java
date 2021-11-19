package mohalim.billing.we.core.di.base;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import mohalim.billing.we.core.di.component.AppComponent;
import mohalim.billing.we.core.di.component.DaggerAppComponent;

public class BaseApplication extends DaggerApplication {

    AppComponent appComponent;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().getInstance(this).app();

        return appComponent;
    }
}
