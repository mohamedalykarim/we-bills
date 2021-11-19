package mohalim.billing.we.core.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import mohalim.billing.we.core.di.base.BaseApplication;
import mohalim.billing.we.core.di.module.ActivityBuilderModule;
import mohalim.billing.we.core.di.module.AppModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        AppModule.class
})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder getInstance(Application application);

        AppComponent app();

    }

}
