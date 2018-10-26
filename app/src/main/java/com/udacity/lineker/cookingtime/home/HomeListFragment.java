package com.udacity.lineker.cookingtime.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FragmentHomeListBinding;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.steps.StepsActivity;

import java.util.List;

public class HomeListFragment extends Fragment implements View.OnClickListener {

    private ReceiptAdapter receiptAdapter;
    private FragmentHomeListBinding binding;

    private GridLayoutManager mGridLayoutManager;

    // Mandatory empty constructor
    public HomeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_list, container, false);

        receiptAdapter = new ReceiptAdapter(receiptClickCallback);

        mGridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.receipt_list_columns));
        binding.recyclerview.setLayoutManager(mGridLayoutManager);
        binding.recyclerview.setAdapter(receiptAdapter);
        binding.update.setOnClickListener(this);
        updateInfoText(R.string.loading_receipts);

        return binding.getRoot();
    }

    private void updateInfoText(int resourceString) {
        if (resourceString > 0) {
            binding.infoText.setText(resourceString);
            binding.infoText.setVisibility(View.VISIBLE);
            binding.update.setVisibility(resourceString == R.string.loading_receipts ?
                    View.INVISIBLE : View.VISIBLE);
            binding.recyclerview.setVisibility(View.INVISIBLE);
        } else {
            binding.infoText.setText("");
            binding.infoText.setVisibility(View.INVISIBLE);
            binding.update.setVisibility(View.INVISIBLE);
            binding.recyclerview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ReceiptsViewModel viewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ReceiptsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getReceiptListObservable().observe(this, new Observer<List<Receipt>>() {
            @Override
            public void onChanged(@Nullable List<Receipt> receipts) {
                if (receipts == null) {
                    updateInfoText(R.string.error_network);
                } else {
                    updateInfoText(receipts.size() == 0 ? R.string.empty_list : 0);
                    receiptAdapter.setReceiptList(receipts);
                }
            }
        });
    }

    private final ReceiptClickCallback receiptClickCallback = new ReceiptClickCallback() {
        @Override
        public void onClick(Receipt receipt) {
            Bundle b = new Bundle();
            b.putParcelable(StepsActivity.ARG_RECEIPT, receipt);

            final Intent intent = new Intent(getContext(), StepsActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
            updateInfoText(R.string.loading_receipts);
            final ReceiptsViewModel viewModel =
                    ViewModelProviders.of(this).get(ReceiptsViewModel.class);

            viewModel.forceUpdate();
            observeViewModel(viewModel);
        }
    }
}
