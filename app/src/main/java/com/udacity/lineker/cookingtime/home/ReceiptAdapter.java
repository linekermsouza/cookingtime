package com.udacity.lineker.cookingtime.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FlavorItemBinding;
import com.udacity.lineker.cookingtime.model.Receipt;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    List<? extends Receipt> receiptList;

    @Nullable
    private final ReceiptClickCallback receiptClickCallback;

    public ReceiptAdapter(@Nullable ReceiptClickCallback receiptClickCallback) {
        this.receiptClickCallback = receiptClickCallback;
    }

    public void setReceiptList(final List<? extends Receipt> receiptList) {
        if (this.receiptList == null) {
            this.receiptList = receiptList;
            notifyItemRangeInserted(0, receiptList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ReceiptAdapter.this.receiptList.size();
                }

                @Override
                public int getNewListSize() {
                    return receiptList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ReceiptAdapter.this.receiptList.get(oldItemPosition).getId() ==
                            receiptList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Receipt project = receiptList.get(newItemPosition);
                    Receipt old = receiptList.get(oldItemPosition);
                    return project.getId() == old.getId();
                }
            });
            this.receiptList = receiptList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlavorItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.flavor_item,
                        parent, false);

        binding.setCallback(receiptClickCallback);
        return new ReceiptViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        holder.binding.setReceipt(receiptList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return receiptList == null ? 0 : receiptList.size();
    }

    static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        final FlavorItemBinding binding;

        public ReceiptViewHolder(FlavorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}