package com.nextsense.nsutils.uiBaseElements;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class UniHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
    public T binding;

    /**
     * Create a universal recycler ViewHolder
     * @param itemView the represented view
     * @param binding the viewBinding
     */
    public UniHolder(@NonNull View itemView, T binding) {
        super(itemView);
        this.binding = binding;
    }
}
