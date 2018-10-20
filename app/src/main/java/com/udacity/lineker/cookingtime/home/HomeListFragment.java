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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.model.Receipt;

import java.util.List;


// This fragment displays all of the AndroidMe images in one large list
// The list appears as a grid of images
public class HomeListFragment extends Fragment {

    private ReceiptAdapter receiptAdapter;

    // Mandatory empty constructor
    public HomeListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        receiptAdapter = new ReceiptAdapter(receiptClickCallback);


        final View rootView = inflater.inflate(R.layout.fragment_home_list, container, false);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        recyclerView.setAdapter(receiptAdapter);

        // Return the root view
        return rootView;
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
                if (receipts != null) {
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
}
