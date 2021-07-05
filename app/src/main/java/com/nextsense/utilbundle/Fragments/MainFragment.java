package com.nextsense.utilbundle.Fragments;

import android.view.LayoutInflater;

import com.nextsense.nsutils.uiBaseElements.NsFragment;
import com.nextsense.utilbundle.Models.TestModel;
import com.nextsense.utilbundle.databinding.FragmentMainBinding;

public class MainFragment extends NsFragment<FragmentMainBinding> {

    @Override
    public void onCreateView() {
        TestModel t = getExtra();
        binding.tvMessage.setText(t.toString());
    }

    @Override
    protected FragmentMainBinding getBinding(LayoutInflater inflater) {
        return FragmentMainBinding.inflate(inflater);
    }
}