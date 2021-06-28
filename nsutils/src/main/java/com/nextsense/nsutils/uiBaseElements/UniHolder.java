package com.nextsense.nsutils.uiBaseElements;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class UniHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
    public T binding;

    /**
     * Create a universal recycler ViewHolder
     * @param binding the viewBinding
     */
    public UniHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
