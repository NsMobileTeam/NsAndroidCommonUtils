package com.nextsense.nsutils.listeners;

public interface IUniversalListener<T> {
    void onSuccess(T result);
    void onFail(Exception e);
}
