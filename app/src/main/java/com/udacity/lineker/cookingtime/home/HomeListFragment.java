/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.udacity.lineker.cookingtime.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import java.util.List;

public class HomeListFragment extends Fragment {

    public static final String POSITION_INDEX = "POSITION_INDEX";
    public static final String TOP_VIEW = "TOP_VIEW";

    private ReceiptAdapter receiptAdapter;
    private FragmentHomeListBinding binding;

    private int positionIndex = -1;
    private int topView;

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
        updateInfoText(R.string.loading_receipts);

        if (savedInstanceState != null) {
            this.positionIndex = savedInstanceState.getInt(POSITION_INDEX);
            this.topView = savedInstanceState.getInt(TOP_VIEW);
        }
        return binding.getRoot();
    }

    private void updateInfoText(int resourceString) {
        if (resourceString > 0) {
            binding.infoText.setText(resourceString);
            binding.infoText.setVisibility(View.VISIBLE);
            binding.recyclerview.setVisibility(View.INVISIBLE);
        } else {
            binding.infoText.setText("");
            binding.infoText.setVisibility(View.INVISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        if (positionIndex!= -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGridLayoutManager.scrollToPositionWithOffset(positionIndex, topView);
                    positionIndex = -1;
                }
            },200);
        }
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

        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        View startView = binding.recyclerview.getChildAt(0);
        int topView = (startView == null) ? 0 : (startView.getTop() - binding.recyclerview.getPaddingTop());

        outState.putInt(POSITION_INDEX, mGridLayoutManager.findFirstVisibleItemPosition());
        outState.putInt(TOP_VIEW, topView);
        super.onSaveInstanceState(outState);
    }
}
