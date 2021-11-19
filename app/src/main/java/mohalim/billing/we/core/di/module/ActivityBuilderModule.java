package mohalim.billing.we.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.billing.we.core.di.activities.main.mainFragmentsBuilderModule;
import mohalim.billing.we.ui.main.MainActivity;
import mohalim.billing.we.ui.splash.SplashActivity;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();


    @ContributesAndroidInjector(
            modules = {
                    mainFragmentsBuilderModule.class
            }
    )
    abstract MainActivity contributeMainActivity();


}
