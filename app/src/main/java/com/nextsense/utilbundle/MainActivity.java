package com.nextsense.utilbundle;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nextsense.nsutils.CommonUtils;
import com.nextsense.nsutils.NsDate;
import com.nextsense.nsutils.storage.DownloadUtil;
import com.nextsense.nsutils.storage.FileUtil;
import com.nextsense.utilbundle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}