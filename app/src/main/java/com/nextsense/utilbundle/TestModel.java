package com.nextsense.utilbundle;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TestModel implements Serializable {
    private String name;
    private int number;

    public TestModel(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s_%d", getName(), getNumber());
    }
}
