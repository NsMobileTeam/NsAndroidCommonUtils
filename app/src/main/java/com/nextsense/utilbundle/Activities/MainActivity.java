package com.nextsense.utilbundle.Activities;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.Models.TestModel;
import com.nextsense.utilbundle.R;
import com.nextsense.utilbundle.databinding.ActivityMainBinding;

public class MainActivity extends NsActivity<ActivityMainBinding> {

    @Override
    protected void onCreate() {
        binding.btnChangeLocale.setText(ResourceFetch.getString(R.string.locale_name));
        binding.btnChangeLocale.setOnClickListener(view -> LocaleUtil.setLocale(MainActivity.this, ResourceFetch.getString(R.string.locale_name).equals("EN") ? "MK" : "EN"));
        binding.textView.setOnClickListener(view -> startActivity(SecondActivity.class, new TestModel("Kupa", 123), null));
        NsDate date = new NsDate();
        binding.time.setText(date.toString("YYYY.MM.dd' 'HH:mm:ss.SSS' '(XXX)"));
        binding.btnLogin.setOnClickListener(view -> startActivity(LoginActivity.class));
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}