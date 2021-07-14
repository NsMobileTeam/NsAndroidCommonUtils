package com.nextsense.utilbundle.Activities;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.NsActivityContract;
import com.nextsense.nsutils.commons.NsVault;
import com.nextsense.nsutils.uiBaseElements.NsActivity;
import com.nextsense.utilbundle.Models.CredentialModel;
import com.nextsense.utilbundle.databinding.ActivityLoginBinding;

public class LoginActivity extends NsActivity<ActivityLoginBinding> {
    private static final String SEC_DATA_KEY = "LoginCreds";

    @Override
    protected void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.login.setOnClickListener(view -> save());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            load();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void load() {
        NsVault.authLoad(SEC_DATA_KEY, CredentialModel.class, new NsVault.IVaultListener<CredentialModel>() {
            @Override
            public void authenticate(NsActivityContract.IContractInterface<Object, Object> listener) {
                startLauncher(null, listener);
            }

            @Override
            public void onFinished(@Nullable CredentialModel result, @Nullable Exception error) {
                if(result != null) {
                    binding.username.setText(result.username);
                    binding.password.setText(result.password);
                    binding.pin.setText(result.pin);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void save() {
        CredentialModel model = new CredentialModel(binding.username.getText().toString(), binding.password.getText().toString(), binding.pin.getText().toString());
        NsVault.authSave(SEC_DATA_KEY, model, new NsVault.IVaultListener<Boolean>() {
            @Override
            public void authenticate(NsActivityContract.IContractInterface<Object, Object> listener) {
                startLauncher(null, listener);
            }

            @Override
            public void onFinished(@Nullable Boolean result, Exception error) {
                CommonUtils.toast(result != null && result ? "Saved" : "Failed");
            }
        });
    }

    @Override
    protected ActivityLoginBinding getBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }
}