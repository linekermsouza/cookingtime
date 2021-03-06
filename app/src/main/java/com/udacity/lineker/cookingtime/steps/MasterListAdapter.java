package com.udacity.lineker.cookingtime.steps;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.MasterListItemBinding;
import com.udacity.lineker.cookingtime.model.MasterListItem;

import java.util.List;


public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MasterListViewHolder> {

    List<? extends MasterListItem> masterList;

    @Nullable
    private final MasterListFragment.OnStepClickListener itemClickCallback;
    private int currentStepPosition;

    public MasterListAdapter(@Nullable MasterListFragment.OnStepClickListener itemClickCallback, int currentStepPosition) {
        this.itemClickCallback = itemClickCallback;
        this.currentStepPosition = currentStepPosition;
    }

    public void setMasterList(final List<? extends MasterListItem> masterList) {
        if (this.masterList == null) {
            this.masterList = masterList;
            notifyItemRangeInserted(0, masterList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MasterListAdapter.this.masterList.size();
                }

                @Override
                public int getNewListSize() {
                    return masterList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MasterListAdapter.this.masterList.get(oldItemPosition).getDescription() ==
                            masterList.get(newItemPosition).getDescription();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MasterListItem item = masterList.get(newItemPosition);
                    MasterListItem old = masterList.get(oldItemPosition);
                    return item.getDescription() == old.getDescription();
                }
            });
            this.masterList = masterList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public MasterListAdapter.MasterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MasterListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.master_list_item,
                        parent, false);

        return new MasterListAdapter.MasterListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MasterListAdapter.MasterListViewHolder holder, int position) {
        holder.binding.setItem(masterList.get(position));
        holder.binding.itemContainer.setBackgroundResource(position == currentStepPosition ?
            R.color.colorAccent : R.color.colorPrimary);
        holder.binding.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickCallback.onStepSelected(holder.binding.getItem())) {
                    MasterListAdapter.this.currentStepPosition = holder.binding.getItem().getStepPosition() + 1;
                    notifyDataSetChanged();
                }
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return masterList == null ? 0 : masterList.size();
    }

    static class MasterListViewHolder extends RecyclerView.ViewHolder {
        final MasterListItemBinding binding;

        public MasterListViewHolder(MasterListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public int getCurrentStepPosition() {
        return currentStepPosition;
    }
}
