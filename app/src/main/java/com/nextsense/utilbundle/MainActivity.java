package com.nextsense.utilbundle;

import android.util.Log;
import android.util.Pair;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.listeners.IUniversalListener;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.nsutils.uiBaseElements.NsQuickDialog;
import com.nextsense.utilbundle.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends NsActivity<ActivityMainBinding> {

    @Override
    protected void onCreate() {
        binding.btnChangeLocale.setText(ResourceFetch.getString(R.string.locale_name));
        binding.btnChangeLocale.setOnClickListener(view -> LocaleUtil.setLocale(MainActivity.this, ResourceFetch.getString(R.string.locale_name).equals("EN") ? "MK" : "EN"));
        binding.textView.setOnClickListener(view -> startActivity(SecondActivity.class, new TestModel("Kupa", 123), null));
        NsDate date = new NsDate();
        binding.time.setText(date.toString("YYYY.MM.dd' 'HH:mm:ss.SSS' '(XXX)"));

        ArrayList<Pair<String, String>> presets = new ArrayList<>();
        presets.add(new Pair<>("username", "Boban"));
        presets.add(new Pair<>("password", "P@ssw0rd"));
        NsQuickDialog.show(this, R.layout.simple_name_dialog, presets, result -> Log.d("",""));
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}