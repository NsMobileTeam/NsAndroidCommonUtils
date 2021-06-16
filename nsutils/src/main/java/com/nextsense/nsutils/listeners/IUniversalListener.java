package com.nextsense.nsutils.listeners;

public interface IUniversalListener<T> {
    void onSuccess(T result);
    default void onFail(Exception e) {}
}
