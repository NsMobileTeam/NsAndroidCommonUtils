package com.nextsense.nsutils.commons;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nextsense.nsutils.storage.NsPrefs;

@SuppressWarnings("unused")
public class NsActivityContract<I,O> extends ActivityResultContract<I, O> implements ActivityResultCallback<O> {
    private NsPrefs.IContractInterface<I,O> listener;

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, I input) {
        try {
            return listener.createIntent(context, input);
        } catch (Exception e) {
            return new Intent();
        }
    }

    @Override
    public O parseResult(int resultCode, @Nullable Intent intent) {
        try {
            return listener.parseResult(resultCode, intent);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onActivityResult(O result) {
        try {
            listener.onContractResult(result);
        } catch (Exception ignore) {}
        listener = null;
    }

    public void setListener(NsPrefs.IContractInterface<I, O> listener) {
        this.listener = listener;
    }
}
