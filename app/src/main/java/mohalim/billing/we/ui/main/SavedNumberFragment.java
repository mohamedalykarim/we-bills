package mohalim.billing.we.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import dagger.android.support.DaggerFragment;

public class SavedNumberFragment extends DaggerFragment {

    public static SavedNumberFragment newInstance() {

        Bundle args = new Bundle();

        SavedNumberFragment fragment = new SavedNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
