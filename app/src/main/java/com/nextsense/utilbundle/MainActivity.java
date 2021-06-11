package com.nextsense.utilbundle;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends NsActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnChangeLocale.setText(ResourceFetch.getString(R.string.locale_name));
        binding.btnChangeLocale.setOnClickListener(view -> LocaleUtil.setLocale(MainActivity.this, ResourceFetch.getString(R.string.locale_name).equals("EN") ? "MK" : "EN"));
        binding.textView.setOnClickListener(view -> startActivity(SecondActivity.class, new TestModel("Kupa", 123), null));

        NsDate date = new NsDate();
        binding.time.setText(date.toString("YYYY_MM_dd'~~'HH.mm.ss.SSS'~~'(XXX)"));
    }
}