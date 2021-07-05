package com.nextsense.utilbundle.Activities;

import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.Fragments.MainFragment;
import com.nextsense.utilbundle.Models.TestModel;
import com.nextsense.utilbundle.databinding.ActivitySecondBinding;

public class SecondActivity extends NsActivity<ActivitySecondBinding> {

    @Override
    protected void onCreate() {
        TestModel t = getExtra();
        if(t != null) {
            binding.passedContent.setText(t.toString());
        }

        MainFragment fragment = new MainFragment();
        fragment.setExtra(new TestModel("Bawb", 234));
        loadFragment(fragment, binding.mainContainer.getId(), true, null);
    }

    @Override
    protected ActivitySecondBinding getBinding() {
        return ActivitySecondBinding.inflate(getLayoutInflater());
    }
}