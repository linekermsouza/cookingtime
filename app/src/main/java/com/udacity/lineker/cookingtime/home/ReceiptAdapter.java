package com.udacity.lineker.cookingtime.home;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flavor_item, parent, false);
        //view.setBackgroundResource(mBackground);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        //holder.binding.setProject(projectList.get(position));
        //holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return receiptList == null ? 0 : receiptList.size();
    }

    static class ReceiptViewHolder extends RecyclerView.ViewHolder {

        public ReceiptViewHolder(View view) {
            super(view);
        }
    }
}