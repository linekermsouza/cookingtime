package com.udacity.lineker.cookingtime.steps;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FragmentMasterListBinding;
import com.udacity.lineker.cookingtime.model.Ingredient;
import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;

import java.util.ArrayList;
import java.util.List;


public class MasterListFragment extends Fragment {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";

    private FragmentMasterListBinding binding;
    private MasterListAdapter mAdapter;
    private Receipt receipt;

    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(MasterListItem item);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
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

        receipt = getArguments().getParcelable(ARG_RECEIPT);
        // Create the adapter
        mAdapter = new MasterListAdapter(mCallback);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        binding.recyclerview.setLayoutManager(mGridLayoutManager);
        binding.recyclerview.setAdapter(mAdapter);
        mAdapter.setMasterList(getItems(receipt));
        // Return the root view
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private List<MasterListItem> getItems(Receipt receipt) {
        List<MasterListItem> result = new ArrayList<>();
        if (receipt.getIngredients() != null) {
            String ingredients = getString(R.string.ingredients_list) + ":\n";
            for (Ingredient ingredient : receipt.getIngredients()) {
                ingredients += String.format("- %s (%s %s)\n",
                        ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure());
            }
            result.add(new MasterListItem(ingredients));
        }
        for (int i = 0; i < receipt.getSteps().size(); i++) {
            Step step = receipt.getSteps().get(i);
            String description = String.format("%s. %s",
                    step.getId(), step.getShortDescription());
            result.add(new MasterListItem(description, receipt, i));
        }

        return result;
    }
}
