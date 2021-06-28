package com.nextsense.utilbundle;

import android.util.Log;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.storage.NsPrefs;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
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
        testSharedList();
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    private void testSharedList() {
        ArrayList<TestModel> testModels = new ArrayList<>();
        testModels.add(new TestModel("Kupa_1", 1));
        testModels.add(new TestModel("Kupa_2", 2));
        testModels.add(new TestModel("Kupa_3", 3));
        testModels.add(new TestModel("Kupa_4", 4));
        testModels.add(new TestModel("Kupa_5", 5));
        testModels.add(new TestModel("Kupa_6", 6));
        testModels.add(new TestModel("Kupa_7", 7));
        testModels.add(new TestModel("Kupa_8", 8));
        testModels.add(new TestModel("Kupa_9", 9));
        testModels.add(new TestModel("Kupa_10", 10));
        testModels.add(new TestModel("Kupa_11", 11));
        testModels.add(new TestModel("Kupa_12", 12));
        testModels.add(new TestModel("Kupa_13", 13));
        testModels.add(new TestModel("Kupa_14", 14));
        testModels.add(new TestModel("Kupa_15", 15));

        NsPrefs.get().saveList("POPOP", testModels);
        ArrayList<TestModel> mods = NsPrefs.get().getList("POPOP", TestModel.class);
        String name = mods.get(0).getName();
        Log.d("","");
    }
}