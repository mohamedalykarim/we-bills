package mohalim.billing.we.core.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.google.android.material.tabs.TabLayout;

public class CustomSetter {
    public static Typeface type;

    public final static String DEFAULT_ARABIC_FONT = "fonts/droid.ttf";

    @BindingAdapter("android:fontType")
    public static void setFont(View view, String font){
        Log.d("TAG", "setFont: "+font);

        if (type==null){
            type = Typeface.createFromAsset(view.getContext().getAssets(),font);
        }

        if (view instanceof TextView){
            ((TextView) view).setTypeface(type);
        }else if (view instanceof EditText){
            ((EditText) view).setTypeface(type);
        }else if (view instanceof Button){
            ((Button) view).setTypeface(type);
        }
    }

}
