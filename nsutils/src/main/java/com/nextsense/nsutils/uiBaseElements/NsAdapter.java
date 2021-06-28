package com.nextsense.nsutils.uiBaseElements;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class NsAdapter<T extends ViewBinding> extends RecyclerView.Adapter<UniHolder<T>> {
    @NonNull
    @Override
    public final UniHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UniHolder<>(getBinding(LayoutInflater.from(parent.getContext()), parent));
    }

    public abstract T getBinding(LayoutInflater inflater, ViewGroup parent);

    @Override
    public abstract void onBindViewHolder(@NonNull UniHolder<T> holder, int position);

    @Override
    public abstract int getItemCount();
}
