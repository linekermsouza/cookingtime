package com.udacity.lineker.cookingtime.step;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FragmentStepBinding;
import com.udacity.lineker.cookingtime.model.Step;

public class StepFragment extends Fragment {

    public static final String ARG_STEP = "ARG_STEP";

    // Tag for logging
    private static final String TAG = "StepFragment";

    private FragmentStepBinding binding;
    private Step step;

    public StepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);

        step = getArguments().getParcelable(ARG_STEP);

        binding.description.setText(step.getDescription());
        // Return the rootView
        return binding.getRoot();
    }
}
