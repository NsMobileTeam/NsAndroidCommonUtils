package com.nextsense.utilbundle;

import android.os.Bundle;
import android.util.Log;

import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.databinding.ActivitySecondBinding;

public class SecondActivity extends NsActivity {
    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TestModel t = getExtra();
        binding.passedContent.setText(String.format("%s_%d", t.getName(), t.getNumber()));
    }
}