package mohalim.billing.we.core.di.activities.main;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.billing.we.ui.main.LookForNewNumberFragment;
import mohalim.billing.we.ui.main.LookUpByAccountFragment;
import mohalim.billing.we.ui.main.LookUpByPhoneFragment;
import mohalim.billing.we.ui.main.SavedNumberFragment;

@Module
public abstract class mainFragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract LookUpByPhoneFragment contributeLookUpByPhoneFragment();

    @ContributesAndroidInjector
    abstract LookUpByAccountFragment contributeLookUpByAccountFragment();

    @ContributesAndroidInjector
    abstract LookForNewNumberFragment contributeLookForNewNumberFragment();

    @ContributesAndroidInjector
    abstract SavedNumberFragment contributeSavedNumberFragment();

    @ContributesAndroidInjector
    abstract BottomSheetDialogFragment contributeBottomSheetDialogFragment();


}
