package com.nextsense.utilbundle.Activities;

import android.os.Handler;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.commons.Note;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.nsutils.uiBaseElements.NsLoadingScreen;
import com.nextsense.utilbundle.Models.TestModel;
import com.nextsense.utilbundle.R;
import com.nextsense.utilbundle.databinding.ActivityMainBinding;

public class MainActivity extends NsActivity<ActivityMainBinding> {

    @Override
    protected void onCreate() {
        binding.btnChangeLocale.setText(ResourceFetch.getString(R.string.locale_name));
        binding.btnChangeLocale.setOnClickListener(view -> LocaleUtil.setLocale(MainActivity.this, ResourceFetch.getString(R.string.locale_name).equals("EN") ? "MK" : "EN"));
        binding.textView.setOnClickListener(view -> startActivity(SecondActivity.class, new TestModel("TestInfo", 123), null));
        NsDate date = new NsDate();
        binding.time.setText(date.toString("YYYY.MM.dd' 'HH:mm:ss.SSS' '(XXX)"));
        Note.o("TOAST", "Successful propagation");
        NsLoadingScreen.setDefaultLayout(R.layout.layout_default_loader);
        NsLoadingScreen.show(getSupportFragmentManager());
        new Handler().postDelayed(NsLoadingScreen::close, 6000);
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}