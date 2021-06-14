package com.nextsense.utilbundle;

import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.databinding.ActivitySecondBinding;

public class SecondActivity extends NsActivity<ActivitySecondBinding> {

    @Override
    protected void onCreate() {
        TestModel t = getExtra();
        binding.passedContent.setText(String.format("%s_%d", t.getName(), t.getNumber()));
    }

    @Override
    protected ActivitySecondBinding getBinding() {
        return ActivitySecondBinding.inflate(getLayoutInflater());
    }
}