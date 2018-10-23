package com.udacity.lineker.cookingtime.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.data.AndroidImageAssets;
import com.udacity.lineker.cookingtime.databinding.FragmentHomeListBinding;
import com.udacity.lineker.cookingtime.databinding.FragmentMasterListBinding;
import com.udacity.lineker.cookingtime.home.ReceiptClickCallback;
import com.udacity.lineker.cookingtime.model.Ingredient;
import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;

import java.util.ArrayList;
import java.util.List;


public class MasterListFragment extends Fragment {

    private FragmentMasterListBinding binding;
    private MasterListAdapter mAdapter;
    private Receipt receipt;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_master_list, container, false);

        receipt = getArguments().getParcelable("receipt");
        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        mAdapter = new MasterListAdapter(itemClickCallback);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        binding.recyclerview.setLayoutManager(mGridLayoutManager);
        // Set the adapter on the GridView
        binding.recyclerview.setAdapter(mAdapter);
        mAdapter.setMasterList(getItems(receipt));
        // Return the root view
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private final ItemClickCallback itemClickCallback = new ItemClickCallback() {
        @Override
        public void onClick(MasterListItem item) {

        }
    };

    private List<MasterListItem> getItems(Receipt receipt) {
        List<MasterListItem> result = new ArrayList<>();
        String ingredients = getString(R.string.ingredients_list) + ":\n";
        for (Ingredient ingredient : receipt.getIngredients()) {
            ingredients += String.format("- %s (%s %s)\n",
                    ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure());
        }
        result.add(new MasterListItem(ingredients, null));

        for (Step step : receipt.getSteps()) {
            String description = String.format("%s. %s",
                    step.getId(), step.getShortDescription());
            result.add(new MasterListItem(description, step));
        }

        return result;
    }
}
