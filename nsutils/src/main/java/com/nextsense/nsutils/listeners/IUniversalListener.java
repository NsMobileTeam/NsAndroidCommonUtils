package com.nextsense.nsutils.listeners;

@SuppressWarnings("unused")
public interface IUniversalListener<T> {
    void onSuccess(T result);
    default void onFail(Exception e) {}
}
