package com.udacity.lineker.cookingtime.steps;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FragmentMasterListBinding;
import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;
import com.udacity.lineker.cookingtime.step.StepActivity;
import com.udacity.lineker.cookingtime.util.CookingUtil;

import java.util.ArrayList;
import java.util.List;


public class MasterListFragment extends Fragment {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    public static final String SAVED_STEP_POSITION = "SAVED_STEP_POSITION";

    public static StepActivity.LastInfo lastInfo = new StepActivity.LastInfo();

    private FragmentMasterListBinding binding;
    private MasterListAdapter mAdapter;
    private Receipt receipt;

    OnStepClickListener mCallback;
    private boolean mTwoPane;
    private int currentStepPosition;

    public interface OnStepClickListener {
        boolean onStepSelected(MasterListItem item);
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

        this.mTwoPane = getResources().getBoolean(R.bool.twoPane);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_master_list, container, false);

        receipt = getArguments().getParcelable(ARG_RECEIPT);
        this.currentStepPosition = 1;
        if (savedInstanceState != null) {
            this.currentStepPosition = savedInstanceState.getInt(SAVED_STEP_POSITION);
        }

        int currentStepPosition = -1;
        if (mTwoPane) {
            currentStepPosition = lastInfo.recoverLastInfo ? lastInfo.stepPosition + 1 : this.currentStepPosition;
        }
        lastInfo.recoverLastInfo = false;
        // Create the adapter
        mAdapter = new MasterListAdapter(mCallback, currentStepPosition);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        binding.recyclerview.setLayoutManager(mGridLayoutManager);
        binding.recyclerview.setAdapter(mAdapter);
        mAdapter.setMasterList(getItems(receipt));
        // Return the root view
        return binding.getRoot();
    }

    private List<MasterListItem> getItems(Receipt receipt) {
        List<MasterListItem> result = new ArrayList<>();
        if (receipt.getIngredients() != null) {

            result.add(new MasterListItem(CookingUtil.getFormattedIngredients(this.getActivity(), receipt.getIngredients())));
        }
        for (int i = 0; i < receipt.getSteps().size(); i++) {
            Step step = receipt.getSteps().get(i);
            result.add(new MasterListItem(CookingUtil.getFormattedStep(step), receipt, i));
        }

        return result;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_STEP_POSITION, mTwoPane ? mAdapter.getCurrentStepPosition() : currentStepPosition);
        super.onSaveInstanceState(outState);
    }
}
